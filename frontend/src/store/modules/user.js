import { login, logout, getInfo, loginWithMobileCode } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    roles: [],
    comId: '',
    lastLoginIp: '',
    lastLoginTime: '',
    permissions: [],
    email: '',
    phone: ''
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
    SET_COMID: (state, comId) => {
      state.comId = comId
    },
    SET_IP: (state, ip) => {
      state.lastLoginIp = ip
    },
    SET_TIME: (state, time) => {
      state.lastLoginTime = time
    },
    SET_EMAIL: (state, email) => {
      state.email = email
    },
    SET_PHONE: (state, phone) => {
      state.phone = phone
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      if (userInfo.password) {
        const username = userInfo.username.trim()
        const password = userInfo.password
        const code = userInfo.code
        const uuid = userInfo.uuid
        return new Promise((resolve, reject) => {
          login(username, password, code, uuid).then(res => {
            setToken(res.token)
            commit('SET_TOKEN', res.token)
            resolve()
          }).catch(error => {
            reject(error)
          })
        })
      } else {
        return new Promise((resolve, reject) => {
          loginWithMobileCode(userInfo).then(res => {
            setToken(res.token)
            commit('SET_TOKEN', res.token)
            resolve()
          }).catch(error => {
            reject(error)
          })
        })

      }
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo(state.token).then(res => {
          const user = res.user
          const avatar = user.avatar == "" ? require("@/assets/image/profile.jpg") : process.env.VUE_APP_BASE_API + user.avatar;
          if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', res.roles)
            commit('SET_PERMISSIONS', res.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_NAME', user.userName)
          commit('SET_COMID', user.comId)
          commit('SET_AVATAR', avatar)
          commit('SET_IP', user.loginIp)
          commit('SET_TIME', user.loginDate)
          commit('SET_EMAIL', user.emial)
          commit('SET_PHONE', user.phonenumber)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
