import request from '@/utils/request'
// 上传图片
export const uploadImageService = (file) => {
  return request.post('/upload/image', file)
}
// 上传文件
export const uploadFileService = (file) => {
  return request.post('/upload/file', file)
}
