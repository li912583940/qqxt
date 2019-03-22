// 特殊会见日设置
import request from '@/utils/request'

export const findPojo = params => { return request.get('/sysHoliday/findPojo', { params: params } ).then(res => res) }

export const findOne = params => { return request.get('/sysHoliday/findOne', { params: params } ).then(res => res) }

export const RequestAdd = params => { return request.post('/sysHoliday/add', params ).then(res => res) }

export const RequestEdit = params => { return request.post('/sysHoliday/edit', params ).then(res => res) }

export const RequestDelete = params => { return request.post('/sysHoliday/delete', params ).then(res => res) }

export const EmptyDate = params => { return request.post('/sysHoliday/emptyDate', params ).then(res => res) }