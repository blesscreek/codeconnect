<script setup>
// element-plus 中文全局
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

// 在这里整websocket？
import { useUserStore } from './stores'
import { watch, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  createWebSocket,
  onopen,
  onmessage,
  closeWebSocket
} from '@/utils/websocket'
import { pullPrivateUnreadServe, pullGroupUnreadServe } from '@/api/chat.js'
import { useChatStore } from '@/stores'
const chatStore = useChatStore()
const router = useRouter()
const userStore = useUserStore()
const info = computed(() => userStore.userInfo)
// 监听用户数据的变化
watch(
  info,
  (val) => {
    if (val.id) websocket()
    else closeWebSocket()
  },
  {
    deep: true
  }
)
onMounted(() => {
  if (info.value.id) websocket()
})

// websocket
const websocket = async () => {
  // 连接websocket
  await createWebSocket('ws://139.9.136.188:8082/im', userStore.userInfo.id)
  await onopen(pullUnreadMessage)
  await onmessage((cmd, msgInfo) => {
    if (cmd == 2) {
      // 异地登录，强制下线
      ElMessage.error('您已在其他地方登陆，将被强制下线')
      setTimeout(() => {
        router.push('/login')
      }, 1000)
    }
    // 未读消息都是通过这里拿到的
    else if (cmd == 3) {
      // 插入私聊消息
      console.log(msgInfo)
      // handlePrivateMessage(msgInfo)
    } else if (cmd == 4) {
      // 插入群聊消息
      console.log(msgInfo)
      // handleGroupMessage(msgInfo)
    }
  })
}
// 拉取未读私信，好像没有用到
const pullUnreadMessage = () => {
  pullPrivateUnreadServe()
  pullGroupUnreadServe()
}
// 插入消息  应该用不上插入消息，因为直接获取的是历史消息
const handlePrivateMessage = (msg) => {
  // 好友列表存在好友信息，直接插入私聊消息
  // let friend = this.$store.state.friendStore.friends.find((f) => {
  //   return f.friendId == msg.sendId
  // })
  // if (friend) {
  //   insertPrivateMessage(friend, msg)
  //   return
  // }
  // 好友列表不存在好友信息，再想想逻辑
}
const handleGroupMessage = () => {}
const insertPrivateMessage = (friend, msg) => {
  if (msg.type < 0 || msg.type > 2) return
  let chatInfo = {
    type: 'PRIVATE',
    targetId: friend.friendId,
    showName: friend.nickname,
    headImage: friend.headImage
  }

  // 打开会话
  chatStore.openChat(chatInfo)
  // 插入消息
  // chatStore.insertMessage(msg)
}
</script>

<template>
  <el-config-provider :locale="zhCn">
    <router-view></router-view>
  </el-config-provider>
</template>

<style scoped></style>
