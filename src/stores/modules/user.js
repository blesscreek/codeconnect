import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userInfoService } from '@/api/user.js'
// 用户模块 token相关
export const useUserStore = defineStore(
  'oj-user',
  () => {
    // token相关
    const token = ref('')
    const refreshToken = ref('')
    const setToken = (newToken) => {
      token.value = newToken
    }
    const setRefreshToken = (newRefreshToken) => {
      refreshToken.value = newRefreshToken
    }

    // 记住我相关
    const remember = ref(false)
    const username = ref({})
    const setRemember = (val) => {
      remember.value = val
    }
    const setUsername = (obj) => {
      username.value = obj
    }

    // 用户信息相关
    // 携带token后直接向后端发请求获得
    const userInfo = ref({})
    const setUserInfo = (obj) => {
      userInfo.value = obj
    }
    // 获取用户信息，感觉是多次使用的，不如直接封装
    const getUserInfoServer = async () => {
      const userInfo = await userInfoService()
      setUserInfo(userInfo.data)
    }

    // 用户登出
    const removeUser = () => {
      userInfo.value = {}
      token.value = ''
      refreshToken.value = ''
    }

    return {
      token,
      remember,
      username,
      refreshToken,
      userInfo,
      setUserInfo,
      setRefreshToken,
      setToken,
      setRemember,
      setUsername,
      getUserInfoServer,
      removeUser
    }
  },
  {
    persist: true // 持久化
  }
)
