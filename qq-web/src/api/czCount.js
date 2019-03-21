import request from '@/utils/request'

// 充值统计

export const findPojo = params => { return request.get('/czCount/findPojo', { params: params } ).then(res => res) }

export const findPrint = params => { return request.get('/czCount/findPrint', { params: params } ).then(res => res) }

// 导出EXCEL
export function exportExcel(param) {
  return request({
    url: '/czCount/exportExcel',
    method: 'post',
    data: param,
    timeout: 600000,
    responseType:'blob'
  }).then(res => res)
}