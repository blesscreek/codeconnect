import request from '@/utils/request'
const baseURL = 'http://123.60.15.140:8080'
// const baseURL = 'http://139.9.136.188:8080'
// 增加题目图片
export const addTopicPictureService = (data) => {
  return request.post(baseURL + '/uploadQuestion/uploadImage', data)
}
// 增加啊题目
export const addTopicService = (data) => {
  return request.post(baseURL + '/question/addQuestion', data)
}
// 获取题目列表 分页
export const getQuestionListService = ({ pageNo, pageSize }, data) => {
  return request.post(
    baseURL + `/question/getQuestionList?pageNo=${pageNo}&pageSize=${pageSize}`,
    data
  )
}
// 获取题目详情
export const getQuestionService = (params) => {
  return request.get(baseURL + `/question/getQuestion?qid=${params}`)
}

// 上传题目
export const submitJudgeQuestion = (data) => {
  return request.post(baseURL + '/judge/submitJudgeQuestion', data)
}
