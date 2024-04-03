import { defineStore } from 'pinia'
import { ref } from 'vue'

// 用户模块 token相关
export const useUserStore = defineStore(
  'oj-user',
  () => {
    // token相关
    const token = ref('')
    const setToken = (newToken) => {
      token.value = newToken
    }
    const removeToken = () => {
      token.value = ''
    }

    // 记住我相关
    const remember = ref(false)
    const account = ref({})
    const getRemember = () => {
      return remember.value
    }
    const setRemember = (val) => {
      remember.value = val
    }
    const getAccount = () => {
      return account.value
    }
    const setAccount = (obj) => {
      account.value = obj
    }

    // 用户信息相关
    // 携带token后直接向后端发请求获得

    return {
      token,
      remember,
      account,
      setToken,
      removeToken,
      getAccount,
      getRemember,
      setRemember,
      setAccount
    }
  },
  {
    persist: true // 持久化
  }
)
