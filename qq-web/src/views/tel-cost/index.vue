<!--
	描述：话务统计
-->
<template>
  <div class="app-container">
  	<div class="filter-container">
  	  <el-date-picker
    		style="width: 200px"
    		class="filter-item"
	      v-model="listQuery.callTimeStart"
	      align="right"
	      type="date"
	      placeholder="选择开始日期"
	      :picker-options="pickerOptionsStart">
	  </el-date-picker>
	  <el-date-picker
	    	style="width: 200px"
	    	class="filter-item"
	      v-model="listQuery.callTimeEnd"
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
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('criminal.search')}}</el-button>
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
      <el-table-column width="160" align="center" label="开始时间">
        <template slot-scope="scope">
          <span>{{scope.row.callTimeStart}}</span>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" label="结束时间">
        <template slot-scope="scope">
          <span>{{scope.row.callTimeEnd}}</span>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" label="内部话费总额(元)">
        <template slot-scope="scope">
          <span>{{scope.row.countIn | qqYeFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" label="外部话费总额(元)">
        <template slot-scope="scope">
          <span>{{scope.row.countOut | qqYeFormat}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="拨打次数">
        <template slot-scope="scope">
          <span>{{scope.row.telCountNum}}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="buttonRole.detailsPermission==1" align="center" :label="$t('criminal.actions')" width="120" >
        <template slot-scope="scope">
          <el-button v-if="buttonRole.detailsPermission==1" type="primary" size="mini" @click="openDetails(scope.row)">话费明细</el-button>
        </template>
      </el-table-column>
    </el-table>

		<!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    
    <!-- 话费明细 开始 -->
    <el-dialog title="话费明细" :visible.sync="dialogDetailsVisible" width="961px">
    	<div class="filter-container">
    	  <el-date-picker
	    		style="width: 200px"
	    		class="filter-item"
		      v-model="detailsListQuery.callTimeStart"
		      align="right"
		      type="date"
		      placeholder="选择开始日期"
		      :picker-options="pickerOptionsStart">
		  </el-date-picker>
		  <el-date-picker
		    	style="width: 200px"
		    	class="filter-item"
		      v-model="detailsListQuery.callTimeEnd"
		      align="right"
		      type="date"
		      placeholder="选择结束日期">
		  </el-date-picker>
	      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleDetailsFilter">{{$t('criminal.search')}}</el-button>
	    </div>
        <el-table :key='detailsTableKey' :data="detailsList" v-loading="detailsListLoading" element-loading-text="给我一点时间" border fit highlight-current-row
	      style="width: 861px;margin-left: 20px;">
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
	      <el-table-column width="160" align="center" label="开始时间">
	        <template slot-scope="scope">
	          <span>{{scope.row.callTimeStart}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="160" align="center" label="结束时间">
	        <template slot-scope="scope">
	          <span>{{scope.row.callTimeEnd}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="160" align="center" label="亲属姓名">
	        <template slot-scope="scope">
	          <span>{{scope.row.qsName}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="110" align="center" label="关系">
	        <template slot-scope="scope">
	          <span>{{scope.row.gx}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="110" align="center" label="通话时长">
	        <template slot-scope="scope">
	          <span>{{scope.row.callTimeLen}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="110" align="center" label="主叫号码">
	        <template slot-scope="scope">
	          <span>{{scope.row.localTele}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="110" align="center" label="被叫号码">
	        <template slot-scope="scope">
	          <span>{{scope.row.tele}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="160" align="center" label="内部话费">
	        <template slot-scope="scope">
	          <span>{{scope.row.callCountIn | qqYeFormat}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="160" align="center" label="外部话费">
	        <template slot-scope="scope">
	          <span>{{scope.row.callCountOut | qqYeFormat}}</span>
	        </template>
	      </el-table-column>
	    </el-table>
	    <div class="filter-container">
	    	<span v-if="costSum !=null">
	    		<span>内部话费总额为：{{costSum.countIn/1000}}（元）</span>
		    	<span>外部话费总额为：{{costSum.countOut/1000}}（元）</span>
		    	<span>总时长：{{costSum.telCallLen}}（分钟）</span>
	    	</span>
	    </div>
	    <!-- 分页 -->
	    <div class="pagination-container">
	      <el-pagination background @size-change="handleDetailsSizeChange" @current-change="handleDetailsCurrentChange" :current-page="detailsListQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="detailsListQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="detailsTotal">
	      </el-pagination>
	    </div>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="dialogDetailsVisible = false">关闭</el-button>
        </span>
    </el-dialog>
    <!-- 话费明细 结束 -->
  </div>
</template>

<script>
import { findPojo, findDetailsPojo} from '@/api/telCost'
import { findList as findJqList} from '@/api/jqSet'

import moment from 'moment'
import waves from '@/directive/waves' // 水波纹指令
import { Message, MessageBox } from 'element-ui'

export default {
  name: 'telCost',
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
      	callTimeStart: undefined,
      	callTimeEnd: undefined,
      	jqNo: undefined,
      	frNo: undefined,
      	frName: undefined,
        pageNum: 1,
        pageSize: 10,
        callCountType: undefined,
        jfFlag: undefined,
        callCountFlag: undefined,
      },
      jqs: [],// 监区下拉选框
      
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
      
      /* 话费明细 开始 */ 
      detailsTableKey: 0,
      detailsList: null,
      detailsTotal: null,
      detailsListLoading: true,
      detailsListQuery: {
      	callTimeStart: undefined,
      	callTimeEnd: undefined,
      	frNo: undefined,
        pageNum: 1,
        pageSize: 10
      },
      dialogDetailsVisible: false,
      costSum: null,
      /* 话费明细 结束 */
     
      
      //按钮权限   1：有权限， 0：无权限
      buttonRole: { 
      	queryPermission: 1, 
      	exportPermission: 0, //导出
      	detailsPermission: 0, //话费明细
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
      if(!this.listQuery.callTimeStart){
      	this.listQuery.callTimeStart = undefined
      }else{
      	this.listQuery.callTimeStart = this.dateFormatYMD(this.listQuery.callTimeStart)+" 00:00:00";
      }
      if(!this.listQuery.callTimeEnd){
      	this.listQuery.callTimeEnd = undefined
      }else{
      	this.listQuery.callTimeEnd = this.dateFormatYMD(this.listQuery.callTimeEnd)+" 23:59:59";
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
    		this.buttonRole.detailsPermission= 1
    	}else{
    		let buttonRoles = JSON.parse(sessionStorage.getItem("buttonRoles"))
    		let telCost = buttonRoles.telCost
    		if(telCost.length>0){
    			for(let value of telCost){
    				if(value=='exportPermission'){
    					this.buttonRole.exportPermission= 1
    				}else if(value=='detailsPermission'){
    					this.buttonRole.detailsPermission= 1
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

   
    /* 充值明细 开始  */
    openDetails(row){
    	this.detailsListQuery.frNo = row.frNo
    	this.dialogDetailsVisible=true
    	this.getDetailsList()
    },
    getDetailsList() {
      this.detailsListLoading = true
      if(!this.detailsListQuery.callTimeStart){
      	this.detailsListQuery.callTimeStart = undefined
      }else{
      	this.detailsListQuery.callTimeStart = this.dateFormatYMD(this.detailsListQuery.callTimeStart)+" 00:00:00";
      }
      if(!this.detailsListQuery.callTimeEnd){
      	this.detailsListQuery.callTimeEnd = undefined
      }else{
      	this.detailsListQuery.callTimeEnd = this.dateFormatYMD(this.detailsListQuery.callTimeEnd)+" 23:59:59";
      }
      findDetailsPojo(this.detailsListQuery).then((res) => {
      	 this.detailsList = res.pojo.list
      	 this.detailsTotal = res.pojo.count
      	 
      	 if(res.costSum){
      	 	this.costSum = res.costSum
      	 }else{
      	 	this.costSum =null
      	 }
      	 
      	 this.detailsListLoading = false
      }).catch(error => {
          this.detailsListLoading = false
      })
    },
    handleDetailsFilter() {
      this.detailsListQuery.pageNum = 1
      this.getDetailsList()
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
