import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
  getFriendListService,
  getConcernsListService,
  getFansListService
} from '@/api/user.js'
// 用户模块 token相关
export const useFriendStore = defineStore(
  'oj-friend',
  () => {
    const friendList = ref([]) // 互关列表
    const fansList = ref([]) // 粉丝列表
    const concernsList = ref([]) // 关注列表
    const getFriendList = async () => {
      const res = await getFriendListService()
      friendList.value = res.data ? res.data : []
    }
    const getFansList = async () => {
      const res = await getFansListService()
      fansList.value = res.data ? res.data : []
    }
    const getConcernsList = async () => {
      const res = await getConcernsListService()
      concernsList.value = res.data ? res.data : []
    }
    return {
      friendList,
      fansList,
      concernsList,
      getFriendList,
      getFansList,
      getConcernsList
    }
  },
  {
    persist: true // 持久化
  }
)
