<template>
  <div class="navbar">
    <hamburger :toggle-click="toggleSideBar" :is-active="sidebar.opened" class="hamburger-container"/>

    <breadcrumb class="breadcrumb-container"/>

    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <error-log class="errLog-container right-menu-item"/>

        <el-tooltip :content="$t('navbar.screenfull')" effect="dark" placement="bottom">
          <screenfull class="screenfull right-menu-item"/>
        </el-tooltip>

        <el-tooltip :content="$t('navbar.size')" effect="dark" placement="bottom">
          <size-select class="international right-menu-item"/>
        </el-tooltip>

        <!--<lang-select class="international right-menu-item"/>-->

        <el-tooltip :content="$t('navbar.theme')" effect="dark" placement="bottom">
          <theme-picker class="theme-switch right-menu-item"/>
        </el-tooltip>
      </template>

      <el-dropdown class="avatar-container right-menu-item" trigger="click">
        <div class="avatar-wrapper">
          <img :src="imagephoto" class="user-avatar">
          <i class="el-icon-caret-bottom"/>
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/">
            <el-dropdown-item>
              {{ $t('navbar.dashboard') }}
            </el-dropdown-item>
          </router-link>
          <a target="_blank" @click="openDriver">
            <el-dropdown-item>
              	下载驱动
            </el-dropdown-item>
          </a>
          <a target="_blank" @click="openEditUser">
            <el-dropdown-item>
              	修改密码
            </el-dropdown-item>
          </a>
          <a target="_blank" @click="resetUserPassword">
            <el-dropdown-item>
              	重置密码
            </el-dropdown-item>
          </a>
          <el-dropdown-item divided>
            <span style="display:block;" @click="logout">{{ $t('navbar.logOut') }}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    
    <!--下载驱动 -->
    <el-dialog title="下载驱动" :visible.sync="dialogDriverVisible" width="800px" :modal-append-to-body="false">
      <table  class="gridtable">
      	<tr height="20px">
      		<th width="15%">序号</th>
      		<th width="15%">操作系统</th>
      		<th width="50%">驱动名称</th>
      		<th width="20%">操作</th>
      	</tr>
	  	 	<tr>
	  	 		<td>1</td>
	  	 		<td>全部</td>
	  	 		<td>网页所需控件</td>
	  	 		<td><a href="/static/Drivers/AllOcx.rar"><el-button type="primary"  size="mini" v-waves icon="el-icon-download">下载</el-button></a></td>
	  	 	</tr>
	  	 	<tr>
	  	 		<td>2</td>
	  	 		<td>全部</td>
	  	 		<td>IC读卡器驱动</td>
	  	 		<td><a href="/static/Drivers/CH341SER.rar"><el-button type="primary"  size="mini" v-waves icon="el-icon-download">下载</el-button></a></td>
	  	 	</tr>
	  	</table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDriverVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
    
    <!-- 新增或编辑 -->
    <el-dialog title="修改密码" :visible.sync="dialogFormVisible" :modal-append-to-body="false">
      <el-form :rules="rules" :model="dataForm" ref="dataForm" label-position="right" label-width="120px" style='width: 400px; margin-left:25%;' >
        <el-form-item label="姓名">
          <el-input v-model="dataForm.userName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="原始密码" prop="userPwdOld">
          <el-input v-model="dataForm.userPwdOld"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="userPwdNew">
          <el-input v-model="dataForm.userPwdNew"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateData">确 定</el-button>
      </div>
    </el-dialog>
    
    <div v-for="(item, index) in sysQqServerList">
			<object :id="item.serverName" :name="item.serverName" codebase="../../../ocx/TeleQqOcx.ocx#version=1,0,0,1" classid="clsid:561E476B-6C4F-4FCC-A8CE-A85C7F781620" 
		 		width="0" height="0">
			</object>
	  </div>
	  
	  <div v-if="dkq==1">
	  	<object id="WM1711" name="WM1711" codebase="../../../ocx/WM171.ocx" classid="clsid:B56F7942-B054-416C-9BF8-C7339EB593D1" style="display: none"></object>
    	<object id="MSCOMM32" name="MSCOMM32" codebase="../../../ocx/MSCOMM32.OCX" classid="clsid:648A5600-2C6E-101B-82B6-000000000014" style="display: none"></object>
	  </div>
	  
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import ErrorLog from '@/components/ErrorLog'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import LangSelect from '@/components/LangSelect'
import ThemePicker from '@/components/ThemePicker'

