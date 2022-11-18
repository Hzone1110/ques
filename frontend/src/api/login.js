import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/login',
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    method: 'get'
  })
}

export function getMobileCode(phone) {
  return request({
    url: '/getSmsCode',
    method: 'get',
    params: { mobile: phone }
  })
}


export function loginWithMobileCode(form){

  const data = {
    username: form.mobile,
    code: form.code
  }
  return request({
    url: '/smsLogin',
    method: 'post',
    data: data
  })
}


// 注册租户
export function registerCompany(data){
  return request({
    url: '/registerCompany',
    method: 'post',
    data: data
  })
}

// 注册答者
export function registerAnswerer(data){
  return request({
    url: '/registerAnswerer',
    method: 'post',
    data: data
  })
}


export function getResetCode(phone) {
  return request({
    url: '/getRePwdCode',
    method: 'get',
    params: { mobile: phone }
  })
}

export function resetPwd(data){
  return request({
    url: '/resetPwd',
    method: 'post',
    data: data
  })
}
