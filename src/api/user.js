import request from '@/utils/request'
export const userLoginService = (account, password) => {
  return request.post('/user/login', { account, password })
}
