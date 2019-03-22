// 线路设置
import request from '@/utils/request'

export const findPojo = params => { return request.get('/sysQqLine/findPojo', { params: params } ).then(res => res) }

export const findOne = params => { return request.get('/sysQqLine/findOne', { params: params } ).then(res => res) }

export const RequestAdd = params => { return request.post('/sysQqLine/add', params ).then(res => res) }

export const RequestEdit = params => { return request.post('/sysQqLine/edit', params ).then(res => res) }

export const RequestDelete = params => { return request.post('/sysQqLine/delete', params ).then(res => res) }