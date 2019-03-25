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
    
    <el-table :key='tableKey' :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
      style="width: 1101px">
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
      <el-table-column v-if="buttonRole.rechargePermission==1 || buttonRole.refundPermission==1" align="center" :label="$t('criminal.actions')" width="180" >
        <template slot-scope="scope">
          <el-button v-if="buttonRole.rechargePermission==1" type="primary" size="mini" @click="openRecharge(scope.row)">充值</el-button>
          <el-button v-if="buttonRole.refundPermission==1" size="mini" type="danger" @click="requestRefund(scope.row)">出狱退费</el-button>
        </template>
      </el-table-column>
      <el-table-column v-if="buttonRole.detailsPermission==1" align="center" label="摘要" width="120" >
        <template slot-scope="scope">
          <el-button v-if="buttonRole.detailsPermission==1" type="primary" size="mini" @click="openDetails(scope.row)">充值明细</el-button>
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
    
    
    <!-- 充值明细 结束 -->
    <el-dialog title="充值明细" :visible.sync="dialogDetailsVisible" width="961px">
        <el-table :key='detailsTableKey' :data="detailsList" v-loading="detailsListLoading" element-loading-text="给我一点时间" border fit highlight-current-row
	      style="width: 861px;margin-left: 20px;">
	      <el-table-column width="160" align="center" label="充值时间">
	        <template slot-scope="scope">
	          <span>{{scope.row.czsj | dateFormat}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="110" align="center" label="充值金额">
	        <template slot-scope="scope">
	          <span>{{scope.row.czje | qqYeFormat}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="110" align="center" label="状态">
	        <template slot-scope="scope">
	          <span v-if="scope.row.czzt==1">已充值</span>
	          <span v-if="scope.row.czzt==0">已修改</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="110" align="center" label="充值操作员">
	        <template slot-scope="scope">
	          <span>{{scope.row.czrName}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="110" align="center" label="修改操作员">
	        <template slot-scope="scope">
	          <span>{{scope.row.scrName}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="160" align="center" label="修改时间">
	        <template slot-scope="scope">
	          <span>{{scope.row.scsj | dateFormat}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column v-if="buttonRole.detailsUpdatePermission==1" align="center" :label="$t('criminal.actions')" width="100" >
	        <template slot-scope="scope">
	          <el-button v-if="buttonRole.detailsUpdatePermission==1" type="primary" size="mini" @click="openDetailsUpdate(scope.row)">修改</el-button>
	        </template>
	      </el-table-column>
	    </el-table>
	    <!-- 分页 -->
	    <div class="pagination-container">
	      <el-pagination background @size-change="handleDetailsSizeChange" @current-change="handleDetailsCurrentChange" :current-page="detailsListQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="detailsListQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="detailsTotal">
	      </el-pagination>
	    </div>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="dialogDetailsVisible = false">关闭</el-button>
        </span>
    </el-dialog>
    <!-- 充值明细 结束 -->
    
    <!-- 修改充值 开始 -->
    <el-dialog title="充值" :visible.sync="dialogDetailsUpdateVisible">
      <el-form :rules="rulesDetailsUpdate" :model="dataDetailsUpdateForm" ref="dataDetailsUpdateForm" label-position="right" label-width="120px" style='width: 400px; margin-left:25%;' >
        <el-form-item :label="$t('currency.jqName')">
        	<el-input v-model="dataDetailsUpdateForm.jqName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item :label="$t('currency.frNo')">
        	<el-input v-model="dataDetailsUpdateForm.frNo" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item :label="$t('currency.frName')">
        	<el-input v-model="dataDetailsUpdateForm.frName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="充值金额(元)" prop="czje">
          <el-input v-model="dataDetailsUpdateForm.czje"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailsUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="requestDetailsUpdate">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 修改充值 结束 -->
  </div>
</template>

<script>
import { findPojo, RequestRecharge, RequestRefund, findDetailsPojo, RequestDetailsUpdate} from '@/api/chargeMessage'
import { findList as findJqList} from '@/api/jqSet'

import moment from 'moment'
import waves from '@/directive/waves' // 水波纹指令
import { Message, MessageBox } from 'element-ui'

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
      listLoading: true,
      listQuery: {
      	jq: undefined,
      	frNo: undefined,
      	frName: undefined,
        pageNum: 1,
        pageSize: 10
      },
      jqs: [],// 监区下拉选框
      
      // 充值 开始
      frname: undefined,
      dialogRechargeVisible: false,
      dataRechargeForm: {
      	webid: undefined,
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
      
      /* 充值明细 开始 */ 
      detailsTableKey: 0,
      detailsList: null,
      detailsTotal: null,
      detailsListLoading: true,
      detailsListQuery: {
      	frNo: undefined,
        pageNum: 1,
        pageSize: 10
      },
      dialogDetailsVisible: false,
      /* 充值明细 结束 */
     
      /* 修改充值 开始 */
      dialogDetailsUpdateVisible:false,
      dataDetailsUpdateForm: {
      	czId: undefined,
        jqName: undefined,
        frNo: undefined,
        frName: undefined,
        czje: undefined
      },
      rulesDetailsUpdate: {
        czje: [{ required: true, message: '充值金额不能为空', trigger: 'blur' }]
      },
      /* 修改充值 结束 */
      
      //按钮权限   1：有权限， 0：无权限
      buttonRole: { 
      	queryPermission: 1, 
      	rechargePermission: 0,
      	refundPermission: 0,
      	detailsPermission: 0,
      	detailsUpdatePermission: 0,
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
      this.listLoading = true
      findPojo(this.listQuery).then((res) => {
      	 this.list = res.pojo.list
      	 this.total = res.pojo.count
      	 this.listLoading = false
      }).catch(error => {
          this.listLoading = false
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
    		this.buttonRole.detailsPermission= 1
    		this.buttonRole.detailsUpdatePermission=1
    	}else{
    		let buttonRoles = JSON.parse(sessionStorage.getItem("buttonRoles"))
    		let chargeMessage = buttonRoles.chargeMessage
    		if(chargeMessage.length>0){
    			for(let value of chargeMessage){
    				if(value=='rechargePermission'){
    					this.buttonRole.rechargePermission= 1
    				}else if(value=='refundPermission'){
    					this.buttonRole.refundPermission= 1
    				}else if(value=='detailsPermission'){
    					this.buttonRole.detailsPermission= 1
    				}else if(value=='detailsUpdatePermission'){
    					this.buttonRole.detailsUpdatePermission= 1
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

	/*  充值 开始   */ 
    //重置表单
	resetForm() {
		this.dataRechargeForm.webid = undefined
		this.dataRechargeForm.jqName = undefined
        this.dataRechargeForm.frNo = undefined
        this.dataRechargeForm.frName = undefined
        this.dataRechargeForm.qqYe = undefined
        this.dataRechargeForm.czje= undefined
    },
    openRecharge(row) {
      this.resetForm()
      this.dialogRechargeVisible = true
      
      this.dataRechargeForm.webid = row.webid
      this.dataRechargeForm.jqName = row.jqName
	  this.dataRechargeForm.frNo = row.frNo
	  this.dataRechargeForm.frName = row.frName
	  let qqYe = 0
	  if(row.qqYe){
	  	qqYe=row.qqYe/1000
	  }
	  this.dataRechargeForm.qqYe = qqYe
	  this.dataRechargeForm.czje= row.czje
    },
    requestRecharge() {
      this.$refs['dataRechargeForm'].validate((valid) => {
        if (valid) {
        	let param ={
        		id: this.dataRechargeForm.webid,
        		czje: this.dataRechargeForm.czje*1000
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
    /*  充值 结束   */ 
    
    /*  出狱退费 开始 */
    requestRefund(row){
    	let qqYe = 0
		if(row.qqYe){
		  qqYe=row.qqYe/1000
		}
    	this.$confirm('罪犯当前余额:'+qqYe+'元，是否继续“出狱退费”操作？（点击确定后，罪犯余额将会被清零，状态变成出狱）', '提示', {
		    type: 'warning'
		}).then(() => {
			let param = {
	    		id: row.webid
	    	}
			RequestRefund(param).then(() => {
				Message({
			        message: '退费成功',
				    type: 'success',
				    duration: 5 * 1000
			    });
	    		this.getList()
	        }).catch(error => {
		        
		    })
		})
    },
    /*  出狱退费 结束  */
   
    /* 充值明细 开始  */
    openDetails(row){
    	this.detailsListQuery.frNo = row.frNo
    	this.dialogDetailsVisible=true
    	this.getDetailsList()
    },
    getDetailsList() {
      this.detailsListLoading = true
      findDetailsPojo(this.detailsListQuery).then((res) => {
      	 this.detailsList = res.pojo.list
      	 this.detailsTotal = res.pojo.count
      	 this.detailsListLoading = false
      }).catch(error => {
          this.detailsListLoading = false
      })
    },
    handleDetailsSizeChange(val) {
      this.detailsListQuery.pageSize = val
      this.getDetailsList()
    },
    handleDetailsCurrentChange(val) {
      this.detailsListQuery.pageNum = val
      this.getDetailsList()
    },
    /* 充值明细 结束  */
   
    /* 修改充值 开始 */
    openDetailsUpdate(row){
    	this.dataDetailsUpdateForm.czId= row.czId
        this.dataDetailsUpdateForm.jqName= row.jqName
        this.dataDetailsUpdateForm.frNo= row.frNo
        this.dataDetailsUpdateForm.frName= row.frName
        let czje = 0
		if(row.czje){
		    czje=row.czje/1000
		}
        this.dataDetailsUpdateForm.czje= czje
    	this.dialogDetailsUpdateVisible=true
    },
    requestDetailsUpdate(){
    	let param ={
    		czId:this.dataDetailsUpdateForm.czId,
    		czje:this.dataDetailsUpdateForm.czje*1000
    	}
    	RequestDetailsUpdate(param).then(res =>{
    		Message({
		        message: res.errMsg,
			    type: 'success',
			    duration: 5 * 1000
		    });
		    this.getDetailsList()
		    this.dialogDetailsUpdateVisible=false
    	}).catch(error => {
		    this.dialogDetailsUpdateVisible=false 
		})
    	
    },
    /* 修改充值 结束 */
	dateFormats: function (val) {
		if(!val){
			return undefined
		}
		return moment(val).format("YYYY-MM-DD HH:mm:ss");
	},
  }
}
</script>
