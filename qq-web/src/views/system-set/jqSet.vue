<!--
监区
-->
<template>
  <div class="app-container">
  	<div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="监区名称" v-model="listQuery.jqName" clearable>
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">搜索</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-circle-plus-outline">添加</el-button>
    </div>
    
    <el-table :key='tableKey' :data="list"   border fit highlight-current-row
      style="width: 671px">
      <el-table-column width="100" align="center" :label="$t('currency.jqNo')">
        <template slot-scope="scope">
          <span>{{scope.row.jqNo}}</span>
        </template>
      </el-table-column>
      <el-table-column width="150" align="center" :label="$t('currency.jqName')">
        <template slot-scope="scope">
          <span>{{scope.row.jqName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="100" align="center" label="特殊监区">
        <template slot-scope="scope">
          <span v-if="scope.row.isTs==0">否</span>
          <span v-if="scope.row.isTs==1" style="color: red;">是</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('criminal.actions')" width="320" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          <el-button size="mini" type="info" icon="el-icon-setting" @click="openWeek(scope.row)">亲情星期设置</el-button>
        </template>
      </el-table-column>
    </el-table>

		<!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

	<!-- 新增或编辑 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="600px" :modal-append-to-body="false">
      <el-form :rules="rules" :model="dataForm" ref="dataForm" label-position="right" label-width="120px" style='width: 400px; margin-left:10%;' >
        <el-form-item label="监区编号" prop="jqNo">
          <el-input v-if="dialogStatus=='update'" v-model="dataForm.jqNo" :disabled="true"></el-input>
          <el-input v-if="dialogStatus=='create'" v-model="dataForm.jqNo"></el-input>
        </el-form-item>
        <el-form-item label="监区名称" prop="jqName">
          <el-input v-model="dataForm.jqName"></el-input>
        </el-form-item>
        <el-form-item label="楼层" prop="floor">
          <el-input v-model="dataForm.floor"></el-input>
        </el-form-item>
        <el-form-item label="特殊监区">
          <el-radio-group v-model="dataForm.isTs">
		    <el-radio :label="0">否</el-radio>
		    <el-radio :label="1">是</el-radio>
		  </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确 定</el-button>
        <el-button v-else type="primary" @click="updateData">确 定</el-button>
      </div>
    </el-dialog>
    
    <!-- 亲情星期设置开始   -->
		<el-dialog title="亲情星期设置" :visible.sync="dialogWeekVisible"width="820px" :modal-append-to-body="false">
			<div class="filter-container">
	      <el-button class="filter-item" style="margin-left: 10px;" @click="openWeekAdd" type="primary" icon="el-icon-circle-plus-outline">{{$t('criminal.add')}}</el-button>
	    </div>
      <el-table :key='weekTableKey' :data="weekList" v-loading="weekListLoading" element-loading-text="给我一点时间" border fit highlight-current-row
	      style="width: 751px;margin-left: 10px;">
	      <el-table-column width="110" align="center" :label="$t('currency.jqNo')">
	        <template slot-scope="scope">
	          <span>{{scope.row.jqNo}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="110" align="center" :label="$t('currency.jqName')">
	        <template slot-scope="scope">
	          <span>{{scope.row.jqName}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="110" align="center" label="会见星期">
	        <template slot-scope="scope">
	          <span v-if="scope.row.jqWeek==0">星期天</span>
	          <span v-if="scope.row.jqWeek==1">星期一</span>
	          <span v-if="scope.row.jqWeek==2">星期二</span>
	          <span v-if="scope.row.jqWeek==3">星期三</span>
	          <span v-if="scope.row.jqWeek==4">星期四</span>
	          <span v-if="scope.row.jqWeek==5">星期五</span>
	          <span v-if="scope.row.jqWeek==6">星期六</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="110" align="center" label="开始时间">
	        <template slot-scope="scope">
	          <span>{{scope.row.jqStarttime}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column width="110" align="center" label="结束时间">
	        <template slot-scope="scope">
	          <span>{{scope.row.jqEndtime}}</span>
	        </template>
	      </el-table-column>
	      <el-table-column  align="center" :label="$t('criminal.actions')" width="200"  fixed="right">
	        <template slot-scope="scope">
	          <el-button  type="primary" size="mini" icon="el-icon-edit" @click="openWeekUpdate(scope.row)">编辑</el-button>
	          <el-button  size="mini" type="danger" icon="el-icon-delete" @click="handleWeekDelete(scope.row)">删除</el-button>
	        </template>
	      </el-table-column>
	    </el-table>
	    <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogWeekVisible = false">关闭</el-button>
      </span>
		</el-dialog>
	  
	  <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogWeekFormVisible" width="700px" :modal-append-to-body="false">
      <el-form :model="dataFormWeek"  label-position="right" label-width="180px" style='width: 400px; margin-left:7%;' >
        <el-form-item label="监区编号" prop="jqNo">
          <el-input v-model="dataFormWeek.jqNo" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="监区名称" prop="jqName">
          <el-input v-model="dataFormWeek.jqName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="星期" prop="jqWeek">
          <el-select class="filter-item" v-model="dataFormWeek.jqWeek" placeholder="请选择">
            <el-option v-for="item in jqWeeks" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="通话时间段" prop="jqTime">
          <el-time-picker
				    is-range
				    v-model="jqTime"
				    range-separator="至"
				    start-placeholder="开始时间"
				    end-placeholder="结束时间"
				    placeholder="选择时间范围"
				    value-format="HH:mm"
				    format="HH:mm"
				    >
				  </el-time-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogWeekFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createWeekData">确 定</el-button>
        <el-button v-else type="primary" @click="updateWeekData">确 定</el-button>
      </div>
    </el-dialog>	
	  <!-- 亲情星期设置 结束   -->
	  
  </div>
</template>

<script>
import { findPojo, findOne, RequestAdd, RequestEdit, RequestDelete, 
	findWeekList, findWeekOne, RequestWeekAdd, RequestWeekEdit, RequestWeekDelete} from '@/api/jqSet'

import moment from 'moment';
import waves from '@/directive/waves' // 水波纹指令
import { Message, MessageBox } from 'element-ui'

export default {
  name: 'jqSet',
  directives: {
    waves
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: null,
      listQuery: {
      	jqName: undefined,
        pageNum: 1,
        pageSize: 20
      },
      // 新增或编辑弹窗
      dataForm: { 
        webid: undefined,
        jqNo: undefined,
        jqName: undefined,
        isTs: 0,
        floor: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编 辑',
        create: '新 增'
      },
      rules: {
       	jqNo: [{ required: true, message: '监区编号不能为空', trigger: 'blur' }],
        jqName: [{ required: true, message: '监区名称不能为空', trigger: 'blur' }]
      },
      
      /**--------------------亲情星期设置   开始--------------------------*/
      dialogWeekVisible: false,
      weekTableKey: 0,
      weekList: null,
      weekListLoading: true,
      weekListQuery: {
        jqNo: undefined,
        jqName: undefined,
      },
      
      dialogWeekFormVisible: false,
      jqTime: null,
      dataFormWeek: { 
        timeIndex: undefined,
        jqNo: undefined,
        jqName: undefined,
        jqWeek: undefined,
        jqStarttime: undefined,
        jqEndtime: undefined
      },
      jqWeeks: [
        {name: '星期一',id:1},
      	{name: '星期二',id:2},
      	{name: '星期三',id:3},
      	{name: '星期四',id:4},
      	{name: '星期五',id:5},
      	{name: '星期六',id:6},
      	{name: '星期日',id:0}
      ],
      /**---------------------亲情星期设置   结束--------------------------*/

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
    this.getList()
  },
  methods: {
    getList() {
    	if(!this.listQuery.jqName){
      	this.listQuery.jqName = undefined
      }
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
    		this.dataForm.webid = res.data.webid
	        this.dataForm.jqNo =  res.data.jqNo
	        this.dataForm.jqName = res.data.jqName
	        this.dataForm.isTs = res.data.isTs
	        this.dataForm.floor = res.data.floor
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
		    })
			})
		},
		
		
		/**------------------ 设置会见星期日开始 ----------------------*/
		getWeekList() { 
	    	this.weekListLoading = true
	      findWeekList(this.weekListQuery).then((res) => {
	      	 this.weekList = res.list
	      	 this.weekListLoading = false
	      }).catch(error => {
	         this.weekListLoading = false
	      })
	    },
		resetChecked(){ //重置
			this.dataFormWeek.timeIndex = undefined
			this.dataFormWeek.jqNo = undefined
			this.dataFormWeek.jqName = undefined
			this.dataFormWeek.jqWeek = undefined
			this.dataFormWeek.jqStarttime = undefined
      this.dataFormWeek.jqEndtime = undefined
			this.jqTime = null
		},
		openWeek(row){
			this.dialogWeekVisible = true
			
			this.weekListQuery.jqNo = row.jqNo
			this.weekListQuery.jqName = row.jqName
			this.getWeekList()
			
		},
		openWeekAdd(){
			 this.dialogStatus = 'create'
			 this.dialogWeekFormVisible = true
			 this.resetChecked()
			 
			 this.dataFormWeek.jqNo = this.weekListQuery.jqNo
			 this.dataFormWeek.jqName = this.weekListQuery.jqName
		},
		createWeekData() {
			  if(this.dataFormWeek.jqWeek==undefined){
	  	  	Message({
		        message: '请选择星期',
			      type: 'error',
			      duration: 5 * 1000
		      });
		      return false
	  	  }
			  if(this.jqTime){
			  	this.dataFormWeek.jqStarttime=this.jqTime[0]
			  	this.dataFormWeek.jqEndtime=this.jqTime[1]
			  }else{
			  	Message({
		        message: '请选择通话时间段',
			      type: 'error',
			      duration: 5 * 1000
		      });
		      return false
			  }
			  RequestWeekAdd(this.dataFormWeek).then(() => {
	        this.dialogWeekFormVisible = false
	        this.getWeekList()
        }).catch(error => {
	        //this.dialogWeekFormVisible = false
	      })
	     
	  },
	  openWeekUpdate(row){
	  	this.dataFormWeek.timeIndex = row.timeIndex
			this.dataFormWeek.jqNo = row.jqNo
			this.dataFormWeek.jqName = row.jqName
			this.dataFormWeek.jqWeek = row.jqWeek
			this.dataFormWeek.jqStarttime = row.jqStarttime
      this.dataFormWeek.jqEndtime = row.jqEndtime
	    
	    this.jqTime = []
	    let time0 = row.jqStarttime
	    time0 = '2019-01-01 '+time0
	    this.jqTime.push(new Date(Date.parse(time0.replace(/-/g, "/"))))
	    let time1 = row.jqEndtime
	    time1 = '2019-01-01 '+time1
	    this.jqTime.push(new Date(Date.parse(time1.replace(/-/g, "/"))))
	    this.dialogStatus = 'update'
	    this.dialogWeekFormVisible = true
	 
	  },
	  updateWeekData(){
	  	  if(this.dataFormWeek.jqWeek==undefined){
	  	  	Message({
		        message: '请选择星期',
			      type: 'error',
			      duration: 5 * 1000
		      });
		      return false
	  	  }
	  	  if(this.jqTime){
	  	  	if(this.jqTime[0].length!=undefined && this.jqTime[0].length==5 ){
	  	  		this.dataFormWeek.jqStarttime = this.jqTime[0]
			  	  this.dataFormWeek.jqEndtime = this.jqTime[1]
	  	  	}else{
	  	  		this.dataFormWeek.jqStarttime = this.dateFormatsHM(this.jqTime[0])
	  	  		this.dataFormWeek.jqEndtime = this.dateFormatsHM(this.jqTime[1])
	  	  	}
			  }else{
			  	Message({
		        message: '请选择通话时间段',
			      type: 'error',
			      duration: 5 * 1000
		      });
		      return false
			  }
	  	  RequestWeekEdit(this.dataFormWeek).then(() => {
          this.dialogWeekFormVisible = false
          this.getWeekList()
        }).catch(error => {
	        //this.dialogWeekFormVisible = false
	      })
	  },
	  handleWeekDelete(row){
	  	this.$confirm('确认删除该记录吗?', '提示', {
				type: 'warning'
			}).then(() => {
				this.listLoading = true;
				let param = {
	    			id: row.timeIndex
	    		}
				RequestWeekDelete(param).then(() => {
	    		this.getWeekList()
	    	}).catch(error => {
		    })
			})
	  },
		/**------------------ 设置会见星期日结束 ----------------------*/
	
		dateFormats: function (val) {
			if(!val){
				return undefined
			}
			return moment(val).format("YYYY-MM-DD HH:mm:ss");
		},
		dateFormatsHM: function (val) {
			if(!val){
				return undefined
			}
			return moment(val).format("HH:mm");
		},
  }
}
</script>
