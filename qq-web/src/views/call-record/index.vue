<!--
	描述：通话录音
-->
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-date-picker
    		style="width: 200px"
    		class="filter-item"
	      v-model="callTimeStart"
	      align="right"
	      type="date"
	      placeholder="选择开始日期"
	      :picker-options="pickerOptionsStart">
	  </el-date-picker>
	  <el-date-picker
	    	style="width: 200px"
	    	class="filter-item"
	      v-model="callTimeEnd"
	      align="right"
	      type="date"
	      placeholder="选择结束日期">
	  </el-date-picker>
	  <el-select clearable style="width: 200px" class="filter-item" v-model="listQuery.jqNo" placeholder="选择监区">
        <el-option v-for="item in jqs" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
    	<el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入服刑人员编号" v-model="listQuery.frNo">
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入服刑人员姓名" v-model="listQuery.frName">
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入亲属姓名" v-model="listQuery.qsName">
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入电话号码" v-model="listQuery.tele">
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入本机号码" v-model="listQuery.localTele">
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入警察警号" v-model="listQuery.yjNo">
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入警察姓名" v-model="listQuery.yjName">
      </el-input>
      <el-select clearable style="width: 200px" class="filter-item" v-model="listQuery.callCountType" placeholder="选择计费方式">
        <el-option v-for="item in callCountTypes" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-select clearable style="width: 200px" class="filter-item" v-model="listQuery.jfFlag" placeholder="选择是否计费">
        <el-option v-for="item in jfFlags" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-select clearable style="width: 200px" class="filter-item" v-model="listQuery.callCountFlag" placeholder="选择扣费标志">
        <el-option v-for="item in callCountFlags" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-select clearable style="width: 200px" class="filter-item" v-model="listQuery.type" placeholder="选择通话类型">
        <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('criminal.search')}}</el-button>
      <el-button v-if="buttonRole.exportPermission==1" class="filter-item" type="primary" v-waves icon="el-icon-download" @click="handleDownload">{{$t('criminal.export')}}</el-button>
    </div>

    <el-table :key='tableKey' :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
      style="width: 1601px">
      <el-table-column width="160" align="center" label="通话开始时间">
        <template slot-scope="scope">
          <span>{{scope.row.callTimeStart}}</span>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" label="通话结束时间">
        <template slot-scope="scope">
          <span>{{scope.row.callTimeEnd}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="通话时长(秒)">
        <template slot-scope="scope">
          <span>{{scope.row.callTimeLen}}</span>
        </template>
      </el-table-column>
      <el-table-column width="130" align="center" label="电话号码">
        <template slot-scope="scope">
          <span>{{scope.row.tele}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="通话类型">
        <template slot-scope="scope">
          <span v-if="scope.row.type==0">亲情电话</span>
          <span v-else-if="scope.row.type==1">特批电话</span>
        </template>
      </el-table-column>
      <el-table-column width="90" align="center" :label="$t('currency.jqName')">
        <template slot-scope="scope">
          <span>{{scope.row.jqName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" :label="$t('currency.frNo')">
        <template slot-scope="scope">
          <span>{{scope.row.frNo}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" :label="$t('currency.frName')">
        <template slot-scope="scope">
          <span v-if="scope.row.monitorFlag !=1">{{scope.row.frName}}</span>
          <span v-if="scope.row.monitorFlag ==1" style="color: red;">{{scope.row.frName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="亲属姓名">
        <template slot-scope="scope">
          <span>{{scope.row.qsName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="90" align="center" label="关系">
        <template slot-scope="scope">
          <span>{{scope.row.gx}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="警察信息">
        <template slot-scope="scope">
          <span>{{scope.row.yjName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="130" align="center" label="本机号码">
        <template slot-scope="scope">
          <span>{{scope.row.localTele}}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="buttonRole.playAudioPermission==1 || buttonRole.downAudioPermission==1" width="100" align="center" label="录音操作" fixed="right">
        <template slot-scope="scope">
        	<div>
        		<el-button v-if="buttonRole.playAudioPermission==1" type="primary" size="mini" @click="palyTape(scope.row)">播放录音</el-button>
        	</div>
        	<div style="margin-top: 2px;">
        		<el-button v-if="buttonRole.downAudioPermission==1" type="primary" size="mini" @click="downAudio(scope.row)">下载录音</el-button>
        	</div>
        </template>
      </el-table-column>
      <el-table-column v-if="buttonRole.notesPermission==1 || buttonRole.seeNotesPermission==1" width="80" align="center" label="摘要操作" fixed="right">
        <template slot-scope="scope">
        	<div>
          		<el-button v-if="buttonRole.notesPermission==1" type="primary" size="mini" @click="zhushi(scope.row)">注释</el-button>
            </div>
            <div style="margin-top: 2px;">
            	<el-button v-if="buttonRole.seeNotesPermission==1" type="primary" size="mini" @click="zhushiAll(scope.row)">查看</el-button>
        	</div>
        </template>
      </el-table-column>
    </el-table>

		<!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    
    <!-- 注释 开始 -->
    <el-dialog title="注释" :visible.sync="dialogZSVisible"  width="600px" :modal-append-to-body="false">
	      <el-form  :model="dataFormZS" ref="dataFormZS" label-position="right" label-width="120px" style='width: 400px; margin-left:10%;' >
	        <el-form-item label="呼叫ID" >
	          <el-input v-model="dataFormZS.callId" :disabled="true"></el-input>
	        </el-form-item>
	        <el-form-item label="罪犯姓名" >
	          <el-input v-model="dataFormZS.frName" :disabled="true"></el-input>
	        </el-form-item>
	        <el-form-item label="注释">
	          <el-input type="textarea" :rows="2" v-model="dataFormZS.writeTxt"></el-input>
	        </el-form-item>
	      </el-form>
	      <div slot="footer" class="dialog-footer">
	        <el-button @click="dialogZSVisible = false">取 消</el-button>
	        <el-button type="primary" @click="updateZS">确 定</el-button>
	      </div>
	  </el-dialog>
	  <!-- 注释 结束 -->
	  
	  <!-- 查看所有注释  开始  -->
    <el-dialog title="查看所有注释" :visible.sync="dialogZsAllVisible" width="800px" :modal-append-to-body="false">
      <el-table :key='zsAllTableKey' :data="zsAllList" v-loading="zsAllListLoading" element-loading-text="给我一点时间" border fit highlight-current-row
	      style="width: 721px">
	      <el-table-column width="160" align="center" label="用户编号">
	        <template slot-scope="scope">
	          <span>{{scope.row.userNo}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="160" align="center" label="用户姓名">
	        <template slot-scope="scope">
	          <span>{{scope.row.userName}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="400" align="center" label="注释摘要">
	        <template slot-scope="scope">
	          <span>{{scope.row.writeTxt}}</span>
	        </template>
	      </el-table-column>
	    </el-table>
	    <!-- 分页 -->
	    <div class="pagination-container">
	      <el-pagination background @size-change="handleZsAllSizeChange" @current-change="handleZsAllCurrentChange" :current-page="zsAllListQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="zsAllListQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="zsAllTotal">
	      </el-pagination>
	    </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogZsAllVisible = false">关闭</el-button>
      </span>
    </el-dialog>
    <!-- 查看所有注释  结束  -->
    
    <!-- 播放录音 开始 -->
    <el-dialog title="播放录音" :visible.sync="dialogTapeVisible" @close='closeTapeDialog'  width="600px" :modal-append-to-body="false">
    	<div style="position: relative;margin-top: 10px; margin-bottom: 30px; margin-left: 10%;">
				<audio id="audio1" :src="callRecfileUrl" controls="controls" controlsList="nodownload">
				</audio>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTapeVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 播放录音 结束 -->
   
  </div>
</template>

<script>
import { findPojo, findOne, findJqList, GetZs, AddRecordFlag, GetZsAllPojo, 
	 exportExcel, DownAudio } from '@/api/callRecord'

import moment from 'moment';
import waves from '@/directive/waves' // 水波纹指令
import { parseTime } from '@/utils'
import { Message, MessageBox } from 'element-ui'
import Vue from 'vue'
import { getToken } from '@/utils/auth'

export default {
  name: 'callRecord',
  directives: {
    waves
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: null,
      listLoading: true,
      callTimeStart: undefined,
      callTimeEnd: undefined,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        callTimeStart: undefined,
        callTimeEnd: undefined,
        jqNo: undefined,
        frNo: undefined,
        frName: undefined,
        qsName: undefined,
        tele: undefined,
        localTele: undefined,
        yjNo: undefined,
        yjName: undefined,
        callCountType: undefined,
        jfFlag: undefined,
        callCountFlag: undefined,
        type: undefined,
      },
      jqs: [ // 监区下拉选框
      
      ],
      callCountTypes: [ // 计费方式
        {
      		id: 0,
      		name: '普通'
      	},
      	{
      		id: 1,
      		name: '一卡通'
      	},
      	{
      		id: 2,
      		name: '翼支付'
      	}
      ],
      jfFlags: [ // 是否计费
        {
      		id: 0,
      		name: '免费'
      	},
      	{
      		id: 1,
      		name: '计费'
      	}
      ],
      callCountFlags: [ // 扣费标志
        {
      		id: 0,
      		name: '免费'
      	},
      	{
      		id: 1,
      		name: '扣费成功'
      	},
      	{
      		id: 2,
      		name: '扣费失败'
      	},
      	{
      		id: -1,
      		name: '自动扣费中'
      	}
      ],
      types: [
        {
      		id: 0,
      		name: '亲情电话'
      	},
      	{
      		id: 1,
      		name: '特批电话'
      	}
      ],
     
      
	    pickerOptionsStart: {
	      shortcuts: [{
	        text: '今天',
	        onClick(picker) {
	          picker.$emit('pick', new Date());
	        }
	      }, {
	        text: '昨天',
	        onClick(picker) {
	          const date = new Date();
	          date.setTime(date.getTime() - 3600 * 1000 * 24);
	          picker.$emit('pick', date);
	        }
	      }, {
	        text: '最近一周',
	        onClick(picker) {
	          const date = new Date();
	          date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
	          picker.$emit('pick', date);
	        }
	      }, {
	        text: '最近一个月',
	        onClick(picker) {
	          const date = new Date();
	          date.setTime(date.getTime() - 3600 * 1000 * 24 * 30);
	          picker.$emit('pick', date);
	        }
	      }, {
	        text: '最近三个月',
	        onClick(picker) {
	          const date = new Date();
	          date.setTime(date.getTime() - 3600 * 1000 * 24 * 90);
	          picker.$emit('pick', date);
	        }
	      }, {
	        text: '最近一年',
	        onClick(picker) {
	          const date = new Date();
	          date.setTime(date.getTime() - 3600 * 1000 * 24 * 365);
	          picker.$emit('pick', date);
	        }
	      }]
	    },
	    
	    /** 播放录音录像 开始 */
	    dialogPlayVisible: false,
	    
	    dialogPlayDownVisible: false,
	    callRecfileUrl: undefined,

	    /** 播放录音录像 结束 */
	   
	   /** 注释 开始 */
      dialogZSVisible: false, 
      dataFormZS: {
      	callId: undefined,
      	frName: undefined,
      	writeTxt: undefined,
      	
      },
      /** 注释 结束 */
     
      /**  查看所有注释  开始 */
		  dialogZsAllVisible: false,
		  zsAllTableKey: 0,
		  zsAllList: null,
		  zsAllTotal: null,
		  zsAllListLoading: true,
		  zsAllListQuery: {
		    pageNum: 1,
		    pageSize: 10,
		    callId: undefined
		  },
		  /**  查看所有注释  结束 */
		 
		  /** 录音操作 开始 */
		  dialogTapeVisible: false,
		  /** 录音操作 结束 */
		 
		  //按钮权限   1：有权限， 0：无权限
      buttonRole: { 
      	queryPermission: 1, 
      	exportPermission: 0, //导出
      	playAudioPermission: 0, //播放录音
      	downAudioPermission: 0, //下载录音
      	notesPermission: 0, //注释
      	seeNotesPermission: 0, //查看所有注释
      },
      
      ie:1,
    }
  },
  filters: {
    dateFormat(date) {
		//时间格式化  
	    if (date == undefined) {  
	      return "";  
	    }  
	    return moment(date).format("YYYY-MM-DD HH:mm:ss");  
	  }
  },
  created() {
    //this.getList()
    this.noSearch()
    
    this.getJqList()
    
    this.isIe()
  },
  mounted() {
    this.setButtonRole()
  },
  methods: {
  	noSearch() {
  		this.total=0
  		this.listLoading = false
  	},
    getList() {
      this.listLoading = true
      if(!this.callTimeStart){
      	this.listQuery.callTimeStart = undefined
      }else{
      	this.listQuery.callTimeStart = this.dateFormatYMD(this.callTimeStart)+" 00:00:00";
      }
      if(!this.callTimeEnd){
      	this.listQuery.callTimeEnd = undefined
      }else{
      	this.listQuery.callTimeEnd = this.dateFormatYMD(this.callTimeEnd)+" 23:59:59";
      }

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
   
		/** 注释 开始 */
		resetFormZS() { //重置表单
			this.dataFormZS.callId = undefined
			this.dataFormZS.frName = undefined
			this.dataFormZS.writeTxt = undefined
	  },
    getZs(callId){ //获取注释
    	GetZs({callId: callId}).then(res => {
    		this.dataFormZS.writeTxt = res.data.writeTxt
    	})
    },
		zhushi(row){
			this.resetFormZS()
			this.dialogZSVisible = true
			this.dataFormZS.callId = row.callId
			this.dataFormZS.frName = row.frName
			
			this.getZs(this.dataFormZS.callId)
		},
		updateZS(){
			AddRecordFlag(this.dataFormZS).then(res => {
				Message({
		          message: res.errMsg,
		          type: 'success',
		          duration: 5 * 1000
		        });
			})
			this.dialogZSVisible = false
		},
		/** 注释 结束 */
   
    /** 查看所有注释 开始 */
    zhushiAll(row){
    	this.dialogZsAllVisible= true
    	this.zsAllListQuery.callId=row.callId
    	this.getZsAllList()
    },
    getZsAllList(){ // 获取所有注释
    	GetZsAllPojo(this.zsAllListQuery).then(res => {
    		 this.zsAllList = res.pojo.list
      	 this.zsAllTotal = res.pojo.count
      	 this.zsAllListLoading = false
      }).catch(error => {
         this.zsAllListLoading = false
      })
    },
    handleZsAllSizeChange(val) {
      this.zsAllListQuery.pageSize = val
      this.getZsAllList()
    },
    handleZsAllCurrentChange(val) {
      this.zsAllListQuery.pageNum = val
      this.getZsAllList()
    },
    /** 查看所有注释 结束 */
    
    isIe(){
    	if(navigator.appVersion.indexOf("MSIE") != -1 || (navigator.appVersion.toLowerCase().indexOf("trident") > -1 && navigator.appVersion.indexOf("rv") > -1) ){ // IE浏览器
    		this.ie=1
    	}else{
    		this.ie=0
    	}
    },
    
    /** 录音操作 开始 */
    palyTape(row) {
    	this.callRecfileUrl = row.callRecfileUrl
    	if(this.ie==1){
    	var httpPath = process.env.BASE_API
    	var tokenValue = getToken()
    		window.open("/static/html/audio.html?id="+row.webid+"&httpPath="+httpPath+"&token="+tokenValue,"","width=360,height=116,left=900,top=620,dependent=yes,scroll:no,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,directories=no,status=no")
    	}else{
    		this.dialogTapeVisible = true
    	}
    },
    downAudio(row){
    	Message({
	        message: '文件正在下载，请稍后。',
		    type: 'success',
		    duration: 5 * 1000
	    });
    	let param={
    		webid: row.webid
    	}
    	let fileName = row.frName+"-"+row.frNo+"音频.wav";
    	DownAudio(param).then(res =>{
    		var blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8' })
	     	if (window.navigator && window.navigator.msSaveOrOpenBlob) { // IE浏览器
        	window.navigator.msSaveOrOpenBlob(blob, fileName);
    		}else{ //非IE浏览器
	     		var downloadElement = document.createElement('a')
		     	var href = window.URL.createObjectURL(blob)
		     	downloadElement.href = href
		     	downloadElement.download = fileName
		     	document.body.appendChild(downloadElement)
		     	downloadElement.click()
	     		document.body.removeChild(downloadElement) // 下载完成移除元素
		     	window.URL.revokeObjectURL(href) // 释放掉blob对象
	     	}
    	}).catch(error => {
	        console.log(error)
	    })
    },
    
    closeTapeDialog(){
    	var x = document.getElementById("audio1")
    	if(this.ie==1){
    		if(x.play){
    			x.pause()
    		}
    		Vue.nextTick(() => {
		      document.getElementById("audioPlay").innerHTML= ''
		    });
    	}else{
    		if(x.play){
	    		x.currentTime = 0;
	        	x.pause();
	    	}
    	}
    	
    },
    /** 录音操作 结束 */
    
    
    
    /** 导出EXCEL 开始 */
    handleDownload() {
      if(!this.callTimeStart){
      	this.listQuery.callTimeStart = undefined
      }else{
      	this.listQuery.callTimeStart = this.dateFormatYMD(this.callTimeStart)+" 00:00:00";
      }
      if(!this.callTimeEnd){
      	this.listQuery.callTimeEnd = undefined
      }else{
      	this.listQuery.callTimeEnd = this.dateFormatYMD(this.callTimeEnd)+" 23:59:59";
      }

      Message({
        message: '已准备导出通话记录文件，请稍等几秒。',
	      type: 'success',
	      duration: 5 * 1000
      });
      
		exportExcel(this.listQuery).then(res => {
	      var blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8' })
	     	if (window.navigator && window.navigator.msSaveOrOpenBlob) { // IE浏览器
        	window.navigator.msSaveOrOpenBlob(blob, '通话记录.xls');
    		}else{ //非IE浏览器
    			var downloadElement = document.createElement('a')
		     	var href = window.URL.createObjectURL(blob)
		     	downloadElement.href = href
		     	downloadElement.download = '通话记录.xls'
		     	document.body.appendChild(downloadElement)
		     	downloadElement.click()
	     		document.body.removeChild(downloadElement) // 下载完成移除元素
		     	window.URL.revokeObjectURL(href) // 释放掉blob对象
    		}
	     	
			}).catch(error => {
         console.log(error)
      })


    },
    /** 导出EXCEL 结束 */
   
    /** 设置权限 开始 */
    setButtonRole() { //设置按钮的权限
    	let roles = sessionStorage.getItem("roles")
    	if(roles.includes('admin')){
    		this.buttonRole.exportPermission= 1
      	this.buttonRole.notesPermission= 1
      	this.buttonRole.seeNotesPermission= 1
      	this.buttonRole.playAudioPermission= 1
      	this.buttonRole.downAudioPermission= 1
      
    	}else{
    		let buttonRoles = JSON.parse(sessionStorage.getItem("buttonRoles"))
    		let callRecord = buttonRoles.callRecord
    		if(callRecord != undefined && callRecord.length>0){
    			for(let value of callRecord){
    				if(value=='exportPermission'){
    					this.buttonRole.exportPermission= 1
    				}else if(value=='notesPermission'){
    					this.buttonRole.notesPermission= 1
    				}else if(value=='seeNotesPermission'){
    					this.buttonRole.seeNotesPermission= 1
    				}else if(value=='playAudioPermission'){
    					this.buttonRole.playAudioPermission= 1
    				}else if(value=='downAudioPermission'){
    					this.buttonRole.downAudioPermission= 1
    				}
    			}
    		}
    	}
    },
    /** 设置权限 结束 */
   
		dateFormats: function (val) {
			if(!val){
				return undefined
			}
			return moment(val).format("YYYY-MM-DD HH:mm:ss");
		},
		dateFormatYMD: function (val) {
			if(!val){
				return undefined
			}
			return moment(val).format("YYYY-MM-DD");
		},
  }
}
</script>

<style>
.box-card {
  margin: 10px;
  }
</style>
