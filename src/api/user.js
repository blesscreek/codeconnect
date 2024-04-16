import request from '@/utils/request'
export const userLoginService = ({ account, password }) => {
  console.log({ account, password })
  return request.post('/user/login', { account, password })
}
export const userRegisterService = (obj) => {
  return request.post('/user/register', obj)
}
