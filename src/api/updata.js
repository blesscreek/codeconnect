import request from '@/utils/request'
const baseURL1 = 'http://123.60.15.140:8080'
const baseURL2 = 'http://139.9.136.188:8080'
// 上传图片
export const uploadImageService = (file) => {
  return request.post(baseURL2 + '/upload/image', file)
}
// 上传文件
export const uploadFileService = (file) => {
  return request.post(baseURL2 + '/upload/file', file)
}
// 上传测试样例
export const uploadTestPoints = (file) => {
  return request.post(baseURL1 + '/uploadQuestion/uploadQuestionCase', file)
}
