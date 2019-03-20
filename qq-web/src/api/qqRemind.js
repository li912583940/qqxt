import request from '@/utils/request'

// 亲情提醒

export const findPojo = params => { return request.get('/qqRemind/findPojo', { params: params } ).then(res => res) }

// 导出EXCEL
export function exportExcel(param) {
  return request({
    url: '/qqRemind/exportExcel',
    method: 'post',
    data: param,
    timeout: 600000,
    responseType:'blob'
  }).then(res => res)
}