<template>
  <div class="app-container">
    <div class="filter-container">
    	<el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入罪犯编号" v-model="listQuery.frNo" clearable>
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入罪犯姓名" v-model="listQuery.frName" clearable>
      </el-input>
      <el-select clearable style="width: 200px" class="filter-item" v-model="listQuery.jq" placeholder="选择监区">
        <el-option v-for="item in jqs" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-select clearable style="width: 200px" class="filter-item" v-model="listQuery.jbNo" placeholder="选择级别">
        <el-option v-for="item in jbNos" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入IC卡号" v-model="listQuery.frCard" clearable>
      </el-input>
      <el-select clearable style="width: 200px" class="filter-item" v-model="listQuery.state" placeholder="选择服刑状态">
        <el-option v-for="item in states" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入电话卡号" v-model="listQuery.qqZh" clearable>
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('criminal.search')}}</el-button>
      <el-button v-if="buttonRole.addPermission==1" class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-circle-plus-outline">{{$t('criminal.add')}}</el-button>
      <el-button v-if="buttonRole.exportPermission==1" class="filter-item" style="margin-left: 10px;" type="primary" v-waves icon="el-icon-download" @click="handleDownload">{{$t('criminal.export')}}</el-button>
    	<el-upload
    		v-if="buttonRole.importPermission==1"
    		ref="upload"
			  class="filter-item"
			  :action="excelPath"
			  :on-success="excelSuccess"
			  :on-error="excelError"
			  multiple>
			  <el-button icon="el-icon-upload2" style="margin-left: 10px;" type="primary">导入</el-button>
			</el-upload>
    </div>

    <el-table :key='tableKey' :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
      style="width: 1511px">
      <el-table-column width="100" align="center" :label="$t('currency.frNo')">
        <template slot-scope="scope">
          <span>{{scope.row.frNo}}</span>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" :label="$t('currency.frName')">
        <template slot-scope="scope">
          <span>{{scope.row.frName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="100" align="center" :label="$t('currency.jqName')">
        <template slot-scope="scope">
          <span>{{scope.row.jqName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" :label="$t('currency.frCard')">
        <template slot-scope="scope">
          <span>{{scope.row.frCard}}</span>
        </template>
      </el-table-column>
      <el-table-column width="90" align="center" label="余额">
        <template slot-scope="scope">
          <span>{{scope.row.qqYe | qqYeFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="拨打次数">
        <template slot-scope="scope">
          <span>{{scope.row.qqUse}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="剩余次数">
        <template slot-scope="scope">
          <span>{{scope.row.qqLeft}}</span>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" label="电话卡号/密码">
        <template slot-scope="scope">
          <span>{{scope.row.qqZh}} / {{scope.row.qqMm}}</span>
        </template>
      </el-table-column>
      <el-table-column width="100" align="center" label="服刑状态">
        <template slot-scope="scope">
          <span v-if="scope.row.state==0">服刑中</span>
          <span v-if="scope.row.state==1" style="color: red;">出狱</span>
        </template>
      </el-table-column>
      <el-table-column v-if="buttonRole.editPermission==1 || buttonRole.deletePermission==1" align="center" :label="$t('criminal.actions')" width="200" fixed="right">
        <template slot-scope="scope">
        	<div>
	          <el-button v-if="buttonRole.editPermission==1" type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(scope.row)">{{$t('criminal.edit')}}</el-button>
	          <el-button v-if="buttonRole.deletePermission==1" size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">{{$t('criminal.delete')}}</el-button>
          </div>
        </template>
      </el-table-column>
      <el-table-column v-if="buttonRole.queryQsPermission==1 || buttonRole.statePermission==1 || buttonRole.speciallyPermission==1" align="center" :label="$t('criminal.actions')" width="220" fixed="right">
        <template slot-scope="scope">
        	<div>
        		<el-button v-if="buttonRole.queryQsPermission==1" type="primary" size="mini" @click="handleQsManage(scope.row)">亲属</el-button>
	          <el-button v-if="buttonRole.statePermission==1" type="primary" size="mini" @click="openState(scope.row)">状态</el-button>
	        	<el-button v-if="buttonRole.speciallyPermission==1" type="primary" size="mini" @click="openSpecially(scope.row)">特批</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

		<!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 新增或编辑 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :rules="rules" :model="dataForm" ref="dataForm" label-position="right" label-width="100px" style='width: 400px; margin-left:25%;' >
        <el-form-item :label="$t('currency.frNo')" prop="frNo">
          <el-input v-if="dialogStatus=='create'" v-model="dataForm.frNo"></el-input>
          <el-input v-else v-model="dataForm.frNo" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item :label="$t('currency.frName')" prop="frName">
          <el-input v-model="dataForm.frName"></el-input>
        </el-form-item>
        <el-form-item label="IC卡号" prop="frCard">
          <el-input v-model="dataForm.frCard"></el-input>
        </el-form-item>
        <el-form-item :label="$t('currency.jqName')" prop="jq">
          <el-select class="filter-item" v-model="dataForm.jq" placeholder="请选择">
            <el-option v-for="item in  jqs" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="级别" prop="jbNo">
          <el-select class="filter-item" v-model="dataForm.jbNo" placeholder="请选择">
            <el-option v-for="item in  jbNos" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="重点监控" >
        	<el-radio-group v-model="dataForm.monitorFlag">
				    <el-radio :label="'0'">否</el-radio>
				    <el-radio :label="'1'">是</el-radio>
				  </el-radio-group>
        </el-form-item>
        <el-form-item label="亲情级别" >
        	<el-radio-group v-model="dataForm.qqJb">
				    <el-radio :label="0">禁止</el-radio>
				    <el-radio :label="1">正常</el-radio>
				  </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确 定</el-button>
        <el-button v-else type="primary" @click="updateData">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 亲属弹框  -->
    <el-dialog :title="qs_frname" :visible.sync="dialogQsVisible" width="1381px">
    	<div class="filter-container">
	      <el-button v-if="buttonRole.addQsPermission==1" class="filter-item" style="margin-left: 10px;" @click="handleQsCreate" type="primary" icon="el-icon-circle-plus-outline">{{$t('criminal.add')}}</el-button>
	    </div>
      <el-table :key='qsTableKey' :data="qsList" v-loading="qsListLoading" element-loading-text="给我一点时间" border fit highlight-current-row
	      style="width: 1281px;margin-left: 10px;">
	      <el-table-column width="110" align="center" label="亲属姓名">
	        <template slot-scope="scope">
	          <span>{{scope.row.qsName}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="180" align="center" label="亲属身份证">
	        <template slot-scope="scope">
	          <span>{{scope.row.qsSfz}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="160" align="center" label="IC卡号">
	        <template slot-scope="scope">
	          <span>{{scope.row.qsCard}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="90" align="center" label="关系">
	        <template slot-scope="scope">
	          <span>{{scope.row.gx}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="90" align="center" label="性别">
	        <template slot-scope="scope">
	          <span>{{scope.row.xb}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="300" align="center" label="地址">
	        <template slot-scope="scope">
	          <span>{{scope.row.dz}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="140" align="center" label="电话号码">
	        <template slot-scope="scope">
	          <span>{{scope.row.tele}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="140" align="center" label="缩位号码">
	        <template slot-scope="scope">
	          <span>{{scope.row.sw}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column v-if="buttonRole.editQsPermission==1 || buttonRole.deleteQsPermission==1" align="center" :label="$t('criminal.actions')" width="200"  fixed="right">
	        <template slot-scope="scope">
	          <el-button v-if="buttonRole.editQsPermission==1" type="primary" size="mini" icon="el-icon-edit" @click="handleQsUpdate(scope.row)">编辑</el-button>
	          <el-button v-if="buttonRole.deleteQsPermission==1" size="mini" type="danger" icon="el-icon-delete" @click="handleQsDelete(scope.row)">删除</el-button>
	        </template>
	      </el-table-column>
	    </el-table>
	    <!-- 分页 -->
	    <div class="pagination-container">
	      <el-pagination background @size-change="handleQsSizeChange" @current-change="handleQsCurrentChange" :current-page="qsListQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="qsListQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="qsTotal">
	      </el-pagination>
	    </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogQsVisible = false">关闭</el-button>
      </span>
    </el-dialog>

		<!-- 亲属新增或编辑 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogQsFormVisible">
      <el-form :rules="rulesQs" :model="dataQsForm" ref="dataQsForm" label-position="right" label-width="120px" style='width: 400px; margin-left:25%;' >
        <el-form-item label="证件号码" prop="qsSfz">
          <el-input v-model="dataQsForm.qsSfz"></el-input>
        </el-form-item>
        <el-form-item label="亲属姓名" prop="qsName">
          <el-input v-model="dataQsForm.qsName"></el-input>
        </el-form-item>
        <el-form-item label="关系" prop="gx">
          <el-select class="filter-item" v-model="dataQsForm.gx" placeholder="请选择">
            <el-option v-for="item in gxs" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="IC卡号" prop="qsCard">
          <el-input v-model="dataQsForm.qsCard"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="dz">
          <el-input v-model="dataQsForm.dz"></el-input>
        </el-form-item>
        <el-form-item label="性别" >
        	<el-radio-group v-model="dataQsForm.xb">
        		<el-radio :label="'男'">男</el-radio>
				    <el-radio :label="'女'">女</el-radio>
				  </el-radio-group>
        </el-form-item>
        <el-form-item label="电话号码" prop="tele">
          <el-input v-model="dataQsForm.tele"></el-input>
        </el-form-item>
        <el-form-item label="缩位号码" prop="sw">
          <el-select class="filter-item" v-model="dataQsForm.sw" placeholder="请选择">
            <el-option v-for="item in sws" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogQsFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createQsData">确 定</el-button>
        <el-button v-else type="primary" @click="updateQsData">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { findPojo, findOne, RequestAdd, RequestEdit, RequestDelete, exportExcel, findJbList, findQsPojo, findQsOne, RequestQsAdd, RequestQsEdit, RequestQsDelete, findGxList, findSwList } from '@/api/criminal'
import { findList as findJqList} from '@/api/jqSet'

import moment from 'moment';
import waves from '@/directive/waves' // 水波纹指令
import { getToken } from '@/utils/auth'
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
      listLoading: true,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        frNo: undefined,
        frName: undefined,
        jq: undefined,
        jbNo: undefined,
        frCard: undefined,
        state: undefined,
        qqZh: undefined,
      },
      
      // 新增或编辑弹窗
      dataForm: { 
        webid: undefined,
        frName: undefined,
        frNo: undefined,
        frCard: undefined,
        jq: undefined,
        jbNo: undefined,
        monitorFlag: '0',
        qqJb: 1,
      },
      jqs: [ // 监区下拉选框
      
      ],
      jbNos: [ //犯人级别下拉框
      
      ],
      states: [
      	{
      		id: 0,
      		name: '服刑中'
      	},
      	{
      		id: 1,
      		name: '已出狱'
      	}
      ],
 
      excelPath: process.env.BASE_API+"/jlFr/importExcel", //罪犯excel导入地址
      
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编 辑',
        create: '新 增'
      },
      rules: {
      	frNo: [{ required: true, message: '编号不能为空', trigger: 'blur'}],
        frName: [{ required: true, message: this.$t('criminal.name'), trigger: 'blur' }],
        jbNo: [{ required: true, message: '级别不能为空', trigger: 'change' }],
        jq: [{ required: true, message: '监区不能为空', trigger: 'change' }]
      },
      
      // 亲属
      qs_frname: undefined, // 亲属弹框左上角显示犯人姓名
      dialogQsFormVisible: false,
      dialogQsVisible: false,
      qsTableKey: 0,
      qsList: null,
      qsTotal: null,
      qsListLoading: true,
      qsListQuery: {
        pageNum: 1,
        pageSize: 10,
        frNo: undefined
      },
      // 新增或编辑弹窗
      dataQsForm: { 
        webid: undefined,
        frNo: undefined,
        qsSfz: undefined,
        qsName: undefined,
        gx: undefined,
        qsCard: undefined,
        dz: undefined,
        xb: '男',
        tele: undefined,
        sw: undefined,
      },
      gxs: [ // 关系
      	
      ],
      sws: [], // 缩位号码
      rulesQs:{
        qsName: [{ required: true, message: '亲属姓名不能为空', trigger: 'blur' }],
        gx: [{ required: true, message: '亲属关系必选', trigger: 'blur' }],
        qsSfz: [{ required: true, message: '亲属身份证不能为空', trigger: 'blur' }]
      },
      
      //按钮权限   1：有权限， 0：无权限
      buttonRole: { 
      	queryPermission: 1, 
      	addPermission: 0,
      	editPermission: 0,
      	deletePermission: 0,
      	exportPermission: 0,
      	importPermission: 0,
      	statePermission: 0,
      	speciallyPermission: 0,
      	//亲属相关的
      	queryQsPermission: 0,
      	addQsPermission: 0,
      	editQsPermission: 0,
      	deleteQsPermission: 0
      }
      
    }
  },
  filters: {
    qqYeFormat(data){
			if(data == undefined){
				return '0';
			}
			return data/1000;
		},
  },
  created() {
    //this.getList()
    this.noSearch()
  	
    this.getJqList()
    this.getJbList()
  },
  mounted() {
     this.setButtonRole()
  },
  methods: {
  	noSearch() {
  		this.listLoading = false
  		this.total=0
  	},
    getList() { // 犯人列表
      this.listLoading = true
      findPojo(this.listQuery).then((res) => {
      	 this.list = res.pojo.list
      	 this.total = res.pojo.count
      	 this.listLoading = false
      }).catch(error => {
          this.listLoading = false
      })
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
    getJbList() { //犯人级别下拉框
    	if(this.jbNos.length === 0) {
    		findJbList({}).then((res) => {
	    		let list = res.list
	    		for(let x of list){
					  let value = {}
					  value.id = x.jbNo
					  value.name = x.jbName
					  this.jbNos.push(value)
					}
	    	})
    	}
    },
    setButtonRole() { //设置按钮的权限
    	let roles = sessionStorage.getItem("roles")
    	if(roles.includes('admin')){
    		this.buttonRole.addPermission= 1
    		this.buttonRole.editPermission= 1
    		this.buttonRole.deletePermission= 1
    		this.buttonRole.exportPermission= 1
    		this.buttonRole.importPermission= 1
    		this.buttonRole.statePermission= 1
      	this.buttonRole.speciallyPermission= 1
      	
    		this.buttonRole.queryQsPermission= 1
    		this.buttonRole.addQsPermission= 1
    		this.buttonRole.editQsPermission= 1
    		this.buttonRole.deleteQsPermission= 1
    	}else{
    		let buttonRoles = JSON.parse(sessionStorage.getItem("buttonRoles"))
    		let criminal = buttonRoles.criminal
    		if(criminal.length>0){
    			for(let value of criminal){
    				if(value=='addPermission'){
    					this.buttonRole.addPermission= 1
    				}else if(value=='editPermission'){
    					this.buttonRole.editPermission= 1
    				}else if(value=='deletePermission'){
    					this.buttonRole.deletePermission= 1
    				}else if(value=='exportPermission'){
    					this.buttonRole.exportPermission= 1
    				}else if(value=='importPermission'){
    					this.buttonRole.importPermission= 1
    				}else if(value=='statePermission'){
    					this.buttonRole.statePermission= 1
    				}else if(value=='speciallyPermission'){
    					this.buttonRole.speciallyPermission= 1
    				}
    				else if(value=='queryQsPermission'){
    					this.buttonRole.queryQsPermission= 1
    				}else if(value=='addQsPermission'){
    					this.buttonRole.addQsPermission= 1
    					this.buttonRole.queryQsPermission= 1
    				}else if(value=='editQsPermission'){
    					this.buttonRole.editQsPermission= 1
    					this.buttonRole.queryQsPermission= 1
    				}else if(value=='deleteQsPermission'){
    					this.buttonRole.deleteQsPermission= 1
    					this.buttonRole.queryQsPermission= 1
    				}
    			}
    		}
    	}
    	
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
    excelSuccess() {
    	this.$refs.upload.clearFiles()
    	Message({
        message: '文件已上传至服务器，请等待十秒钟左右查询信息。',
	      type: 'success',
	      duration: 5 * 1000
      });
      
    },
    excelError() {
    	this.$refs.upload.clearFiles()
    	Message({
        message: '文件上传失败，请检查文件是否为符合要求的EXCEL表格！',
	      type: 'error',
	      duration: 5 * 1000
      });
    },
    //重置表单
		resetForm(formName) {
//			if(this.$refs[formName] !== undefined){
//				this.$refs[formName].resetFields();
//			}
			this.dataForm.webid= undefined
	    this.dataForm.frName= undefined
	    this.dataForm.frNo= ''
	    this.dataForm.frCard= undefined
	    this.dataForm.jq= this.jqs.length === 0?undefined:this.jqs[0].id
	    this.dataForm.jbNo= this.jbNos.length === 0?undefined:this.jbNos[0].id
	    this.dataForm.monitorFlag= '0'
	    this.dataForm.qqJb= 1
	  },
    handleCreate() {
      this.dialogStatus = 'create'
      this.resetForm('dataForm')
      this.dialogFormVisible = true
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          RequestAdd(this.dataForm).then((res) => {
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
        this.dataForm.frName =  res.data.frName
        this.dataForm.frNo = res.data.frNo
        this.dataForm.frCard = res.data.frCard
        this.dataForm.jq = res.data.jq
        this.dataForm.jbNo = res.data.jbNo
        this.dataForm.monitorFlag = res.data.monitorFlag
        this.dataForm.qqJb = res.data.qqJb
    	})
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          RequestEdit(this.dataForm).then(res => {
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
	    		id: row.webid
	    	}
				RequestDelete(param).then(() => {
	    		this.getList()
	    	}).catch(error => {
	      })
			})
		},
    handleDownload() {
   		Message({
        message: '已准备导出罪犯信息文件，请稍等几秒。',
	      type: 'success',
	      duration: 5 * 1000
      });
      
			exportExcel(this.listQuery).then(res => {
	      var blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8' })
	     	if (window.navigator && window.navigator.msSaveOrOpenBlob) { // IE浏览器
        	window.navigator.msSaveOrOpenBlob(blob, '罪犯信息.xls');
    		}else{ //非IE浏览器
    			var downloadElement = document.createElement('a')
		     	var href = window.URL.createObjectURL(blob)
		     	downloadElement.href = href
		     	downloadElement.download = '罪犯信息.xls'
		     	document.body.appendChild(downloadElement)
		     	downloadElement.click()
	     		document.body.removeChild(downloadElement) // 下载完成移除元素
		     	window.URL.revokeObjectURL(href) // 释放掉blob对象
    		}
	     	
			}).catch(error => {
         console.log(error)
      })

    },
    
    
    // 亲属方法开始
    getQsList() { 
    	this.qsListLoading = true
      findQsPojo(this.qsListQuery).then((res) => {
      	 this.qsList = res.pojo.list
      	 this.qsTotal = res.pojo.count
      	 this.qsListLoading = false
      }).catch(error => {
         this.qsListLoading = false
      })
    },
    getGxList() {
    	if(this.gxs.length === 0) {
    		findGxList({}).then((res) => {
	    		let list = res.list
	    		for(let x of list){
					  let value = {}
					  value.id = x.qsGx
					  value.name = x.qsGx
					  this.gxs.push(value)
					}
	    	})
    	}
    },
    getSwList(id) {
    	this.sws = []
    	let param ={
    		frNo: this.qsListQuery.frNo,
    		id: id
    	}
    	findSwList(param).then(res =>{
    		let list = res.swList
    		for(let x of list){
    			let value = {}
    			value.id = x
    			value.name = x
    			this.sws.push(value)
    		}
    	})
    },
    handleQsManage(row) { //打开亲属弹框
    	this.qsListQuery.frNo = row.frNo
    	this.qs_frname = row.frName
			this.dialogQsVisible = true
			this.getQsList()
			this.getGxList()
		},
		handleQsSizeChange(val) {
      this.qsListQuery.pageSize = val
      this.getQsList()
    },
    handleQsCurrentChange(val) {
      this.qsListQuery.pageNum = val
      this.getQsList()
    },
    //重置表单
		resetQsForm(formName) {
			if(this.$refs[formName] !== undefined){
				this.$refs[formName].resetFields();
			}
			this.dataForm.webid= undefined
			this.dataQsForm.webid= undefined
	  },
    handleQsCreate() {
      this.dialogStatus = 'create'
      this.resetQsForm('dataQsForm')
      this.dialogQsFormVisible = true
      this.getSwList(undefined)
    },
    createQsData() {
      this.$refs['dataQsForm'].validate((valid) => {
        if (valid) {
        	this.dataQsForm.frNo = this.qsListQuery.frNo
          RequestQsAdd(this.dataQsForm).then(() => {
            this.dialogQsFormVisible = false
            this.getQsList()
          }).catch(error => {
		        this.dialogQsFormVisible = false
		      })
        }
      })
    },
    handleQsUpdate(row) {
    	let param = {
    		id: row.webid
    	}
    	findQsOne(param).then((res) =>{
    		this.dataQsForm.webid = res.data.webid
        this.dataQsForm.frNo = this.qsListQuery.frNo
        this.dataQsForm.qsSfz = res.data.qsSfz
        this.dataQsForm.qsName = res.data.qsName
        this.dataQsForm.gx = res.data.gx
        this.dataQsForm.qsCard = res.data.qsCard
        this.dataQsForm.dz = res.data.dz
        this.dataQsForm.xb = res.data.xb
        this.dataQsForm.tele = res.data.tele
        this.dataQsForm.sw = res.data.sw
    	})
      this.dialogStatus = 'update'
      this.dialogQsFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataQsForm'].clearValidate()
      })
      
      this.getSwList(row.webid)
      
    },
    updateQsData() {
      this.$refs['dataQsForm'].validate((valid) => {
        if (valid) {
          RequestQsEdit(this.dataQsForm).then(() => {
            this.dialogQsFormVisible = false
            this.getQsList()
          }).catch(error => {
		        this.dialogQsFormVisible = false
		      })
        }
      })
    },
    //删除
		handleQsDelete(row) {
			this.$confirm('确认删除该记录吗?', '提示', {
				type: 'warning'
			}).then(() => {
				this.qsListLoading = true;
				let param = {
	    		id: row.webid
	    	}
				RequestQsDelete(param).then(() => {
	    		this.getQsList()
	    	}).catch(error => {
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
