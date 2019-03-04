// 话费充值
import request from '@/utils/request'

export const findPojo = params => { return request.get('/qqChargeMessage/findPojo', { params: params } ).then(res => res) }

export const RequestRecharge = params => { return request.post('/qqChargeMessage/requestRecharge', params).then(res => res) }