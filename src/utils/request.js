import axios from 'axios'
import { useUserStore } from '@/stores'
import router from '@/router'
import { ElMessage } from 'element-plus'

const baseURL = 'http://123.60.15.140:8080'
// const baseURL = 'http://big-event-vue-api-t.itheima.net'

const request = axios.create({
  // TODO 1. 基础地址，超时时间
  baseURL,
  timeout: 10000
})

request.interceptors.request.use(
  (config) => {
    // TODO 2. 携带token
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = userStore.token
    }
    return config
  },
  (err) => Promise.reject(err)
)

request.interceptors.response.use(
  (res) => {
    // TODO 3. 处理业务失败
    if (res.data.code === 0) return res
    // ElMessage({ message: res.data.message || '服务异常', type: 'error' })
    // TODO 4. 摘取核心响应数据
    // return Promise.reject(res.data)
    return Promise.reject(res)
  },
  (err) => {
    // TODO 5. 处理401错误
    ElMessage({
      message: err.response.data.message || '服务异常',
      type: 'error'
    })
    if (err.response?.status === 401) router.push('login')
    return Promise.reject(err)
  }
)

export default request
export { baseURL }