import { EditPassword, ResetUserPassword } from '@/api/login'
import { Message, MessageBox } from 'element-ui'
import { GetQqServerList} from '@/api/realtMonitor'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    ErrorLog,
    Screenfull,
    SizeSelect,
    LangSelect,
    ThemePicker
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'name',
      'avatar',
      'device'
    ])
  },
  data() {
    return {
      imagephoto: '/static/image/user.jpg',
      
      dialogDriverVisible : false, //下载驱动
      
    	// 注册控件
      sysQqServerList: null,
      //注册读卡器控件
      dkq: 0,
      
    	dialogFormVisible: false,
    	dataForm: { 
        webid: undefined,
        userName: undefined,
        userPwdOld: undefined,
        userPwdNew: undefined,
      },
      
    	rules: {
        password: [{ required: true, message: '密码不能为空', trigger: 'blur' }]
      },
      
    }
  },
  mounted() {
  	this.getQqServerList()
  },
  destroyed() {

  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('toggleSideBar')
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload()// In order to re-instantiate the vue-router object to avoid bugs
      })
    },
		
		openDriver(){ // 下载驱动
    	this.dialogDriverVisible=true
    },
    
    //重置表单
		resetForm() {
			this.dataForm.webid= undefined
	    this.dataForm.userName= undefined
	 		this.dataForm.userPwdOld= undefined
	 		this.dataForm.userPwdNew= undefined
	  },
    openEditUser() { //打开修改密码弹框
    	this.resetForm()
    	
    	this.dialogFormVisible = true
    	let user = JSON.parse(sessionStorage.getItem("user"))
    	if(user){
    		this.dataForm.webid=user.webid
    		this.dataForm.userName=user.userName
    	}
    },
    
    updateData() {
    	EditPassword(this.dataForm).then(res =>{
    		Message({
	        message: '密码修改成功，下次登录时请使用新密码。',
		      type: 'success',
		      duration: 5 * 1000
	      });
    		this.dialogFormVisible = false
    	})
    	
    },
    
    resetUserPassword(){ //重置密码
    	this.$confirm('确认重置密码吗?', '提示', {
				type: 'warning'
			}).then(() => {
				let user = JSON.parse(sessionStorage.getItem("user"))
	    	if(user){
	    		let param = {
	    		webid: user.webid
	    	}
				ResetUserPassword(param).then((res) => {
					Message({
		        message: '密码重置成功。新的密码为123456，下次登录时请使用新密码。',
			      type: 'success',
			      duration: 5 * 1000
		      });
	    	}).catch(error => {
	    		
	      })
	    	}
				
			})
    },
    getQqServerList() { // 获取服务器用于注册控件
    	if(navigator.appVersion.indexOf("MSIE") != -1 || (navigator.appVersion.toLowerCase().indexOf("trident") > -1 && navigator.appVersion.indexOf("rv") > -1) ){ // IE浏览器
				GetQqServerList({}).then(res => {
					this.sysQqServerList = res.list
				})
				this.dkq=1
			}
		},
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .breadcrumb-container{
    float: left;
  }
  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }
  .right-menu {
    float: right;
    height: 100%;
    &:focus{
     outline: none;
    }
    .right-menu-item {
      display: inline-block;
      margin: 0 8px;
    }
    .screenfull {
      height: 20px;
    }
    .international{
      vertical-align: top;
    }
    .theme-switch {
      vertical-align: 15px;
    }
    .avatar-container {
      height: 50px;
      margin-right: 30px;
      .avatar-wrapper {
        cursor: pointer;
        margin-top: 5px;
        position: relative;
        .user-avatar {
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }
        .el-icon-caret-bottom {
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}

table.gridtable {
font-family: verdana,arial,sans-serif;
font-size:15px;
color:#333333;
border-width: 1px;
border-color: #76a5af;
width: 700px;
border-collapse: collapse;
margin-left: 30px;
}
table.gridtable th {
border-width: 1px;
padding: 1px;
border-style: solid;
border-color: #76a5af;
background-color: #dedede;
}
table.gridtable td {
border-width: 1px;
padding: 1px;
border-style: solid;
border-color: #76a5af;
background-color: #ffffff;
text-align:center;
}
</style>