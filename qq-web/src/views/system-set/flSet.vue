<!--
	描述： 费率设置
-->
<template>
  <div class="app-container">
  	<div class="filter-container">
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-circle-plus-outline">{{$t('criminal.add')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="openFlUsable" type="info" icon="el-icon-setting">费率切换</el-button>
    </div>
    
    <el-table :key='tableKey' :data="list"   border fit highlight-current-row
      style="width: 951px">
      <el-table-column width="150" align="center" label="费率字冠" >
        <template slot-scope="scope">
          <span>{{scope.row.flFlag}}</span>
        </template>
      </el-table-column>
      <el-table-column width="150" align="center" label="费率类别" >
        <template slot-scope="scope">
          <span>{{scope.row.flMc}}</span>
        </template>
      </el-table-column>
      <el-table-column width="150" align="center" label="费率单位(秒)" >
        <template slot-scope="scope">
          <span>{{scope.row.flUnit}}</span>
        </template>
      </el-table-column>
      <el-table-column width="150" align="center" label="内部费率值(元)" >
        <template slot-scope="scope">
          <span>{{scope.row.flCountIn | qqYeFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column width="150" align="center" label="外部费率值(元)" >
        <template slot-scope="scope">
          <span>{{scope.row.flCountOut | qqYeFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('criminal.actions')" width="200">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button  size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

		<!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

	<!-- 新增或编辑 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="700px">
      <el-form :rules="rules" :model="dataForm" ref="dataForm" label-position="right" label-width="120px" style='width: 520px; margin-left:10%;' >
        <el-form-item label="费率字冠" prop="flFlag">
          <el-input v-model="dataForm.flFlag" style="width:200px"></el-input>
        </el-form-item>
        <el-form-item label="费率类别" prop="flMc">
          <el-input v-model="dataForm.flMc" style="width:200px"></el-input>
        </el-form-item>
        <el-form-item label="通话期计费" >
         	 <span>每</span>
         	 <el-input v-model="dataForm.flUnit" style="width:60px"></el-input>
         	 <span>秒</span>
         	 <el-input v-model="dataForm.flCountIn" style="width:60px"></el-input>
         	 <span>元,</span>
         	 <span v-if="sysParam && sysParam.paramData1=='1'">
         	 	<span>(外部</span>
         	 	<el-input v-model="dataForm.flCountOut" style="width:60px"></el-input>
         	 	<span>元)</span>
         	 </span>
        </el-form-item>
        <el-form-item label="通话前期计费" >
        	 <span>前</span>
	     	 <el-input v-model="dataForm.flBeginTime" style="width:60px"></el-input>
	     	 <span>秒,</span>
             <span>每</span>
	     	 <el-input v-model="dataForm.flBeginUnit" style="width:60px"></el-input>
	     	 <span>秒</span>
	     	 <el-input v-model="dataForm.flBeginCountIn" style="width:60px"></el-input>
	     	 <span>元,</span>
	     	 <span v-if="sysParam && sysParam.paramData1=='1'">
	     	 	<span>(外部</span>
	     	 	<el-input v-model="dataForm.flBeginCountOut" style="width:60px"></el-input>
	     	 	<span>元)</span>
	     	 </span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确 定</el-button>
        <el-button v-else type="primary" @click="updateData">确 定</el-button>
      </div>
    </el-dialog>
    
    <!-- 费率切换  开始 -->
    <el-dialog title="费率切换" :visible.sync="dialogFormFlVisible" width="600px">
      <el-form :model="dataFlForm" label-position="right" label-width="120px" style='width: 400px; margin-left:17%;' >
        <el-form-item label="对内对外费率" prop="paramData1">
          <el-radio-group v-model="dataFlForm.paramData1">
		    <el-radio :label="'0'">关闭</el-radio>
		    <el-radio :label="'1'">开启</el-radio>
		  </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormFlVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateFlData">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 费率切换  结束 -->
  </div>
</template>

<script>
import { findPojo, findOne, RequestAdd, RequestEdit, RequestDelete, findSysParam, RequestFlData} from '@/api/flSet'

import moment from 'moment';
import waves from '@/directive/waves' // 水波纹指令
import { Message, MessageBox } from 'element-ui'

export default {
  name: 'flSet',
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
      dataForm: { 
        webid: undefined,
        flFlag: undefined,
        flMc: undefined,
        flUnit: undefined,
        flCountIn: undefined,
        flCountOut: undefined,
        flBeginTime: undefined,
        flBeginUnit: undefined,
        flBeginCountIn: undefined,
        flBeginCountOut: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编 辑',
        create: '新 增'
      },
       rules: {
        qsGx: [{ required: true, message: '亲属关系不能为空', trigger: 'blur' }]
      },
      sysParam: null,
      
      /**** 费率切换  开始 ****/
      dialogFormFlVisible: false,
      dataFlForm:{
      	webid: undefined,
      	paramData1: '0',
      },
      /**** 费率切换  结束 ****/
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
    this.getSysParam()
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
    getSysParam(){
    	findSysParam({}).then(res =>{
    		this.sysParam = res.data
    		this.dataFlForm.webid = res.data.webid
    		this.dataFlForm.paramData1 = res.data.paramData1
    	})
    },
    //重置表单
	resetForm() {
		this.dataForm.webid = undefined
		this.dataForm.flFlag = undefined
    	this.dataForm.flMc = undefined
        this.dataForm.flUnit = undefined
        this.dataForm.flCountIn = undefined
        this.dataForm.flCountOut = undefined
        this.dataForm.flBeginTime = undefined
        this.dataForm.flBeginUnit = undefined
        this.dataForm.flBeginCountIn = undefined
        this.dataForm.flBeginCountOut = undefined
    },
    handleCreate() {
      this.dialogStatus = 'create'
      this.resetForm()
      this.dialogFormVisible = true
//    this.$nextTick(() => {
//      this.$refs['dataForm'].clearValidate()
//    })
    },
    createData() {
      if(this.dataForm.flFlag==undefined){
      	Message({
	        message: '费率字冠不能为空',
		    type: 'error',
		    duration: 5 * 1000
	    });
	    return false
      }
      if(this.dataForm.flCountIn==undefined || this.dataForm.flUnit==undefined){
      	Message({
	        message: '通话期计费不能为空',
		    type: 'error',
		    duration: 5 * 1000
	    });
	    return false
      }
      let param = Object.assign({},this.dataForm)
      param.flCountIn=this.dataForm.flCountIn!=undefined?this.dataForm.flCountIn*1000:60
      param.flCountOut=this.dataForm.flCountOut!=undefined?this.dataForm.flCountOut*1000:0
      param.flBeginCountIn=this.dataForm.flBeginCountIn!=undefined?this.dataForm.flBeginCountIn*1000:0
      param.flBeginCountOut=this.dataForm.flBeginCountOut!=undefined?this.dataForm.flBeginCountOut*1000:0
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          RequestAdd(param).then(res => {
            this.dialogFormVisible = false
            this.getList()
          }).catch(error => {
	        //this.dialogFormVisible = false
	      })
        }
      })
    },
    handleUpdate(row) {
    	let param = {
    		id: row.webid
    	}
    	findOne(param).then((res) =>{
    		this.dataForm.webid = res.data.webid
    		this.dataForm.flFlag = res.data.flFlag
        	this.dataForm.flMc = res.data.flMc
	        this.dataForm.flUnit = res.data.flUnit
	        this.dataForm.flCountIn = res.data.flCountIn/1000
	        this.dataForm.flCountOut = res.data.flCountOut/1000
	        this.dataForm.flBeginTime = res.data.flBeginTime
	        this.dataForm.flBeginUnit = res.data.flBeginUnit
	        this.dataForm.flBeginCountIn = res.data.flBeginCountIn/1000
	        this.dataForm.flBeginCountOut = res.data.flBeginCountOut/1000
    	})
	    this.dialogStatus = 'update'
	    this.dialogFormVisible = true
	    this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      if(this.dataForm.flFlag==undefined){
      	Message({
	        message: '费率字冠不能为空',
		    type: 'error',
		    duration: 5 * 1000
	    });
	    return false
      }
      if(this.dataForm.flCountIn==undefined || this.dataForm.flUnit==undefined){
      	Message({
	        message: '通话期计费不能为空',
		    type: 'error',
		    duration: 5 * 1000
	    });
	    return false
      }
      let param = Object.assign({},this.dataForm)
      param.flCountIn=this.dataForm.flCountIn!=undefined?this.dataForm.flCountIn*1000:60
      param.flCountOut=this.dataForm.flCountOut!=undefined?this.dataForm.flCountOut*1000:0
      param.flBeginCountIn=this.dataForm.flBeginCountIn!=undefined?this.dataForm.flBeginCountIn*1000:0
      param.flBeginCountOut=this.dataForm.flBeginCountOut!=undefined?this.dataForm.flBeginCountOut*1000:0
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          RequestEdit(param).then(res => {
      		this.dialogFormVisible = false
            this.getList()
          }).catch(error => {
	        //this.dialogFormVisible = false
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
    			id: row.id
    		}
			RequestDelete(param).then(() => {
    		this.getList()
    	}).catch(error => {
	        this.dialogFormVisible = false
	      })
		})
	},
	/**** 费率切换  开始 ****/
	openFlUsable(){
		this.dialogFormFlVisible = true
	},
	updateFlData(){
		let param ={
			webid: this.dataFlForm.webid,
    		paramData1: this.dataFlForm.paramData1
		}
		RequestFlData(param).then(res =>{
			Message({
		        message: res.errMsg,
			    type: 'success',
			    duration: 5 * 1000
		    });
		    this.getSysParam()
		    this.dialogFormFlVisible = false
		})
	},
	/**** 费率切换  结束 ****/
	dateFormats: function (val) {
		if(!val){
			return undefined
		}
		return moment(val).format("YYYY-MM-DD HH:mm:ss");
	},
  }
}
</script>
