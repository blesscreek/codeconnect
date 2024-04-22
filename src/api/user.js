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

// 上传或更新头像
export const uploadImageService = (file) => {
  return request.post('/upload/image', file)
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

// 获取关注列表
export const getFriendListService = () => {
  return request.get('/friend/list')
}
