// 亲情监控JS
import request from '@/utils/request'

export const findPojo = params => { return request.get('/qqMonitor/findPojo', { params: params } ).then(res => res) }

// 更新监听警察信息
export const UpdateYJ = params => { return request.post('/qqMonitor/updateYJ', params ).then(res => res) }

//  获取服务器
export const GetQqServerList = params => { return request.get('/sysQqServer/findList', { params: params } ).then(res => res) }

// 获取网络监控插话表
export const GetMonitorVocList = params => { return request.get('/jlMonitorVoc/findList', { params: params } ).then(res => res) }

// 获取当前用户在此次通话的注释
export const GetZs = params => { return request.get('/qqMonitor/getZs', { params: params } ).then(res => res) }

// 注释
export const AddMonitorFlag = params => { return request.post('/qqMonitor/addMonitorFlag', params ).then(res => res) }

