<script setup>
// element-plus 中文全局
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

// 在这里整websocket？
import { useUserStore, useFriendStore, useChatStore } from './stores'
import { watch, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  createWebSocket,
  onopen,
  onmessage,
  closeWebSocket
} from '@/utils/websocket'
import { pullPrivateUnreadServe, pullGroupUnreadServe } from '@/api/chat.js'
import { getUserInfoService } from '@/api/user.js'

const userStore = useUserStore()
const friendStore = useFriendStore()
const chatStore = useChatStore()
const router = useRouter()
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
  await friendStore.getFriendList() // 好友
  await friendStore.getFansList() // 粉丝
  await friendStore.getConcernsList() // 关注
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
      handlePrivateMessage(msgInfo)
    } else if (cmd == 4) {
      // 插入群聊消息
      handleGroupMessage(msgInfo)
    }
  })
}
// 拉取未读私信，告诉后端要获得未读消息了，前端通过websocket获取未读消息
const pullUnreadMessage = () => {
  pullPrivateUnreadServe()
  pullGroupUnreadServe()
}
// 插入消息  应该用不上插入消息，因为直接获取的是历史消息
const handlePrivateMessage = async (msg) => {
  // 好友列表不存在好友信息，通过id获取信息，好友也可以这么写？
  const res = await getUserInfoService(msg.sendId)
  let user = res.data
  // 要放到消息列表里了
  if (msg.type < 0 || msg.type > 2) return
  let chatInfo = {
    type: 'PRIVATE',
    targetId: user.id,
    showName: user.nickname,
    headImage: user.headImageThumb
  }
  // 打开会话
  chatStore.getNewChat(chatInfo)
  // 插入消息
  chatStore.insertMessage(msg)
}
const handleGroupMessage = (msg) => {
  if (msg.groupId != 16) return
  if (msg.type < 0 || msg.type > 2) return
  chatStore.insertGroupMessage(msg)
}
</script>

<template>
  <el-config-provider :locale="zhCn">
    <router-view></router-view>
  </el-config-provider>
</template>

<style scoped></style>
