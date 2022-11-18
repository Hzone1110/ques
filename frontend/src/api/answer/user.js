import request from '@/utils/request'

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/answer/user/list',
    method: 'get',
    params: query
  })
}

// 智能答者标签分析按钮接口
export function allotTag() {
  return request({
    url: '/answer/user/allotTag',
    method: 'put'
  })
}