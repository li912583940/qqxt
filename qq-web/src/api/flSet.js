import request from '@/utils/request'

//费率设置

export const findPojo = params => { return request.get('/sysQqFl/findPojo', { params: params } ).then(res => res) }

export const findOne = params => { return request.get('/sysQqFl/findOne', { params: params } ).then(res => res) }

export const RequestAdd = params => { return request.post('/sysQqFl/add', params ).then(res => res) }

export const RequestEdit = params => { return request.post('/sysQqFl/edit', params ).then(res => res) }

export const RequestDelete = params => { return request.post('/sysQqFl/delete', params ).then(res => res) }

export const findSysParam = params => { return request.get('/sysQqFl/findSysParam', { params: params } ).then(res => res) }

export const RequestFlData = params => { return request.post('/sysParam/edit', params ).then(res => res) }
