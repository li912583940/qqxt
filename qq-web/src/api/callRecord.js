import request from '@/utils/request'

// 亲情记录

export const findPojo = params => { return request.get('/jlQqRec/findPojo', { params: params } ).then(res => res) }

export const findCount = params => { return request.get('/jlQqRec/findCount', { params: params } ).then(res => res) }

export const findOne = params => { return request.get('/jlQqRec/findOne', { params: params } ).then(res => res) }

// 获得监区集合
export const findJqList = params => { return request.get('/jlJq/findList', { params: params } ).then(res => res) }

// 获取当前用户在此次亲情电话记录的注释
export const GetZs = params => { return request.get('/jlQqRec/getZs', { params: params } ).then(res => res) }

// 添加当前用户的在亲情电话记录中的注释
export const AddRecordFlag = params => { return request.post('/jlQqRec/addRecordFlag', params ).then(res => res) }

// 获取当前亲情电话记录所有注释
export const GetZsAllPojo = params => { return request.get('/jlQqInfo/findPojo', { params: params } ).then(res => res) }


// 导出EXCEL
export function exportExcel(param) {
  return request({
    url: '/jlQqRec/exportExcel',
    method: 'post',
    data: param,
    responseType:'blob'
  }).then(res => res)
}

// 获取一个星期内每天亲情电话总数
export const GetWeekCount = params => { return request.get('/jlQqRec/getWeekCount', { params: params } ).then(res => res) }

// 下载音频
export function DownAudio(param) {
  return request({
    url: '/jlQqRec/downAudio',
    method: 'post',
    data: param,
    timeout: 600000,
    responseType:'blob'
  }).then(res => res)
}