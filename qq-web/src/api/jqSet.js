// 监区设置
import request from '@/utils/request'

// 获得监区集合
export const findList = params => { return request.get('/jlJq/findList', { params: params } ).then(res => res) }

export const findPojo = params => { return request.get('/jlJq/findPojo', { params: params } ).then(res => res) }

export const findOne = params => { return request.get('/jlJq/findOne', { params: params } ).then(res => res) }

export const RequestAdd = params => { return request.post('/jlJq/add', params ).then(res => res) }

export const RequestEdit = params => { return request.post('/jlJq/edit', params ).then(res => res) }

export const RequestDelete = params => { return request.post('/jlJq/delete', params ).then(res => res) }

//获取当前监区的亲情星期设置
export const findWeekList = params => { return request.get('/jlJqWeekTime/findList', { params: params } ).then(res => res) }

export const findWeekOne = params => { return request.get('/jlJqWeekTime/findOne', { params: params } ).then(res => res) }

//提交当前监区的会见星期日
export const RequestWeekAdd = params => { return request.post('/jlJqWeekTime/add', params ).then(res => res) }

export const RequestWeekEdit = params => { return request.post('/jlJqWeekTime/edit', params ).then(res => res) }

export const RequestWeekDelete = params => { return request.post('/jlJqWeekTime/delete', params ).then(res => res) }
