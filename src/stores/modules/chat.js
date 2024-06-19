import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
const route = useRoute()
// 用户模块 token相关
export const useChatStore = defineStore(
  'oj-chat',
  () => {
    const close = ref(true)
    const activeIndex = ref(-1) // 当前激活的窗口
    const chats = ref([]) //消息的列表
    // 群消息数据
    const groupMsg = ref({
      targetId: 16,
      type: 'GROUP',
      showName: 'learn together',
      headImage: '',
      lastContent: '', //？最后一条消息
      lastSendTime: new Date().getTime(), //？最后发消息的时间
      unreadCount: 0, //？未读的数量  点击会话，拉取历史记录的时候未读数为0
      messages: []
    })
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

    // 接收新消息
    const getNewChat = (chatInfo) => {
      let chat = null,
        aChatId = chats.value[activeIndex.value].targetId
      let i = 0 //遍历的id，用于移除存在的会话
      // 找到当前消息，并放在最上方
      let flag = 0
      for (let x of chats.value) {
        if (x.type == chatInfo.type && x.targetId === chatInfo.targetId) {
          chat = x
          if (chatInfo.targetId == chats.value[activeIndex.value].targetId) {
            flag = 1 // 如果收到消息的是当前会话
          }
          // 放置头部
          chats.value.splice(i, 1)
          chats.value.unshift(chat)
          break
        }
        i++
      }
      if (flag) activeChat(0)
      else {
        i = 0
        for (let x of chats.value) {
          if (x.targetId == aChatId) activeChat(i)
          i++
        }
      }
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
      }
    }

    // 激活当前窗口
    const activeChat = (index) => {
      console.log('activeChat')
      activeIndex.value = index
      // 会报错,因为该属性不存在
    }

    // 插入消息
    const insertMessage = (msgInfo) => {
      let type = msgInfo.groupId ? 'GROUP' : 'PRIVATE'

      let targetId = msgInfo.groupId
        ? msgInfo.groupId
        : msgInfo.selfSend
          ? msgInfo.receiveId
          : msgInfo.sendId
      let chat = null
      for (let x of chats.value) {
        if (x.type == type && x.targetId === targetId) {
          chat = x
          break
        }
      }
      // 插入新的数据
      if (msgInfo.type == 1) {
        chat.lastContent = '[图片]'
      } else if (msgInfo.type == 2) {
        chat.lastContent = '[文件]'
      } else {
        chat.lastContent = msgInfo.content
      }
      chat.lastSendTime = msgInfo.sendTime
      // 如果不是当前会话，未读加1
      chat.unreadCount++
      // 如果收到的消息来自当前激活窗口并且再私聊页面
      if (
        targetId == chats.value[activeIndex.value].targetId &&
        route.path.split('/')[2] == 'friend'
      ) {
        chat.unreadCount = 0
      }
      chat.messages.push(msgInfo)
    }

    // 处理群聊新消息
    const insertGroupMessage = (msgInfo) => {
      if (msgInfo.type == 1) {
        groupMsg.value.lastContent = '[图片]'
      } else if (msgInfo.type == 2) {
        groupMsg.value.lastContent = '[文件]'
      } else {
        groupMsg.value.lastContent = msgInfo.content
      }
      groupMsg.value.lastSendTime = msgInfo.sendTime
      // 如果不是当前会话，未读加1
      groupMsg.value.unreadCount++
      groupMsg.value.messages.push(msgInfo)
    }

    // 计算属性，计算未读消息
    const groupUnread = computed(() => {
      return groupMsg.value.unreadCount
    })
    const msgUnread = computed(() => {
      let ans = 0
      // if(Object.keys(chats.value).length === 0) return 0
      for (let x of chats.value) {
        ans += x.unreadCount
      }
      return ans
    })
    // 删除消息列表
    const deleteChat = (id) => {
      chats.value = chats.value.filter((x) => {
        return x.targetId != id
      })
    }
    const removeChat = ()=>{
      groupMsg.value={
        targetId: 16,
        type: 'GROUP',
        showName: 'learn together',
        headImage: '',
        lastContent: '', //？最后一条消息
        lastSendTime: new Date().getTime(), //？最后发消息的时间
        unreadCount: 0, //？未读的数量  点击会话，拉取历史记录的时候未读数为0
        messages: []
      }
      chats.value=[]
    }
    return {
      close,
      activeIndex,
      chats,
      groupMsg,
      groupUnread,
      msgUnread,
      openChat,
      activeChat,
      insertMessage,
      getNewChat,
      insertGroupMessage,
      deleteChat,
      removeChat
    }
  },
  {
    persist: true
  }
)
