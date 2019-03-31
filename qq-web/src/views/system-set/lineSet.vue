<!--
	作者：912583940@qq.com
	时间：2018-11-01
	描述： 线路设置
-->
<template>
  <div class="app-container">
    <el-table :key='tableKey' :data="list"   border fit highlight-current-row
      style="width: 901px">
      <el-table-column width="100" align="center" label="线路逻辑号">
        <template slot-scope="scope">
          <span>{{scope.row.lineNo}}</span>
        </template>
      </el-table-column>
      <el-table-column width="120" align="center" label="服务器名称">
        <template slot-scope="scope">
          <span>{{scope.row.jy}}</span>
        </template>
      </el-table-column>
      <el-table-column width="100" align="center" label="线路监区">
        <template slot-scope="scope">
          <span>{{scope.row.jqName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" label="读卡器">
        <template slot-scope="scope">
          <span>{{scope.row.dkq}}</span>
        </template>
      </el-table-column>
      <el-table-column width="100" align="center" label="线路状态">
        <template slot-scope="scope">
          <span v-if="scope.row.state==0" style="color: red;">关闭</span>
          <span v-if="scope.row.state==1">开启</span>
        </template>
      </el-table-column>
      <el-table-column width="160" align="center" label="线路号码">
        <template slot-scope="scope">
          <span>{{scope.row.localTele}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('criminal.actions')" width="160">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(scope.row)">配置</el-button>
        </template>
      </el-table-column>
    </el-table>

		<!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

	<!-- 新增或编辑 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="600px">
      <el-form :rules="rules" :model="dataForm" ref="dataForm" label-position="right" label-width="120px" style='width: 400px; margin-left:10%;' >
        <el-form-item label="线路逻辑号">
          <el-input v-model="dataForm.lineNo" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="服务器名称" prop="jy"> 
          <el-select class="filter-item" v-model="dataForm.jy" placeholder="请选择">
            <el-option v-for="item in jys" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="线路监区" prop="jq">
          <el-select class="filter-item" v-model="dataForm.jq" placeholder="请选择">
            <el-option v-for="item in jqs" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="读卡器">
          <el-input v-model="dataForm.dkq"></el-input>
        </el-form-item>
        <el-form-item label="线路号码">
          <el-input v-model="dataForm.localTele"></el-input>
        </el-form-item>
        <el-form-item label="线路状态">
          <el-radio-group v-model="dataForm.state">
		    <el-radio :label="0">关闭</el-radio>
		    <el-radio :label="1">开启</el-radio>
		  </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确 定</el-button>
        <el-button v-else type="primary" @click="updateData">确 定</el-button>
      </div>
    </el-dialog>
    
	
  </div>
</template>

<script>
	
import { findPojo, findOne, RequestAdd, RequestEdit, RequestDelete, findDeptNameList} from '@/api/lineSet'
import { findList as findJqList} from '@/api/jqSet'
import { findList as findJyList} from '@/api/sysQqServer'

import moment from 'moment';
import waves from '@/directive/waves' // 水波纹指令


export default {
  name: 'lineSet',
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
      jys:[],
      jqs:[],
      dataForm: { 
        webid: undefined,
        jy: undefined,
        jq: undefined,
        dkq: undefined,
        localTele: undefined,
        state: 1
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '配 置',
        create: '新 增'
      }
   
    }
  },
  filters: {
    
  },
  created() {
    this.getList()
  },
  mounted() {
    this.getJyList()
    this.getJqList()
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
	        this.dataForm.lineNo =  res.data.lineNo
	        this.dataForm.jy = res.data.jy
	        this.dataForm.jq = res.data.jq
	        this.dataForm.dkq = res.data.dkq
	        this.dataForm.localTele = res.data.localTele
	        this.dataForm.state = res.data.state
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
	        this.dialogFormVisible = false
	      })
		})
	},
	getJyList(){
		if(this.jys.length === 0){
			findJyList({}).then( res =>{
				let list = res.list
				for(let x of list){
					let value = {}
					value.id = x.serverName
					value.name = x.serverName
					this.jys.push(value)
				}
			})
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
    
	dateFormats: function (val) {
		if(!val){
			return undefined
		}
		return moment(val).format("YYYY-MM-DD HH:mm:ss");
	},
  }
}
</script>
