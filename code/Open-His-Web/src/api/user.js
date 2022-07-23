import request from '@/utils/request'

/**
 * 用户登陆接口
 * @param {*} data
 * @returns
 */
export function login(data) {
  return request({
    // url: '/vue-element-admin/user/login',
    url: '/login/doLogin',
    method: 'post',
    data
  })
}

/**
 * 获取当前登陆用户信息
 * @param {*} token
 * @returns
 */
export function getInfo(token) {
  return request({
    // url: '/vue-element-admin/user/info',
    url: '/login/getInfo',
    method: 'get',
    params: { token }
  })
}

/**
 * 退出系统
 * @returns
 */
export function logout() {
  return request({
    // url: '/vue-element-admin/user/logout',
    url: '/login/logout',
    method: 'get'
  })
}

/**
 * 获取该用户的菜单
 * @returns
 */
export function getSystemMenus() {
  return request({
    url: '/login/getMenus',
    method: 'get'
  })
}
