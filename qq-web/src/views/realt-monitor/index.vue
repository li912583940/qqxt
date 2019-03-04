<template>
  <div class="app-container">
    <el-table :key='tableKey' :data="list" v-loading="listLoading" element-loading-text="给我一点时间"  border fit highlight-current-row
      style="width: 1461px">
      <el-table-column width="110" align="center" label="线路逻辑编号">
        <template slot-scope="scope">
          <span>{{scope.row.lineNo}}</span>
        </template>
      </el-table-column>
      <el-table-column width="90" align="center" :label="$t('currency.jqName')">
        <template slot-scope="scope">
          <span>{{scope.row.jqName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="100" align="center" label="状态">
        <template slot-scope="scope">
          <span v-if="scope.row.monitorState =='通话'">通话</span>
          <span v-if="scope.row.monitorState =='空闲'">空闲</span>
          <span v-if="scope.row.monitorState =='应答'">应答</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="电话号码">
        <template slot-scope="scope">
        	<span v-if="scope.row.monitorFlag == 1" style="color: red;">{{scope.row.monitorTele}}</span>
        	<span v-if="scope.row.monitorFlag != 1">{{scope.row.monitorTele}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="类型">
        <template slot-scope="scope">
        	<span v-if="scope.row.monitorFlag == 1" style="color: red;">{{scope.row.monitorType}}</span>
        	<span v-if="scope.row.monitorFlag != 1">{{scope.row.monitorType}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" :label="$t('currency.frName')">
        <template slot-scope="scope">
          <span v-if="scope.row.monitorFlag == 1" style="color: red;">{{scope.row.monitorFr}}</span>
        	<span v-if="scope.row.monitorFlag != 1">{{scope.row.monitorFr}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="亲属姓名">
        <template slot-scope="scope">
          <span v-if="scope.row.monitorFlag == 1" style="color: red;">{{scope.row.monitorQs}}</span>
        	<span v-if="scope.row.monitorFlag != 1">{{scope.row.monitorQs}}</span>
        </template>
      </el-table-column>
      <el-table-column width="100" align="center" label="关系">
        <template slot-scope="scope">
          <span v-if="scope.row.monitorFlag == 1" style="color: red;">{{scope.row.monitorGx}}</span>
        	<span v-if="scope.row.monitorFlag != 1">{{scope.row.monitorGx}}</span>
        </template>
      </el-table-column>
      <el-table-column width="100" align="center" label="线路号码">
        <template slot-scope="scope">
          <span>{{scope.row.localTele}}</span>
        </template>
      </el-table-column>
      <el-table-column width="100" align="center" label="剩余时间">
        <template slot-scope="scope">
          <span>{{scope.row.monitorTime}}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="buttonRole.jiantingPermission==1 || buttonRole.qieduanPermission==1 || buttonRole.chahuaPermission==1" align="center" :label="$t('criminal.actions')" width="300" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="jtState==1 && buttonRole.jiantingPermission==1" type="primary" size="mini" icon="el-icon-service" @click="jianting(scope.row)">监听</el-button>
          <el-button v-if="jtState==2 && buttonRole.jiantingPermission==1" size="mini" type="info" icon="el-icon-phone" @click="jiantingStop(scope.row)">停止监听</el-button>
          <el-button v-if="jtState==2 && buttonRole.qieduanPermission==1" size="mini" type="danger" icon="el-icon-phone" @click="qieduan(scope.row)">切断</el-button>
          <el-button v-if="buttonRole.chahuaPermission==1" type="primary" size="mini" icon="el-icon-phone-outline" @click="chahua(scope.row)">插话</el-button>
        </template>
      </el-table-column>
      <el-table-column v-if=" buttonRole.zhushiPermission==1" align="center" label="功能" width="120" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="buttonRole.zhushiPermission==1" size="mini" type="info" icon="el-icon-document" @click="zhushi(scope.row)">注释</el-button>
        </template>
      </el-table-column>
    </el-table>

		<!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

	  <el-dialog title="插话" :visible.sync="dialogCHVisible">
      <el-form :rules="rulesCH" :model="dataFormCH" ref="dataFormCH" label-position="right" label-width="120px" style='width: 400px; margin-left:25%;' >
        <el-form-item label="姓名" >
          <el-input v-model="dataFormCH.frName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="插话" prop="vocId" >
        	<el-select class="filter-item" v-model="dataFormCH.vocId" placeholder="请选择">
	            <el-option v-for="item in monitorVocList" :key="item.vocId" :label="item.vocInfo" :value="item.vocId"></el-option>
	        </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCHVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateCH">确 定</el-button>
      </div>
    </el-dialog>
	
	  <el-dialog title="注释" :visible.sync="dialogZSVisible">
      <el-form :model="dataFormZS" ref="dataFormZS" label-position="right" label-width="120px" style='width: 400px; margin-left:25%;' >
        <el-form-item label="姓名" >
          <el-input v-model="dataFormZS.frName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="注释" >
          <el-input v-model="dataFormZS.writeTxt"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogZSVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateZS">确 定</el-button>
      </div>
    </el-dialog>
    
  </div>
</template>

<script>
	
import { findPojo, GetMonitorVocList, AddMonitorFlag, GetZs} from '@/api/realtMonitor'

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
      listLoading: true,
      listQuery: {
        pageNum: 1,
        pageSize: 20
      },
      
      jtState: 1, //监听状态
      
      /** 插话 开始 */
      // 插话
      monitorVocList: [],
      dialogCHVisible: false,
      
      dataFormCH: {
      	frName: undefined,
      	serverName: undefined,
      	lineNo:undefined,
      	vocId: undefined,
      },
      rulesCH: {
        vocId: [{ required: true, message: '请选择一条内容', trigger: 'blur' }]
      },
      /** 插话 结束 */
     
      /** 注释开始 */
      dialogZSVisible: false, 
      dataFormZS: {
      	monitorCallid: undefined,
      	frName: undefined,
      	writeTxt: undefined,
      	
      },
      /** 注释结束 */
     
     //按钮权限   1：有权限， 0：无权限
      buttonRole: { 
      	queryPermission: 1,      //查询
      	jiantingPermission: 0,   //监听
      	qieduanPermission: 0,    //切断
      	chahuaPermission: 0,     //插话
      	zhushiPermission: 0      //注释
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
  	
  	this.getMonitorVocList()
  	
  	if(this.timer){
  		this.clearInterval(this.timer)
  	}else{
  		this.timer = setInterval(() =>{
  			this.getList()
  		}, 10000)
  	}
  },
  destroyed() {
  	clearInterval(this.timer)
  },
  methods: {
    getList() {
    	this.listLoading = true
      findPojo(this.listQuery).then((res) => {
      	 this.list = res.pojo.list
      	 this.total = res.pojo.count
      })
      this.listLoading = false
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
    		this.buttonRole.jiantingPermission= 1
    		this.buttonRole.qieduanPermission= 1
    		this.buttonRole.chahuaPermission= 1
    		this.buttonRole.zhushiPermission= 1
    	}else{
    		let buttonRoles = JSON.parse(sessionStorage.getItem("buttonRoles"))
    		let realtMonitor = buttonRoles.realtMonitor
    		if(realtMonitor.length>0){
    			for(let value of realtMonitor){
    				if(value=='jiantingPermission'){
    					this.buttonRole.jiantingPermission= 1
    				}else if(value=='qieduanPermission'){
    					this.buttonRole.qieduanPermission= 1
    				}else if(value=='chahuaPermission'){
    					this.buttonRole.chahuaPermission= 1
    				}else if(value=='zhushiPermission'){
    					this.buttonRole.zhushiPermission= 1
    				}
    			}
    		}
    	}
    },
    /** 监听 开始 */
    jianting(row){
    	if(row.monitorState=='通话'){
    		document.getElementById(row.jy).ListenTele(row.lineNo);
    		this.jtState = 2
    	}else{
    		Message({
	        message: '当前线路不在通话状态',
		      type: 'error',
		      duration: 5 * 1000
	      });
    	}
    },
    /** 监听 结束 */
   
    /** 停止监听 开始 */
    jiantingStop(row){
    	document.getElementById(row.jy).ListenStop(row.lineNo);
    	this.jtState = 1
    	Message({
        message: '停止监听',
	      type: 'success',
	      duration: 5 * 1000
      });
    },
    /** 停止监听 结束 */
    
    /** 切断 开始 */
    qieduan(row){
    	this.$confirm('是否要继续切断通话？切断后，该通话将不可恢复，请确认！', '提示', {
				type: 'warning'
			}).then(() => {
				document.getElementById(row.jy).StopTele(row.lineNo);
		    Message({
	        message: '已成功切断当前通话',
		      type: 'success',
		      duration: 5 * 1000
		    });
			}).catch(error =>{
				Message({
	        message: '系统原因，切断当前通话失败',
		      type: 'error',
		      duration: 5 * 1000
	      });
			})
    },
    /** 切断 结束 */
  
  
		/** 用于插话  开始 */
		getMonitorVocList(){ // 获取插话内容
			GetMonitorVocList({}).then(res => {
				this.monitorVocList = res.list
			})
		},
		//重置表单
		resetFormCH() {
			if(this.$refs['dataFormCH'] !== undefined){
				this.$refs['dataFormCH'].resetFields();
			}
			this.dataFormCH.serverName = undefined
	      	this.dataFormCH.lineNo = undefined
	    },
		chahua(row){
			this.resetFormCH()
			this.dialogCHVisible = true
			this.dataFormCH.frName = row.monitorFr
			this.dataFormCH.serverName = row.jy
	      	this.dataFormCH.lineNo = row.lineNo
		},
		updateCH(){
			var serverName = this.dataFormCH.serverName
			console.log(serverName)
			var lineNo = this.dataFormCH.lineNo 
			var vocId = this.dataFormCH.vocId
			
			this.$refs['dataFormCH'].validate((valid) => {
		        if (valid) {
		           document.getElementById(serverName).InsertVoc(lineNo,vocId);
		        }
		    })
			
			this.dialogCHVisible = false
		},
		/** 用于插话  结束 */
		
		/** 注释 开始 */
		//重置表单
		resetFormZS() {
			this.dataFormZS.monitorCallid = undefined
			this.dataFormZS.frName = undefined
			this.dataFormZS.writeTxt = undefined
	      	
	  },
    getZs(monitorCallid){ //获取注释
    	GetZs({monitorCallid: monitorCallid}).then(res => {
    		this.dataFormZS.writeTxt = res.data.writeTxt
    	})
    },
		zhushi(row){
			if(!row.monitorCallid){
				Message({
	          message: '当前线路未处于通话状态，无法添加注释',
	          type: 'success',
	          duration: 5 * 1000
	        });
	        return false;
			}
			this.resetFormZS()
			this.dialogZSVisible = true
			this.dataFormZS.monitorCallid = row.monitorCallid
			this.dataFormZS.frName = row.monitorFr
			
			this.getZs(this.dataFormZS.monitorCallid)
		},
		updateZS(){
			AddMonitorFlag(this.dataFormZS).then(res => {
				Message({
		          message: res.errMsg,
		          type: 'success',
		          duration: 5 * 1000
		        });
			})
			this.dialogZSVisible = false
		},
		/** 注释 结束 */
	
		dateFormats: function (val) {
			if(!val){
				return undefined
			}
			return moment(val).format("YYYY-MM-DD HH:mm:ss");
		},
  }
}
</script>
