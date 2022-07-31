import request from '@/utils/request'

// 查询发药统计列表
export function queryDrug(params) {
  return request({
    url: '/statistics/drug/queryDrug',
    method: 'get',
    params: params
  })
}
// 查询发药数量统计列表
export function queryDrugStat(params) {
  return request({
    url: '/statistics/drug/queryDrugStat',
    method: 'get',
    params: params
  })
}

