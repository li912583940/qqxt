// 服刑人员级别
import request from '@/utils/request'

export const findPojo = params => { return request.get('/jlJb/findPojo', { params: params } ).then(res => res) }

export const findList = params => { return request.get('/jlJb/findList', { params: params } ).then(res => res) }

export const findOne = params => { return request.get('/jlJb/findOne', { params: params } ).then(res => res) }

export const RequestAdd = params => { return request.post('/jlJb/add', params ).then(res => res) }

export const RequestEdit = params => { return request.post('/jlJb/edit', params ).then(res => res) }

export const RequestDelete = params => { return request.post('/jlJb/delete', params ).then(res => res) }

export const resetQqCount = params => { return request.post('/jlJb/resetQqCount', params ).then(res => res) }

export const qzResetQqCount = params => { return request.post('/jlJb/qzResetQqCount', params ).then(res => res) }

export const addCountByJb = params => { return request.post('/jlJb/addCountByJb', params ).then(res => res) }