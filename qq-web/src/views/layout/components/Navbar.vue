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

        <lang-select class="international right-menu-item"/>

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
    
    
    <!-- 新增或编辑 -->
    <el-dialog title="修改密码" :visible.sync="dialogFormVisible">
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
			<object :id="item.serverName" :name="item.serverName" codebase="../../ocx/TeleQqOcx.ocx#version=1,0,0,1" classid="clsid:561E476B-6C4F-4FCC-A8CE-A85C7F781620" 
		 		width="0" height="0">
			</object>
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
    	// 注册控件
      sysQqServerList: null,
      
    	dialogFormVisible: false,
    	dataForm: { 
        webId: undefined,
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
  	this.getHjServerList()
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

    //重置表单
		resetForm() {
			this.dataForm.webId= undefined
	    this.dataForm.userName= undefined
	 		this.dataForm.userPwdOld= undefined
	 		this.dataForm.userPwdNew= undefined
	  },
    openEditUser() { //打开修改密码弹框
    	this.resetForm()
    	
    	this.dialogFormVisible = true
    	let user = JSON.parse(sessionStorage.getItem("user"))
    	if(user){
    		this.dataForm.webId=user.webId
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
	    		webId: user.webId
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
    getHjServerList() { // 获取服务器用于注册控件
			GetQqServerList({}).then(res => {
				this.sysQqServerList = res.list
			})
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
</style>