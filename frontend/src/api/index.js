import request from '@/utils/request'

// 获取路由
export const getData = () => {
  return request({
    url: '/common/stat',
    method: 'get'
  })
}