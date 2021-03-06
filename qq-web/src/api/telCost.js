import request from '@/utils/request'

// 话务统计

export const findPojo = params => { return request.get('/telCost/findPojo', { params: params } ).then(res => res) }

export const findDetailsPojo = params => { return request.get('/telCost/findDetailsPojo', { params: params } ).then(res => res) }

// 导出EXCEL
export function exportCostExcel(param) {
  return request({
    url: '/telCost/exportCostExcel',
    method: 'post',
    data: param,
    timeout: 600000,
    responseType:'blob'
  }).then(res => res)
}

// 导出EXCEL
export function exportFrCostExcel(param) {
  return request({
    url: '/telCost/exportFrCostExcel',
    method: 'post',
    data: param,
    timeout: 600000,
    responseType:'blob'
  }).then(res => res)
}
