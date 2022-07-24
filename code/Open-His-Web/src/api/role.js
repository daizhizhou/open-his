import request from '@/utils/request'

export function getRoutes() {
  return request({
    url: '/vue-element-admin/routes',
    method: 'get'
  })
}

export function getRoles() {
  return request({
    url: '/vue-element-admin/roles',
    method: 'get'
  })
}

export function addRole(data) {
  return request({
    url: '/vue-element-admin/role',
    method: 'post',
    data
  })
}

export function updateRole(id, data) {
  return request({
    url: `/vue-element-admin/role/${id}`,
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/vue-element-admin/role/${id}`,
    method: 'delete'
  })
}

// 查询所有可用角色
export function selectAllRole() {
  return request({
    url: '/system/role/selectAllRole',
    method: 'get'
  })
}
// 根据用户ID查询用户拥有的角色IDS
export function getRoleIdsByUserId(userId) {
  return request({
    url: '/system/role/getRoleIdsByUserId/' + userId,
    method: 'get'
  })
}

// 保存角色和用户之间的关系
export function saveRoleUser(userId, roleIds) {
  // 处理如果没有选择角色数据。无法匹配后台数据的问题
  if (roleIds.length === 0) {
    roleIds = [-1]
  }
  return request({
    url: '/system/role/saveRoleUser/' + userId + '/' + roleIds,
    method: 'post'
  })
}
