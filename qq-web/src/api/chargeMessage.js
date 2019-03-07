import request from '@/utils/request'
// 话费充值

export const findPojo = params => { return request.get('/qqChargeMessage/findPojo', { params: params } ).then(res => res) }

// 充值
export const RequestRecharge = params => { return request.post('/qqChargeMessage/requestRecharge', params).then(res => res)}

//出狱退费
export const RequestRefund = params => { return request.post('/qqChargeMessage/requestRefund', params ).then(res => res) }

// 查询充值明细
export const findDetailsPojo = params => { return request.get('/qqChargeMessage/findDetailsPojo', { params: params } ).then(res => res) }

//修改明细
export const RequestDetailsUpdate = params => { return request.post('/qqChargeMessage/requestDetailsUpdate', params ).then(res => res) }
