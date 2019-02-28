import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/views/layout/Layout'

/* Router Modules */

//import componentsRouter from './modules/components'
//import chartsRouter from './modules/charts'
//import tableRouter from './modules/table'
//import nestedRouter from './modules/nested'

/** note: Submenu only appear when children.length>=1
 *  detail see  https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 **/

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    roles: ['admin','editor']     will control the page roles (you can set multiple roles)
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
    noCache: true                if true ,the page will no be cached(default is false)
  }
**/
export const constantRouterMap = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/authredirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/errorPage/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/errorPage/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index'),
      name: 'Dashboard',
      meta: { title: 'dashboard', icon: 'dashboard', noCache: true }
    }]
  },
  
  { // 罪犯管理-添加家属
    path: '/addCriQs',
    component: Layout,
    children: [
      {
        path: '/addCriQs',
        component: () => import('@/views/criminal/addQs'),
      }
    ],
    hidden: true
  },
]

export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

// 动态权限路由
export const asyncRouterMap = [
  { // 服刑人员管理
    path: '/criminal',
    component: Layout,
    //redirect: '/criminal',
    name: 'criminalManage',
    meta: {
      title: 'criminalManage',
      icon: 'peoples',
      roles:'criminalManage'
    },
    children: [
      { path: 'criminal', component: () => import('@/views/criminal/criminal'), name: 'criminal', meta: { title: 'criminal', icon: 'peoples', roles:'criminal' }}, //服刑人员
      { path: 'relatives', component: () => import('@/views/criminal/relatives'), name: 'relatives', meta: { title: 'relatives', icon: 'peoples', roles:'relatives'}} // 亲属
    ]
  },

  { // 干警信息
    path: '/yjMessage',
    component: Layout,
    meta: {
      roles:'yjMessage'
    },
    children: [{
      path: 'index',
      component: () => import('@/views/yj-message/index'),
      name: 'yjMessage',
      meta: { title: 'yjMessage', icon: 'people', roles:'yjMessage' }
    }]
  },
  
  { // 实时监控
    path: '/realtMonitor',
    component: Layout,
    meta: {
      roles:'realtMonitor'
    },
    children: [{
      path: 'index',
      component: () => import('@/views/realt-monitor/index'),
      name: 'realtMonitor',
      meta: { title: 'realtMonitor', icon: 'form', roles:'realtMonitor' }
    }]
  },
   
  { // 通话录音
    path: '/callRecord',
    component: Layout,
    meta: {
      roles:'callRecord'
    },
    children: [{
      path: 'index',
      component: () => import('@/views/call-record/index'),
      name: 'callRecord',
      meta: { title: 'callRecord', icon: 'form', roles:'callRecord' }
    }]
  },
  
  { // 话费充值
    path: '/chargeMessage',
    component: Layout,
    meta: {
      roles:'chargeMessage'
    },
    children: [{
      path: 'index',
      component: () => import('@/views/charge-message/index'),
      name: 'chargeMessage',
      meta: { title: 'chargeMessage', icon: 'form', roles:'chargeMessage' }
    }]
  },
  
  { // 话务统计
    path: '/telCost',
    component: Layout,
    meta: {
      roles:'telCost'
    },
    children: [{
      path: 'index',
      component: () => import('@/views/tel-cost/index'),
      name: 'telCost',
      meta: { title: 'telCost', icon: 'form', roles:'telCost' }
    }]
  },
  
  { // 日志管理
    path: '/diaryMessage',
    component: Layout,
    meta: {
      roles:'diaryMessage'
    },
    children: [{
      path: 'index',
      component: () => import('@/views/diary-message/index'),
      name: 'diaryMessage',
      meta: { title: 'diaryMessage', icon: 'form', roles:'diaryMessage' }
    }]
  },

  { // 亲情提醒
    path: '/qqRemind',
    component: Layout,
    meta: {
      roles:'qqRemind'
    },
    children: [{
      path: 'index',
      component: () => import('@/views/qq-remind/index'),
      name: 'qqRemind',
      meta: { title: 'qqRemind', icon: 'form', roles:'qqRemind' }
    }]
  },
  
  { // 充值统计
    path: '/czCount',
    component: Layout,
    meta: {
      roles:'czCount'
    },
    children: [{
      path: 'index',
      component: () => import('@/views/cz-count/index'),
      name: 'czCount',
      meta: { title: 'czCount', icon: 'form', roles:'czCount' }
    }]
  },
  
    { // 系统设置
    path: '/systemSet',
    component: Layout,
    //redirect: '/systemSet',
    name: 'systemSet',
    meta: {
      title: 'systemSet',
      icon: 'tab',
      roles:'systemSet'
    },
    children: [
      { path: 'sysUser', component: () => import('@/views/system-set/sysUser'), name: 'sysUser', meta: { title: 'sysUser', icon: 'user', roles:'sysUser' }}, //系统用户管理
      { path: 'sysRoles', component: () => import('@/views/system-set/sysRoles'), name: 'sysRoles', meta: { title: 'sysRoles', icon: 'user', roles:'sysRoles'}}, //系统权限配置
      { path: 'criminalLevel', component: () => import('@/views/system-set/criminalLevel'), name: 'criminalLevel', meta: { title: 'criminalLevel', icon: 'tree', roles:'criminalLevel' }}, //服刑人员级别
      { path: 'jqSet', component: () => import('@/views/system-set/jqSet'), name: 'jqSet', meta: { title: 'jqSet', icon: 'tab', roles:'jqSet' }}, //监区设置
      //{ path: 'spSet', component: () => import('@/views/system-set/spSet'), name: 'spSet', meta: { title: 'spSet', icon: 'tab', roles:'spSet' }}, //审批设置
      { path: 'lineSet', component: () => import('@/views/system-set/lineSet'), name: 'lineSet', meta: { title: 'lineSet', icon: 'tab', roles:'lineSet' }}, // 线路设置
      { path: 'gxManage', component: () => import('@/views/system-set/gxManage'), name: 'gxManage', meta: { title: 'gxManage', icon: 'tab', roles:'gxManage' }}, // 亲属关系
      { path: 'deptManage', component: () => import('@/views/system-set/deptManage'), name: 'deptManage', meta: { title: 'deptManage', icon: 'tab', roles:'deptManage' }}, // 部门管理
      { path: 'sysParam', component: () => import('@/views/system-set/sysParam'), name: 'sysParam', meta: { title: 'sysParam', icon: 'tab', roles:'sysParam' }} // 系统参数
    ]
  },
  
  
//{ // 会见审批
//  path: '/meetSp',
//  component: Layout,
//  meta: {
//    roles:'meetSp'
//  },
//  children: [{
//    path: 'index',
//    component: () => import('@/views/meet-sp/index'),
//    name: 'meetSp',
//    meta: { title: 'meetSp', icon: 'documentation', roles:'meetSp' }
//  }]
//},
  
  { path: '*', redirect: '/404', hidden: true }
]
