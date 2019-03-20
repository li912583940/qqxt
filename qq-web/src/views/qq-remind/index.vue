<!--
	描述：亲情提醒
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
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('criminal.search')}}</el-button>
      <el-button v-if="buttonRole.exportPermission==1" class="filter-item" style="margin-left: 10px;" type="primary" v-waves icon="el-icon-download" @click="handleDownload">{{$t('criminal.export')}}</el-button>
  	</div>
    
    <el-table :key='tableKey' :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
      style="width: 481px">
      <el-table-column width="160" align="center" :label="$t('currency.jqName')" >
        <template slot-scope="scope">
          <span>{{scope.row.jqName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" :label="$t('currency.frNo')">
        <template slot-scope="scope">
          <span>{{scope.row.frNo}}</span>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" :label="$t('currency.frName')">
        <template slot-scope="scope">
          <span>{{scope.row.frName}}</span>
        </template>
      </el-table-column>
    </el-table>
	<!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { findPojo, exportExcel} from '@/api/qqRemind'

import moment from 'moment'
import waves from '@/directive/waves' // 水波纹指令
import { Message, MessageBox } from 'element-ui'

export default {
  name: 'qqRemind',
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
        pageNum: 1,
        pageSize: 10,
      },
      
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
      
      //按钮权限   1：有权限， 0：无权限
      buttonRole: { 
      	queryPermission: 1, 
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
  },
  created() {
    this.getList()
  },
  mounted() {
    this.setButtonRole()
  },
  methods: {
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
    		this.buttonRole.exportPermission= 1
    	}else{
    		let buttonRoles = JSON.parse(sessionStorage.getItem("buttonRoles"))
    		let qqRemind = buttonRoles.qqRemind
    		if(qqRemind.length>0){
    			for(let value of qqRemind){
    				if(value=='exportPermission'){
    					this.buttonRole.exportPermission= 1
    				}
    			}
    		}
    	}
    },

    handleDownload() { //导出
   		Message({
	        message: '已准备导出<未打电话罪犯名单>文件，请稍等几秒。',
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
        	window.navigator.msSaveOrOpenBlob(blob, '未打电话罪犯名单.xls');
    		}else{ //非IE浏览器
    			var downloadElement = document.createElement('a')
		     	var href = window.URL.createObjectURL(blob)
		     	downloadElement.href = href
		     	downloadElement.download = '未打电话罪犯名单.xls'
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
