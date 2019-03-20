<!--
	描述：充值统计
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
  	  <el-select clearable style="width: 200px" class="filter-item" v-model="listQuery.jqNo" placeholder="选择罪犯监区">
        <el-option v-for="item in jqs" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入罪犯编号" v-model="listQuery.frNo" clearable>
      </el-input>
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="输入罪犯姓名" v-model="listQuery.frName" clearable>
      </el-input>
      <el-select clearable style="width: 200px" class="filter-item" v-model="listQuery.type" placeholder="选择充值方式">
        <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('criminal.search')}}</el-button>
      <el-button v-if="buttonRole.exportPermission==1" class="filter-item" style="margin-left: 10px;" type="primary" v-waves icon="el-icon-download" @click="handleDownload">{{$t('criminal.export')}}</el-button>
  	</div>
    
    <el-table :key='tableKey' :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
      style="width: 1231px">
      <el-table-column width="100" align="center" :label="$t('currency.jqName')" >
        <template slot-scope="scope">
          <span>{{scope.row.jqName}}</span>
        </template>
      </el-table-column>
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
      <el-table-column width="110" align="center" label="充值金额">
        <template slot-scope="scope">
          <span>{{scope.row.czje | qqYeFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" label="充值时间">
        <template slot-scope="scope">
          <span>{{scope.row.czsj | dateFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="充值人编号">
        <template slot-scope="scope">
          <span>{{scope.row.czUserNo}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="充值人名称">
        <template slot-scope="scope">
          <span>{{scope.row.czUserName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" label="修改时间">
        <template slot-scope="scope">
          <span>{{scope.row.scsj | dateFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="修改人编号">
        <template slot-scope="scope">
          <span>{{scope.row.scUserNo}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="修改人名称">
        <template slot-scope="scope">
          <span>{{scope.row.scUserName}}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="buttonRole.printPermission==1" align="center" :label="$t('criminal.actions')" width="120" >
        <template slot-scope="scope">
          <el-button v-if="buttonRole.printPermission==1 && scope.row.ccState==1" type="primary" size="mini" @click="openPrint(scope.row)">打印</el-button>
        </template>
      </el-table-column>
    </el-table>
    <span style="margin-top: 15px;"></span>
	<div class="filter-container">
    	<span v-if="czCountSum !=null" >
    		<span style="margin-left: 20px;">充值总额为：{{czCountSum.countIn/1000}}（元）</span>
	    	<span style="margin-left: 20px;">退费总额为：{{czCountSum.thcountOutZe/1000}}（元）</span>
	    	<span style="margin-left: 20px;">实际金额：{{czCountSum.sjcountInZe/1000}}（元）</span>
    	</span>
    </div>
		<!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    
   <!-- 打印小票 -->
    <el-dialog title="" :visible.sync="dialogFormVisible">
      <div id="wrap" class="wrap">
		  	<span v-for="x in this.printList">
		  	  <li>{{ x}}</li>
		  	</span>
		  </div>
      <div slot="footer" class="dialog-footer">
      	<el-button type="primary" @click="print">打 印</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        
      </div>
    </el-dialog>
    
  </div>
</template>

<script>
import { findPojo, findDetailsPojo, exportExcel} from '@/api/czCount'
import { findList as findJqList} from '@/api/jqSet'

import moment from 'moment'
import waves from '@/directive/waves' // 水波纹指令
import { Message, MessageBox } from 'element-ui'

export default {
  name: 'czCount',
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
      	callTimeStart: undefined,
      	callTimeEnd: undefined,
      	jqNo: undefined,
      	frNo: undefined,
      	frName: undefined,
      	type: undefined,
        pageNum: 1,
        pageSize: 10,
      },
      jqs: [],// 监区下拉选框
      czCountSum: null,
      
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
	  types: [ // 充值方式
        {
      		id: 1,
      		name: '充值'
      	},
      	{
      		id: 2,
      		name: '退费'
      	}
      
      ],
      
      //按钮权限   1：有权限， 0：无权限
      buttonRole: { 
      	queryPermission: 1, 
      	printPermission: 0, 
      	exportPermission: 0, //导出
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
      if(!this.callTimeStart1){
      	this.listQuery.callTimeStart = undefined
      }else{
      	this.listQuery.callTimeStart = this.dateFormatYMD(this.callTimeStart1)+" 00:00:00";
      }
      if(!this.callTimeEnd1){
      	this.listQuery.callTimeEnd = undefined
      }else{
      	this.listQuery.callTimeEnd = this.dateFormatYMD(this.callTimeEnd1)+" 23:59:59";
      }
      findPojo(this.listQuery).then((res) => {
      	 this.list = res.pojo.list
      	 this.total = res.pojo.count
      	 
      	 if(res.czCountSum){
      	 	this.czCountSum = res.czCountSum
      	 }else{
      	 	this.czCountSum = null
      	 }
      	 
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
    		this.buttonRole.printPermission= 1
    		this.buttonRole.exportPermission= 1
    	}else{
    		let buttonRoles = JSON.parse(sessionStorage.getItem("buttonRoles"))
    		let czCount = buttonRoles.czCount
    		if(czCount.length>0){
    			for(let value of czCount){
    				if(value=='printPermission'){
    					this.buttonRole.printPermission= 1
    				}else if(value=='exportPermission'){
    					this.buttonRole.exportPermission= 1
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

    handleDownload() { //导出
   		Message({
	        message: '已准备导出话费帐单文件，请稍等几秒。',
		      type: 'success',
		      duration: 5 * 1000
	    });
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
		exportExcel(this.listQuery).then(res => {
	        var blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8' })
	     	if (window.navigator && window.navigator.msSaveOrOpenBlob) { // IE浏览器
        	window.navigator.msSaveOrOpenBlob(blob, '话费帐单.xls');
    		}else{ //非IE浏览器
    			var downloadElement = document.createElement('a')
		     	var href = window.URL.createObjectURL(blob)
		     	downloadElement.href = href
		     	downloadElement.download = '话费帐单.xls'
		     	document.body.appendChild(downloadElement)
		     	downloadElement.click()
	     		document.body.removeChild(downloadElement) // 下载完成移除元素
		     	window.URL.revokeObjectURL(href) // 释放掉blob对象
    		}
	     	
			}).catch(error => {
         console.log(error)
      })

    },
   
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
