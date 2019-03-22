import request from '@/utils/request'

//监狱  系统参数

export const findList = params => { return request.get('/sysQqServer/findList', { params: params } ).then(res => res) }

export const findPojo = params => { return request.get('/sysQqServer/findPojo', { params: params } ).then(res => res) }

export const findOne = params => { return request.get('/sysQqServer/findOne', { params: params } ).then(res => res) }

export const RequestAdd = params => { return request.post('/sysQqServer/add', params ).then(res => res) }

export const RequestEdit = params => { return request.post('/sysQqServer/edit', params ).then(res => res) }

export const RequestDelete = params => { return request.post('/sysQqServer/delete', params ).then(res => res) }