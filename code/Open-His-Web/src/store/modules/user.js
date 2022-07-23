import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  roles: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  // user login 这个login是方法声明，提供给store的
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      // 这里调用 api/user.login 接口
      login({ username: username.trim(), password: password }).then(response => {
        // 接口响应成功，返回data数据，把token设置到缓存中，回调执行resolve
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        // 反之，携带异常参数执行异常回调函数
        reject(error)
      })
    })
  },

  // get user info 获取用户信息
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        // 1、响应成功，获取data数据
        const { data } = response

        // 2、如果数据不存在，回调异常方法，入参提示信息
        // if (!data) return reject('Verification failed, please Login again.')
        if (!data) return reject('验证失败，请重新登录！')

        // 3、解构响应数据，获取角色。名称，头像，介绍三个对象
        // const { roles, name, avatar, introduction } = data
        const { roles, name, avatar } = data

        // 4、角色列表不能为空 roles must be a non-empty array
        // if (!roles || roles.length <= 0) reject('getInfo: roles must be a non-null array!')
        if (!roles || roles.length <= 0) reject(`用户:${name} 没有分配任何角色！`)

        commit('SET_ROLES', roles)
        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        // commit('SET_INTRODUCTION', introduction) 因为没有项目没有这个对象，所以不设置
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout 用户退出操作
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      // 调用api的logout接口，然后不需要获取响应数据
      logout(state.token).then(() => {
        // 清空token和角色列表
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        removeToken()
        // 把路由对象进行重置处理
        resetRouter()

        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, { root: true })

        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
