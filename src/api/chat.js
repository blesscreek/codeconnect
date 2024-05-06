import request from '@/utils/request'

// 拉取好友私聊记录
export const getPrivateHistoryServe = (params) => {
  return request.get('/message/private/history', {
    params: params
  })
}

// 查询群聊
export const findGroupServe = (id) => {
  return request.get(`/group/find/${id}`)
}

// 拉取群聊记录
export const getGroupHistoryServe = (params) => {
  return request.get('/message/group/history', {
    params: params
  })
}

// 获取聊天室成员
export const getGroupMemberServe = (id) => {
  return request.get(`/group/members/${id}`)
}

// 发消息
// 发送消息
export const sendPrivateMessageServe = (data) => {
  return request.post('/message/private/send', data)
}
// 群聊发送消息
export const sendGroupMessageServe = (data) => {
  return request.post('/message/group/send', data)
}

// 拉取未读私信消息
export const pullPrivateUnreadServe = () => {
  return request.post('/message/private/pullUnreadMessage')
}
// 拉取群聊未读私信消息
export const pullGroupUnreadServe = () => {
  return request.post('/message/group/pullUnreadMessage')
}
