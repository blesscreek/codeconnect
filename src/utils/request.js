import axios from 'axios'
import { useUserStore } from '@/stores'
import router from '@/router'
import { ElMessage } from 'element-plus'
import { refreshTokenService } from '@/api/user'

// const baseURL = 'http://192.168.0.190:8080'
const baseURL = 'http://139.9.136.188:8080'

const request = axios.create({
  // TODO 1. 基础地址，超时时间
  baseURL,
  timeout: 100000
})

// TODO 2. 携带token  请求拦截器
request.interceptors.request.use(
  (config) => {
    // config.headers = undefined
    const userStore = useUserStore()
    if (userStore.token) {
      // oj的
      // config.headers.Authorization = userStore.token
      // 聊天室
      config.headers.accessToken = userStore.token
      config.headers.refreshToken = userStore.refreshToken //长token
    }
    if (config.data instanceof FormData) {
      console.log('*')
      config.headers['Content-Type'] = 'multipart/form-data'
    } else {
      config.headers['Content-Type'] = 'application/json'
    }
    return config
  },
  (err) => Promise.reject(err)
)

// 响应拦截器
request.interceptors.response.use(
  async (res) => {
    const userStore = useUserStore()
    // TODO 3. 处理业务失败
    // 成功是直接返回数据
    if (res.data.code === 200) return res.data
    if (res.data.code === 400) {
      return router.replace('/login')
    }
    if (res.data.code === 401) {
      console.log('token失效，尝试重新获取')
      let refreshToken = userStore.refreshToken
      if (!refreshToken) {
        router.replace('/login')
      }
      // 发送请求, 进行刷新token操作, 获取新的token
      const newToken = await refreshTokenService()
      console.log(newToken.data)
      // 保存token
      userStore.setToken(newToken.data.accessToken)
      userStore.setRefreshToken(newToken.data.refreshToken)
      // 重新发送刚才的请求
      return request(res.config)
    }
    // 失败时
    ElMessage({ message: res.data.message || '服务异常', type: 'error' })
    // TODO 4. 摘取核心响应数据
    return Promise.reject(res.data)
  },
  (err) => {
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
