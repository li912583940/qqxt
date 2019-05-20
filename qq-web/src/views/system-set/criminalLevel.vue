<!--
	作者：912583940@qq.com
	时间：2018-11-01
	描述： 罪犯级别
-->
<template>
  <div class="app-container">
  	<div class="filter-container">
      <el-button v-if="buttonRole.addPermission==1" class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-edit">{{$t('criminal.add')}}</el-button>
      <el-button v-if="buttonRole.resetPermission==1" class="filter-item" style="margin-left: 10px;" @click="ResetQqCount" type="info" icon="el-icon-setting">复位亲情电话次数</el-button>
      <!--<el-button class="filter-item" style="margin-left: 10px;" @click="OpenAddCountByJq" type="info" icon="el-icon-setting">按监区批量设置电话次数</el-button>-->
      <el-button v-if="buttonRole.addCountPermission==1" class="filter-item" style="margin-left: 10px;" @click="OpenAddCountByJb" type="info" icon="el-icon-setting">批量设置电话次数</el-button>
    </div>
    
    <el-table :key='tableKey' :data="list"   border fit highlight-current-row
      style="width: 1001px">
      <el-table-column width="200" align="center" label="级别编号" >
        <template slot-scope="scope">
          <span>{{scope.row.jbNo}}</span>
        </template>
      </el-table-column>
      <el-table-column width="200" align="center" label="级别名称" >
        <template slot-scope="scope">
          <span>{{scope.row.jbName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="200" align="center" label="每月亲情电话次数">
        <template slot-scope="scope">
          <span>{{scope.row.qqCount}}</span>
        </template>
      </el-table-column>
      <el-table-column width="200" align="center" label="每次亲情电话时长">
        <template slot-scope="scope">
          <span>{{scope.row.qqTime}}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="buttonRole.editPermission==1 || buttonRole.deletePermission==1" align="center" :label="$t('criminal.actions')" width="200">
        <template slot-scope="scope">
          <el-button v-if="buttonRole.editPermission==1" type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
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
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="660px" :modal-append-to-body="false">
      <el-form :rules="rules" :model="dataForm" ref="dataForm" label-position="right" label-width="180px" style='width: 400px; margin-left:10%;' >
         <el-form-item label="级别编号" prop="jbNo">
          <el-input v-if="dialogStatus=='update'" v-model="dataForm.jbNo" :disabled="true"></el-input>
          <el-input v-if="dialogStatus=='create'" v-model="dataForm.jbNo"></el-input>
        </el-form-item>
        <el-form-item label="级别名称" prop="jbName">
          <el-input v-model="dataForm.jbName"></el-input>
        </el-form-item>
        <el-form-item label="每月亲情电话次数" prop="qqCount">
          <el-input v-model="dataForm.qqCount"></el-input>
        </el-form-item>
        <el-form-item label="每次亲情电话时长(分钟)" prop="qqTime">
          <el-input v-model="dataForm.qqTime"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确 定</el-button>
        <el-button v-else type="primary" @click="updateData">确 定</el-button>
      </div>
    </el-dialog>
    
    <!-- 按监区批量设置电话次数 -->
	<el-dialog title="批量设置电话次数" :visible.sync="dialogJbVisible" width="740px" :modal-append-to-body="false">
		
		<el-card style="width: 540px; margin-left: 10%;">
			
			<el-transfer
			    filterable
			    :filter-method="transferFilter"
			    filter-placeholder="请输入关键字搜索"
			    v-model="jbValue"
			    :data="jbData"
			    :titles="['未拥有级别', '拥有级别']">
		    </el-transfer>
		    <div style="width: 400px; margin-top: 10px;">
				<span><strong>每月亲情电话次数:</strong></span>
				<el-input v-model="jbCount" style="width: 200px;margin-left: 10px;"></el-input>
			</div>
	  </el-card>
	  
	  <div slot="footer" class="dialog-footer">
	    <el-button @click="dialogJbVisible = false">取 消</el-button>
	    <el-button type="primary" @click="AddCountByJb">确 定</el-button>
	  </div>
	</el-dialog>
	
  </div>
</template>

<script>
import { findPojo, findList, findOne, RequestAdd, RequestEdit, RequestDelete, resetQqCount, qzResetQqCount, addCountByJb } from '@/api/criminalLevel'
	
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
      	jbName: undefined,
        pageNum: 1,
        pageSize: 20
      },
      // 新增或编辑弹窗
      dataForm: { 
        webid: undefined,
        jbNo: undefined,
        jbName: undefined,
        qqCount: undefined,
        qqTime: undefined,
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编 辑',
        create: '新 增'
      },
       rules: {
       	jbNo: [{ required: true, message: '级别编号不能为空', trigger: 'blur' }],
        jbName: [{ required: true, message: '级别名称不能为空', trigger: 'blur' }]
      },
      
      /*******  按级别批量设置电话次数   开始 ********/
      jbCount: null,
      dialogJbVisible: false, // 添加用户弹框
	  jbData: [],
	  jbValue: [],
	  /*******  按级别批量设置电话次数   结束  ********/
	 
	 
	  //按钮权限   1：有权限， 0：无权限
      buttonRole: { 
      	queryPermission: 1, 
      	addPermission: 0,
      	editPermission: 0,
      	deletePermission: 0,
      	resetPermission: 0,  // 复位亲情电话次数
      	addCountPermission: 0, // 批量设置电话次数
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
    		this.buttonRole.editPermission= 1
    		this.buttonRole.deletePermission= 1
    		this.buttonRole.resetPermission= 1
    		this.buttonRole.addCountPermission= 1
    	}else{
    		let buttonRoles = JSON.parse(sessionStorage.getItem("buttonRoles"))
    		let criminalLevel = buttonRoles.criminalLevel
    		if(criminalLevel.length>0){
    			for(let value of criminalLevel){
    				if(value=='addPermission'){
    					this.buttonRole.addPermission= 1
    				}else if(value=='editPermission'){
    					this.buttonRole.editPermission= 1
    				}else if(value=='deletePermission'){
    					this.buttonRole.deletePermission= 1
    				}else if(value=='resetPermission'){
    					this.buttonRole.resetPermission= 1
    				}else if(value=='addCountPermission'){
    					this.buttonRole.addCountPermission= 1
    				}
    			}
    		}
    	}
    },
    
    //重置表单
	resetForm(formName) {
		if(this.$refs[formName] !== undefined){
			this.$refs[formName].resetFields();
		}
		this.dataForm.webid = undefined
    },
    handleCreate() {
      this.dialogStatus = 'create'
      this.resetForm('dataForm')
      this.dialogFormVisible = true
//    this.$nextTick(() => {
//      this.$refs['dataForm'].clearValidate()
//    })
    },
    createData() {
    	if(this.dataForm.qqCount!=null && this.dataForm.qqCount<-1){
			Message({
		        message: '每月亲情电话次数的值不能小于-1',
			    type: 'error',
			    duration: 5 * 1000
		    });
			return false;
		}
    	if(this.dataForm.qqTime!=null && (this.dataForm.qqTime<-1 || this.dataForm.qqTime==0)){
			Message({
		        message: '每次亲情电话时长的值不能小于-1并且不能等于0',
			    type: 'error',
			    duration: 5 * 1000
		    });
			return false;
		}
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          RequestAdd(this.dataForm).then(() => {
            this.dialogFormVisible = false
            this.getList()
          }).catch(error => {
		        this.dialogFormVisible = false
		      })
        }
      })
    },
    handleUpdate(row) {
    	let param = {
    		id: row.webid
    	}
    	findOne(param).then((res) =>{
    		this.dataForm.webid = res.data.webid,
    		this.dataForm.jbNo = res.data.jbNo,
	        this.dataForm.jbName =  res.data.jbName,
	        this.dataForm.qqCount = res.data.qqCount,
	        this.dataForm.qqTime = res.data.qqTime
    	})
	    this.dialogStatus = 'update'
	    this.dialogFormVisible = true
	    this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
		if(this.dataForm.qqCount!=null && this.dataForm.qqCount<-1){
			Message({
		        message: '每月亲情电话次数的值不能小于-1',
			    type: 'error',
			    duration: 5 * 1000
		    });
			return false;
		}
    	if(this.dataForm.qqTime!=null && (this.dataForm.qqTime<-1 || this.dataForm.qqTime==0)){
			Message({
		        message: '每次亲情电话时长的值不能小于-1并且不能等于0',
			    type: 'error',
			    duration: 5 * 1000
		    });
			return false;
		}
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          RequestEdit(this.dataForm).then(() => {
            this.dialogFormVisible = false
            this.getList()
          }).catch(error => {
	        this.dialogFormVisible = false
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
    			id: row.webid
    		}
			RequestDelete(param).then(() => {
    			this.getList()
    		}).catch(error => {
	        	this.dialogFormVisible = false
	      	})
		})
	},

    //
    ResetQqCount(){
    	resetQqCount({}).then(res =>{
    		let result = res.data
    		if(result==0){
    			this.$confirm('是否强制复位?', '提示', {
					type: 'warning'
				}).then(() => {
					qzResetQqCount({}).then(res => {
						Message({
					        message: '强制复位成功',
						    type: 'success',
						    duration: 5 * 1000
					    });
			    	})
				})
    		}else{
    			Message({
			        message: '复位成功',
				      type: 'success',
				      duration: 5 * 1000
			    });
    		}
    	})
    },
    OpenAddCountByJb(){
    	this.jbValue = []
    	this.jbCount = null
    	this.dialogJbVisible =true
    	if(this.jbData.length ==0){ // 只查询一次
	 		//查询系统用户并设置到穿梭框中
		 	findList({}).then(res => {
		 		let list = res.list
		 		if(list == undefined){
		 			return false;
		 		}
		 		list.forEach((item, index) => {
		 			this.jbData.push({
		 				label: item.jbName,
		 				key:item.webid
		 			})
		 		})
		 	})
	 	}
    },
    AddCountByJb(){
    	if(this.jbValue.length==0){
    		Message({
		        message: '请至少选择一个级别',
			    type: 'error',
			    duration: 5 * 1000
		    });
    		return false;
    	}
    	if(this.jbCount==null || this.jbCount<-1){
    		Message({
		        message: '请设置每月亲情电话次数，并且值不能小于-1',
			    type: 'error',
			    duration: 5 * 1000
		    });
    		return false;
    	}
    	let jbs = this.jbValue.join()
		let param = {
			count: this.jbCount,
			jbs: jbs
		}
		addCountByJb(param).then(res => {
			this.getList()
			this.dialogJbVisible = false
		})
    },
    transferFilter(query, item){ //穿梭框搜索功能
	  	return item.label.indexOf(query) > -1;
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
