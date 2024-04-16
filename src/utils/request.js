import axios from 'axios'
import { useUserStore } from '@/stores'
import router from '@/router'
import { ElMessage } from 'element-plus'

const baseURL = 'http://192.168.0.190:8080'
// const baseURL = 'http://192.168.1.182:8080'

const request = axios.create({
  // TODO 1. 基础地址，超时时间
  baseURL,
  timeout: 100000
})

// TODO 2. 携带token
request.interceptors.request.use(
  (config) => {
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
    console.log(res)
    // TODO 3. 处理业务失败
    // 成功是直接返回数据
    if (res.data.code === 200) return res.data
    // 失败时
    ElMessage({ message: res.data.message || '服务异常', type: 'error' })
    // TODO 4. 摘取核心响应数据
    return Promise.reject(res.data)
  },
  (err) => {
    // TODO 5. 处理401错误
    ElMessage({
      message: err.response?.data.message || '服务异常',
      type: 'error'
    })
    if (err.response?.status === 401) router.push('login')
    return Promise.reject(err)
  }
)

export default request
export { baseURL }
