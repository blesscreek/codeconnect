import request from '@/utils/request'
const baseURL1 = 'http://123.60.15.140:8080'
const baseURL2 = 'http://139.9.136.188:8080'
// oj的
// export const userLoginService = ({ account, password }) => {
//   console.log({ account, password })
//   return request.post('/user/login', { account, password })
// }

// 聊天室的
export const userLoginService = ({ username, password }) => {
  return request.post(baseURL1 + '/login', { username, password })
}
export const userRegisterService = ({ username, password, checkPassword }) => {
  return request.post(baseURL2 + '/register', {
    username,
    password,
    checkPassword
  })
}

// 更新token
export const refreshTokenService = () => {
  return request.put(baseURL1 + '/refreshToken')
}

// 查询自己的信息
export const userInfoService = () => {
  return request.get(baseURL2 + '/user/self')
}

// 修改个人信息
export const userUpdateService = (data) => {
  // const
  return request.put(baseURL2 + '/user/update', data)
}

// 获取互关列表
export const getFriendListService = () => {
  return request.get(baseURL2 + '/friend/list')
}
// 获取关注列表
export const getConcernsListService = () => {
  return request.get(baseURL2 + '/friend/concerns')
}
// 获取粉丝列表
export const getFansListService = () => {
  return request.get(baseURL2 + '/friend/fans')
}
// 查找用户信息
export const getUserInfoService = (id) => {
  return request.get(baseURL2 + `/user/find/${id}`)
}
// 查找群聊信息
export const getGroupInfoService = (id) => {
  return request.get(baseURL2 + `/group/find/${id}`)
}
// 关注
export const addFriendService = (id) => {
  // return request.put(`/friend/add`, {
  //   params: {
  //     friendId: '' + id
  //   }
  // })
  return request.put(baseURL2 + `/friend/add?friendId=${id}`)
}
// 取消关注
export const deleteFriendService = (id) => {
  return request.delete(baseURL2 + `/friend/delete/${id}`)
}
