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
    const removeToken = () => {
      token.value = ''
    }
    const setRefreshToken = (newRefreshToken) => {
      refreshToken.value = newRefreshToken
    }

    // 记住我相关
    const remember = ref(false)
    const account = ref({})
    const setRemember = (val) => {
      remember.value = val
    }
    const setAccount = (obj) => {
      account.value = obj
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
      // console.log(userInfo)
      setUserInfo(userInfo.data)
    }

    return {
      token,
      remember,
      account,
      refreshToken,
      userInfo,
      setUserInfo,
      setRefreshToken,
      setToken,
      removeToken,
      setRemember,
      setAccount,
      getUserInfoServer
    }
  },
  {
    persist: true // 持久化
  }
)
