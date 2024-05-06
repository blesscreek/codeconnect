import { defineStore } from 'pinia'
import { ref } from 'vue'
// import { userInfoService } from '@/api/chat.js'
// 用户模块 token相关
export const useChatStore = defineStore(
  'oj-chat',
  () => {
    const activeIndex = ref(-1) // 当前激活的窗口
    const chats = ref([]) //消息的列表
    // 打开消息
    const openChat = (chatInfo) => {
      let chat = null
      let activeChat = activeIndex.value >= 0 ? chats[activeIndex] : null
      let i = 0 //遍历的id，用于移除存在的会话
      // 找到当前消息，并放在最上方
      for (let x of chats.value) {
        if (x.type == chatInfo.type && x.targetId === chatInfo.targetId) {
          chat = x
          // 放置头部
          chats.value.splice(i, 1)
          chats.value.unshift(chat)
          break
        }
        i++
      }
      console.log(chats.value)

      // 创建会话 当消息列表里没有当前消息时
      if (chat == null) {
        chat = {
          targetId: chatInfo.targetId,
          type: chatInfo.type,
          showName: chatInfo.showName,
          headImage: chatInfo.headImage,
          lastContent: '', //？最后一条消息
          lastSendTime: new Date().getTime(), //？最后发消息的时间
          unreadCount: 0, //？未读的数量  点击会话，拉取历史记录的时候未读数为0
          messages: [] //消息列表  //在当前会话中时拉取，获得历史记录时清空
        }
        // 放到消息列表中
        chats.value.unshift(chat)
        console.log(chats.value)
      }

      // 选中会话保持不变 什么意思
      if (activeChat) {
        chats.value.forEach((chat, index) => {
          if (
            activeChat.type == chat.type &&
            activeChat.targetId == chat.targetId
          ) {
            activeIndex.value = index
          }
        })
      }
    }

    // 当前激活窗口
    const activeChat = (index) => {
      activeIndex.value = index
      // 不知道是干什么的，但是会报错
      // chats[index].unreadCount = 0 //让未读为0
    }

    return {
      activeIndex,
      chats,
      openChat,
      activeChat
    }
  },
  {
    persist: true
  }
)
