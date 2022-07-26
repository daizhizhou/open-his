import request from '@/utils/request'

// 分页查询所有的采购单数据
export function listPurchaseForPage(query) {
  return request({
    url: '/erp/purchase/listPurchaseForPage',
    method: 'get',
    params: query
  })
}
// 分页查询所有的待审核采购单数据
export function listPurchasePendingForPage(query) {
  return request({
    url: '/erp/purchase/listPurchasePendingForPage',
    method: 'get',
    params: query
  })
}
// 提交审核【根据采购单号】
export function doAudit(purchaseId) {
  return request({
    url: '/erp/purchase/doAudit/' + purchaseId,
    method: 'post'
  })
}
// 作废【根据采购单号】
export function doInvalid(purchaseId) {
  return request({
    url: '/erp/purchase/doInvalid/' + purchaseId,
    method: 'post'
  })
}
// 审核通过【根据采购单号】
export function auditPass(purchaseId) {
  return request({
    url: '/erp/purchase/auditPass/' + purchaseId,
    method: 'post'
  })
}
// 审核不通过【根据采购单号】
export function auditNoPass(purchaseId, auditMsg) {
  return request({
    url: '/erp/purchase/auditNoPass/' + purchaseId + '/' + auditMsg,
    method: 'post'
  })
}
// 根据采购单据ID查询采购详情信息
export function getPurchaseItemById(purchaseId) {
  return request({
    url: '/erp/purchase/getPurchaseItemById/' + purchaseId,
    method: 'get'
  })
}
