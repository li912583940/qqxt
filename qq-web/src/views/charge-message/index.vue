<!--
	描述：话费充值
-->
<template>
  <div class="app-container">
  	<div class="filter-container">
  	  <el-select clearable style="width: 200px" class="filter-item" v-model="listQuery.jq" placeholder="选择监区">
        <el-option v-for="item in jqs" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入罪犯编号" v-model="listQuery.frNo" clearable>
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入罪犯姓名" v-model="listQuery.frName" clearable>
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('criminal.search')}}</el-button>
    </div>
    
    <el-table :key='tableKey' :data="list"   border fit highlight-current-row
      style="width: 1001px">
      <el-table-column width="200" align="center" :label="$t('currency.jqName')" >
        <template slot-scope="scope">
          <span>{{scope.row.jqName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="200" align="center" :label="$t('currency.frNo')">
        <template slot-scope="scope">
          <span>{{scope.row.frNo}}</span>
        </template>
      </el-table-column>
      <el-table-column width="200" align="center" :label="$t('currency.frName')">
        <template slot-scope="scope">
          <span>{{scope.row.frName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="200" align="center" label="话费余额">
        <template slot-scope="scope">
          <span>{{scope.row.qqYe | qqYeFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="buttonRole.rechargePermission==1 || buttonRole.refundPermission==1" align="center" :label="$t('criminal.actions')" width="200" >
        <template slot-scope="scope">
          <el-button v-if="buttonRole.rechargePermission==1" type="primary" size="mini" @click="openRecharge(scope.row)">充值</el-button>
          <el-button v-if="buttonRole.refundPermission==1" size="mini" type="danger" @click="openRefund(scope.row)">出狱退费</el-button>
        </template>
      </el-table-column>
    </el-table>

		<!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

	<!-- 充值 开始 -->
    <el-dialog title="充值" :visible.sync="dialogRechargeVisible">
      <el-form :rules="rulesRecharge" :model="dataRechargeForm" ref="dataRechargeForm" label-position="right" label-width="120px" style='width: 400px; margin-left:25%;' >
        <el-form-item :label="$t('currency.jqName')">
        	<el-input v-model="dataRechargeForm.jqName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item :label="$t('currency.frNo')">
        	<el-input v-model="dataRechargeForm.frNo" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item :label="$t('currency.frName')">
        	<el-input v-model="dataRechargeForm.frName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="话费余额">
        	<el-input v-model="dataRechargeForm.qqYe" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="充值金额(元)" prop="czje">
          <el-input v-model="dataRechargeForm.czje"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogRechargeVisible = false">取 消</el-button>
        <el-button type="primary" @click="requestRecharge">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 充值 结束 -->
    
  </div>
</template>

<script>
import { findPojo, RequestRecharge} from '@/api/chargeMessage'
import { findList as findJqList} from '@/api/jqSet'

import moment from 'moment'
import waves from '@/directive/waves' // 水波纹指令


export default {
  name: 'chargeMessage',
  directives: {
    waves
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: null,
      listQuery: {
      	jq: undefined,
      	frNo: undefined,
      	frName: undefined,
        pageNum: 1,
        pageSize: 20
      },
      jqs: [],// 监区下拉选框
      
      // 充值 开始
      frname: undefined,
      dialogRechargeVisible: false,
      dataRechargeForm: {
      	webId: undefined,
        jqName: undefined,
        frNo: undefined,
        frName: undefined,
        qqYe: undefined,
        czje: undefined
      },
      rulesRecharge: {
        czje: [{ required: true, message: '充值金额不能为空', trigger: 'blur' }]
      },
      // 充值 结束 
      
       
      
      //按钮权限   1：有权限， 0：无权限
      buttonRole: { 
      	queryPermission: 1, 
      	rechargePermission: 0,
      	refundPermission: 0,
      }
      
    }
  },
  filters: {
    dateFormat(data) {
		//时间格式化  
	    if (data == undefined) {  
	      return "";  
	    }  
	    return moment(data).format("YYYY-MM-DD HH:mm:ss");  
	},
	qqYeFormat(data){
		if(data == undefined){
			return 0;
		}
		return data/1000;
	},
  },
  created() {
    this.getList()
  },
  mounted() {
    this.setButtonRole()
    this.getJqList()
  },
  methods: {
    getList() {
      findPojo(this.listQuery).then((res) => {
      	 this.list = res.pojo.list
      	 this.total = res.pojo.count
      })
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val
      this.getList()
    },
    
    setButtonRole() { //设置按钮的权限
    	let roles = sessionStorage.getItem("roles")
    	if(roles.includes('admin')){
    		this.buttonRole.rechargePermission= 1
    		this.buttonRole.editPermission= 1
    		this.buttonRole.refundPermission= 1
    	}else{
    		let buttonRoles = JSON.parse(sessionStorage.getItem("buttonRoles"))
    		let chargeMessage = buttonRoles.chargeMessage
    		if(chargeMessage.length>0){
    			for(let value of chargeMessage){
    				if(value=='rechargePermission'){
    					this.buttonRole.rechargePermission= 1
    				}else if(value=='refundPermission'){
    					this.buttonRole.refundPermission= 1
    				}
    			}
    		}
    	}
    },
    
    getJqList() { //监区下拉框
    	if(this.jqs.length === 0) {
    		findJqList({}).then((res) => {
	    		let list = res.list
	    		for(let x of list){
				  let value = {}
				  value.id = x.jqNo
				  value.name = x.jqName
				  this.jqs.push(value)
				}
	    	})
    	}
    },

    //重置表单
	resetForm() {
		this.dataRechargeForm.webId = undefined
		this.dataRechargeForm.jqName = undefined
        this.dataRechargeForm.frNo = undefined
        this.dataRechargeForm.frName = undefined
        this.dataRechargeForm.qqYe = undefined
        this.dataRechargeForm.czje= undefined
    },
    openRecharge(row) {
      this.resetForm()
      this.dialogRechargeVisible = true
      
      this.dataRechargeForm.webId = row.webId
      this.dataRechargeForm.jqName = row.jqName
	  this.dataRechargeForm.frNo = row.frNo
	  this.dataRechargeForm.frName = row.frName
	  let qqYe = 0
	  if(row.qqYe){
	  	qqYe=row.qqYe
	  }
	  this.dataRechargeForm.qqYe = qqYe
	  this.dataRechargeForm.czje= row.czje
    },
    requestRecharge() {
      this.$refs['dataRechargeForm'].validate((valid) => {
        if (valid) {
        	let param ={
        		webId: this.dataRechargeForm.webId,
        		czje: this.dataRechargeForm.czje
        	}
          RequestRecharge(param).then(() => {
            this.dialogRechargeVisible = false
            this.getList()
          }).catch(error => {
		    this.dialogRechargeVisible = false
		  })
        }
      })
    },
    //删除
	handleDelete(row) {
	    this.$confirm('确认删除该记录吗?', '提示', {
		    type: 'warning'
		}).then(() => {
			this.listLoading = true;
			let param = {
	    		d: row.webId
	    	}
			RequestDelete(param).then(() => {
	    		this.getList()
	        }).catch(error => {
		        this.dialogFormVisible = false
		    })
		})
	},
	dateFormats: function (val) {
		if(!val){
			return undefined
		}
		return moment(val).format("YYYY-MM-DD HH:mm:ss");
	},
  }
}
</script>
