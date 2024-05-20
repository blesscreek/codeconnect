import request from '@/utils/request'
export const addTopicPictureService = (data) => {
  return request.post('/uploadQuestion/uploadImage', data)
}
export const addTopicService = (data) => {
  return request.post('/question/addQuestion', data)
}

export const getQuestionListService = ({ pageNo, pageSize }, data) => {
  return request.post(
    `/question/getQuestionList?pageNo=${pageNo}&pageSize=${pageSize}`,
    data
  )
}
export const getQuestionService = (params) => {
  return request.get(`/question/getQuestion?qid=${params}`)
}

// 上传题目
export const submitJudgeQuestion = (data) => {
  return request.post('/judge/submitJudgeQuestion', data)
}
