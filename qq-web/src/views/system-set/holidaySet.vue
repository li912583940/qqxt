<!--
	描述： 亲情节假日
-->
<template>
  <div class="app-container">
  	<div class="filter-container">
      <el-button v-if="buttonRole.addPermission==1" class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-circle-plus-outline">{{$t('currency.add')}}</el-button>
      <el-button v-if="buttonRole.deleteAllPermission==1" class="filter-item" style="margin-left: 10px;" @click="emptyDate" type="danger" icon="el-icon-delete">清空日期</el-button>
  	</div>
    
    <el-table :key='tableKey' :data="list"   border fit highlight-current-row
      style="width: 401px">
      <el-table-column width="200" align="center" label="日期" >
        <template slot-scope="scope">
          <span>{{scope.row.holidayDate}}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="buttonRole.deletePermission==1" align="center" :label="$t('criminal.actions')" width="200">
        <template slot-scope="scope">
          <el-button v-if="buttonRole.deletePermission==1" size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

		<!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

	<!-- 新增或编辑 -->
    <el-dialog title="新 增" :visible.sync="dialogFormVisible" width="600px" :modal-append-to-body="false">
      <el-form label-position="right" label-width="120px" style='width: 400px; margin-left:10%;' >
        <el-form-item label="日期">
          <el-date-picker
			  width="560px"
		      type="dates"
		      v-model="holidayValue"
		      placeholder="选择一个或多个日期"
		      value-format="yyyy-MM-dd">
	      </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="createData">确 定</el-button>
      </div>
    </el-dialog>
    
  </div>
</template>

<script>
import { findPojo, findOne, RequestAdd, RequestEdit, RequestDelete, EmptyDate} from '@/api/holidaySet'

import moment from 'moment';
import waves from '@/directive/waves' // 水波纹指令
import { Message, MessageBox } from 'element-ui'

export default {
  name: 'criminal',
  directives: {
    waves
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: null,
      listQuery: {
        pageNum: 1,
        pageSize: 10
      },
      // 新增或编辑弹窗
      holidayValue: [],
      dialogFormVisible: false,
	  
	  //按钮权限   1：有权限， 0：无权限
      buttonRole: { 
      	queryPermission: 1, 
      	addPermission: 0,
      	deletePermission: 0,
      	deleteAllPermission: 0,
      },
    }
  },
  filters: {
    
  },
  created() {
    this.getList()
  },
  mounted() {
    this.setButtonRole()
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
    		this.buttonRole.addPermission= 1
    		this.buttonRole.deletePermission= 1
    		this.buttonRole.deleteAllPermission= 1
    	}else{
    		let buttonRoles = JSON.parse(sessionStorage.getItem("buttonRoles"))
    		let holidaySet = buttonRoles.holidaySet
    		if(holidaySet.length>0){
    			for(let value of holidaySet){
    				if(value=='addPermission'){
    					this.buttonRole.addPermission= 1
    				}else if(value=='deletePermission'){
    					this.buttonRole.deletePermission= 1
    				}else if(value=='deleteAllPermission'){
    					this.buttonRole.deleteAllPermission= 1
    				}
    			}
    		}
    	}
    },
    
    handleCreate() {
      this.holidayValue =[]
      this.dialogFormVisible = true
    },
    createData() {
      if(this.holidayValue.length==0){
      	Message({
	        message: '请选择日期',
		    type: 'error',
		    duration: 5 * 1000
	    });
	    return false;
      }
      let holidays = this.holidayValue.join()
      let param = {
      	holidays: holidays
      }
      RequestAdd(param).then(res => {
        this.dialogFormVisible = false
        this.getList()
      }).catch(error => {
        //this.dialogFormVisible = false
      })
    },
    //删除
	handleDelete(row) {
		this.$confirm('确认删除该记录吗?', '提示', {
			type: 'warning'
		}).then(() => {
			this.listLoading = true;
			let param = {
    			id: row.webid
    		}
			RequestDelete(param).then(() => {
    		this.getList()
    	}).catch(error => {
	        this.dialogFormVisible = false
	      })
		})
	},
   
    emptyDate() {
    	this.$confirm('确认清空所有日期吗?', '提示', {
			type: 'warning'
		}).then(() => {
			EmptyDate({}).then(res =>{
				this.getList()
				Message({
			        message: '操作成功',
				      type: 'success',
				      duration: 5 * 1000
			    });
			}).catch(error =>{
				Message({
			        message: '操作失败',
				      type: 'error',
				      duration: 5 * 1000
			    });
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
