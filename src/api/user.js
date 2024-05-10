import request from '@/utils/request'
// oj的
// export const userLoginService = ({ account, password }) => {
//   console.log({ account, password })
//   return request.post('/user/login', { account, password })
// }

// 聊天室的
export const userLoginService = ({ account: username, password }) => {
  return request.post('/login', { username, password })
}
export const userRegisterService = (obj) => {
  return request.post('/user/register', obj)
}

// 查询自己的信息
export const userInfoService = () => {
  return request.get('/user/self')
}

// 修改个人信息
export const userUpdateService = (data) => {
  // const
  return request.put('/user/update', data)
}

// 更新token
export const refreshTokenService = () => {
  return request.put('/refreshToken')
}

// 获取互关列表
export const getFriendListService = () => {
  return request.get('/friend/list')
}
// 获取关注列表
export const getConcernsListService = () => {
  return request.get('/friend/concerns')
}
// 获取粉丝列表
export const getFansListService = () => {
  return request.get('/friend/fans')
}
// 查找用户信息
export const getUserInfoService = (id) => {
  return request.get(`/user/find/${id}`)
}
// 查找群聊信息
export const getGroupInfoService = (id) => {
  return request.get(`/group/find/${id}`)
}
