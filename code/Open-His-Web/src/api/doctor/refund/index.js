import request from '@/utils/request'
// 根据挂号ID查询未支付的退费信息及详情
export function getChargeCareHistoryByRegId(regId) {
  return request({
    url: '/doctor/backfee/getChargeCareHistoryByRegId/' + regId,
    method: 'get'
  })
}

// 分页查询所有退费订单
export function queryAllOrderBackfeeForPage(params) {
  return request({
    url: '/doctor/backfee/queryAllOrderBackfeeForPage',
    method: 'get',
    params: params
  })
}
// 根据订单ID查询订单详情
export function queryOrderBackfeeItemByBackId(backId) {
  return request({
    url: '/doctor/backfee/queryOrderBackfeeItemByBackId/' + backId,
    method: 'get'
  })
}
