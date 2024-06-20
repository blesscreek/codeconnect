<!-- 聊天界面显示聊天记录和发消息 -->
<script setup>
import { ref, computed, watch, nextTick } from 'vue'
import {
  getPrivateHistoryServe,
  sendPrivateMessageServe,
  getGroupHistoryServe,
  sendGroupMessageServe
} from '@/api/chat.js'
import { uploadImageService, uploadFileService } from '@/api/updata.js'
import { useUserStore, useChatStore } from '@/stores'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'

// 从父组件拿到发消息对象的名字
const props = defineProps({
  chatInfo: Object,
  groupMembers: Object
})

const userStore = useUserStore()
const chatStore = useChatStore()
const route = useRoute()
const path = computed(() => {
  return route.path.split('/')[2]
})
const length = computed(() => {
  return chatStore.chats.length
})
const show = computed(() => {
  return path.value == 'chatroom' || length.value != 0
})
watch(show, () => {
  console.log(show)
})
const MessageList = ref([]) //消息列表
const page = ref(1) //消息的页数
// 滚动条事件
const nowHight = ref(0) //当前内容高度
const scroll = ref(null)
const message = ref() //消息容器
const isScroll = ref(false)
// 到顶加载历史记录
const getScroll = async ({ scrollTop }) => {
  if (scrollTop == 0) {
    if (!isScroll.value) return
    loading.value = true
    page.value = page.value + 1
    const list = await getMessageInfo(page.value)
    // 将聊天记录加到列表
    list.forEach((x) => {
      MessageList.value.unshift(x)
    })
    loading.value = false
    // 滚动条到底部
    nextTick(() => {
      scroll.value.setScrollTop(
        message.value.clientHeight - nowHight.value - 40
      )
      nowHight.value = message.value.clientHeight
    })
  }
  isScroll.value = true
}
const loading = ref(false)
const mine = computed(() => {
  return userStore.userInfo
})

// 判断拿到的是自己的头像还是对方的头像
const headImage = (msgInfo) => {
  if (props.chatInfo.type == 'GROUP') {
    let member
    for (let m of props.groupMembers) {
      if (m.memberId == msgInfo.sendId) {
        member = m
        break
      }
    }
    return member ? member.headImage : ''
  }
  return msgInfo.sendId == mine.value.id
    ? mine.value.headImageThumb
    : props.chatInfo.headImage
}
const showName = (msgInfo) => {
  if (props.chatInfo.type == 'GROUP') {
    let member
    for (let m of props.groupMembers) {
      if (m.memberId == msgInfo.sendId) {
        member = m
        break
      }
    }
    return member ? member.memberNickname : ''
  }
  return msgInfo.sendId == mine.value.id
    ? mine.value.nickname
    : props.chatInfo.showName
}

// 拿到当前发消息对象的历史消息  判断一下是不是群聊
const getMessageInfo = async (page) => {
  let res = {}
  if (!props.chatInfo) return []
  if (props.chatInfo.type == 'GROUP') {
    chatStore.groupMsg.unreadCount = 0
    res = await getGroupHistoryServe({
      groupId: props.chatInfo.targetId,
      page: page,
      size: 10
    })
  } else if (props.chatInfo.type == 'PRIVATE') {
    res = await getPrivateHistoryServe({
      friendId: props.chatInfo.targetId,
      page: page,
      size: 10
    })
  } else {
    return []
  }
  if (res.data.length == 0) {
    ElMessage.error('没有更多聊天记录了哦~')
  }
  return res.data
}

// 点击或刷新重新获得聊天记录
const getMessage = async () => {
  page.value = 1
  if (path.value == 'friend') {
    if (length.value) chatStore.chats[chatStore.activeIndex].unreadCount = 0 //将未读清零
  }
  // 将消息列表清零
  isScroll.value = false //重新获取页面时滚动条触顶不加载 不能放在nextTick后写，否则没有效果
  MessageList.value = []
  const list = await getMessageInfo(page.value)
  // 将聊天记录加到列表
  list.forEach((x) => {
    MessageList.value.unshift(x)
  })
  // 加载最近一条消息
  if (path.value == 'friend') {
    let msgInfo = MessageList.value[MessageList.value.length - 1]
    let chat = chatStore.chats[chatStore.activeIndex]
    if (msgInfo.type == 1) {
      chat.lastContent = '[图片]'
    } else if (msgInfo.type == 2) {
      chat.lastContent = '[文件]'
    } else {
      chat.lastContent = msgInfo.content
    }
  }
  // 滚动条到底部
  nextTick(() => {
    if (!scroll.value) return
    scroll.value.setScrollTop(message.value.clientHeight)
    nowHight.value = message.value.clientHeight
  })
}
getMessage()
//监听对象变化
const chatInfo = computed(() => {
  return props.chatInfo
})
watch(chatInfo, () => {
  getMessage()
})

// 收到新消息，加到消息的后面，滚动条拉到底，点击后清除未读消息，从历史拉取消息记录
const newMsg = computed(() => {
  if (!length.value) return []
  return chatStore.chats[chatStore.activeIndex].messages
})
// 监听消息列表有没有变化
watch(
  newMsg,
  () => {
    if (length.value == 0) return
    while (chatStore.chats[chatStore.activeIndex].messages.length) {
      let x = chatStore.chats[chatStore.activeIndex].messages.shift()
      MessageList.value.push(x)
    }
    nextTick(() => {
      scroll.value.setScrollTop(message.value.clientHeight)
      nowHight.value = message.value.clientHeight
    })
  },
  { deep: true }
)

// 收到群聊消息
const newGroupMsg = computed(() => {
  return chatStore.groupMsg.messages
})
watch(
  newGroupMsg,
  () => {
    // 加载群聊信息
    if (path.value != 'chatroom') return
    chatStore.groupMsg.unreadCount = 0
    while (chatStore.groupMsg.messages.length) {
      MessageList.value.push(chatStore.groupMsg.messages.shift())
    }
    nextTick(() => {
      scroll.value.setScrollTop(message.value.clientHeight)
      nowHight.value = message.value.clientHeight
    })
  },
  {
    deep: true
  }
)

// emoji框相关
const showEmotion = ref(false)
const emoji = ref()
const emoBoxPos = ref({ x: 0, y: 0 })
const switchEmotionBox = () => {
  showEmotion.value = !showEmotion.value
  const val = emoji.value.getBoundingClientRect()
  emoBoxPos.value.y = val.y
  emoBoxPos.value.x = val.x
}
// 点击某个表情以后
// 拿到富文本处理表情的函数
const editor = ref()
// 发消息的输入框用富文本编辑器，父组件调用子组件暴露的方法
const getEmotion = (val) => {
  editor.value.handleEmoji(val)
  showEmotion.value = false
}

// 输入框的值
const textValue = ref('')
// 拿到富文本的值
const getText = (val) => {
  textValue.value = val
}
// 发送文字消息
const sendMessage = async () => {
  if (!textValue.value) return ElMessage.error('消息内容不能为空')
  let text = textValue.value
  textValue.value = ''
  editor.value.afterUpdata() //清空富文本框

  if (props.chatInfo.type == 'GROUP') {
    await sendGroupMessageServe({
      groupId: props.chatInfo.targetId,
      content: text,
      type: 0
    })
  } else {
    await sendPrivateMessageServe({
      receiveId: props.chatInfo.targetId,
      content: text,
      type: 0
    })
  }
  getMessage()
}

// 发送图片
const onSelectFile1 = async (uploadFile) => {
  const imgFile = ref({})
  const reader = new FileReader()
  // 存一下文件
  imgFile.value = uploadFile.raw
  reader.readAsDataURL(uploadFile.raw)
  const file = new FormData()
  file.append('file', imgFile.value)
  const img = await uploadImageService(file)
  // 拿到上传之后的url receiveId
  if (props.chatInfo.type == 'GROUP') {
    await sendGroupMessageServe({
      groupId: props.chatInfo.targetId,
      content: JSON.stringify({
        url: img.data.url,
        thumbUrl: img.data.thumbUrl
      }),
      type: 1
    })
  } else {
    await sendPrivateMessageServe({
      receiveId: props.chatInfo.targetId,
      content: JSON.stringify({
        url: img.data.url,
        thumbUrl: img.data.thumbUrl
      }),
      type: 1
    })
  }
  getMessage()
}
// 发送文件
const onSelectFile2 = async (uploadFile) => {
  const File = ref({})
  const reader = new FileReader()
  // 存一下文件
  File.value = uploadFile.raw
  reader.readAsDataURL(uploadFile.raw)
  const file = new FormData()
  file.append('file', File.value)
  const fileData = await uploadFileService(file)
  // data里只有url
  if (props.chatInfo.type == 'GROUP') {
    await sendGroupMessageServe({
      groupId: props.chatInfo.targetId,
      content: JSON.stringify({
        name: File.value.name,
        size: File.value.size,
        url: fileData.data
      }),
      type: 2
    })
  } else {
    await sendPrivateMessageServe({
      receiveId: props.chatInfo.targetId,
      content: JSON.stringify({
        name: File.value.name,
        size: File.value.size,
        url: fileData.data
      }),
      type: 2
    })
  }
  getMessage()
}

// 鼠标右键菜单,通知父组件
const emit = defineEmits(['setContextmenu', 'openVisitingCard'])
const getContextmenu = (obj) => {
  emit('setContextmenu', obj)
}
const openVisitingCard = (obj) => {
  emit('openVisitingCard', obj)
}
</script>

<template>
  <!-- 渲染聊天消息 -->
  <el-empty description="description" class="null" v-show="!show" />
  <div class="Mbox" v-show="show">
    <div class="head">
      <div>
        {{ props.chatInfo.showName ? props.chatInfo.showName : '聊天室' }}
      </div>
    </div>
    <div class="content">
      <el-scrollbar ref="scroll" @scroll="getScroll">
        <!-- 聊天列表 -->
        <div class="message" ref="message">
          <div class="loading" v-show="loading" v-loading="loading"></div>
          <chat-message-item
            v-for="(x, i) in MessageList"
            :key="i"
            :msgInfo="x"
            :mine="x.sendId == mine.id"
            :headImage="headImage(x)"
            :showName="showName(x)"
            @setContextmenu="getContextmenu"
            @openVisitingCard="openVisitingCard"
          ></chat-message-item>
        </div>
      </el-scrollbar>
    </div>
  </div>

  <!-- 发消息的输入框 -->
  <div class="Ibox" v-show="show">
    <div class="fun">
      <!-- emoji表情 -->
      <el-icon @click="switchEmotionBox">
        <img ref="emoji" src="@/assets/smile.svg" alt="" />
      </el-icon>
      <!-- 上传图片 -->
      <el-upload
        class="avatar-uploader"
        :show-file-list="false"
        :auto-upload="false"
        :on-change="onSelectFile1"
      >
        <el-icon>
          <img src="@/assets/picture.svg" alt="" />
        </el-icon>
      </el-upload>
      <!-- 上传文件 -->
      <el-upload
        class="avatar-uploader"
        :show-file-list="false"
        :auto-upload="false"
        :on-change="onSelectFile2"
      >
        <el-icon>
          <img ref="emoji" src="@/assets/document.svg" alt="" />
        </el-icon>
      </el-upload>
    </div>
    <!-- 富文本输入框 -->
    <div class="textarea">
      <input-editor ref="editor" @updateValue="getText"></input-editor>
    </div>

    <div class="footer">
      <el-button type="primary" @click="sendMessage">发送</el-button>
    </div>
    <emoji-content
      v-show="showEmotion"
      :pos="emoBoxPos"
      @getEmoji="getEmotion"
    ></emoji-content>
  </div>
</template>

<style lang="scss" scoped>
.null {
  width: 100%;
  height: 100%;
}
.Mbox {
  width: 100%;
  height: 73%;
  border-radius: 10px;
  background-color: #f3f3f3;
  .head {
    width: 100%;
    height: 60px;
    background-color: #fff;
    font-size: 19px;
    box-sizing: border-box;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  // 滚动条
  .scrollbar-demo-item {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 50px;
    margin: 10px;
    text-align: center;
    border-radius: 4px;
    background: var(--el-color-primary-light-9);
    color: var(--el-color-primary);
  }
  .content {
    padding: 5px;
    height: calc(100% - 70px); //支持计算
    overflow-y: auto;
    .message {
      width: calc(100% - 15px);
    }
    // loading相关样式在全局
  }
}
// 输入框
.Ibox {
  width: 100%;
  height: 27%;
  background-color: #f3f3f3;
  border-top: 1px solid #c7c7c7;
  box-sizing: border-box;

  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
}
.fun {
  width: 100%;
  padding-left: 20px;
  height: 18%;
  display: flex;
  align-items: center;
  .avatar-uploader {
    width: 45px;
    height: 30px;
  }
  .el-icon {
    width: 45px;
    height: 28px;
    margin-top: 2px;
    opacity: 60%;
    cursor: pointer;
  }
  .el-icon:hover {
    opacity: 80%;
  }
  img {
    width: 28px;
  }
}
.textarea {
  width: 100%;
  height: 59%;
  border: none;
  font-size: 20px;
  background-color: #f3f3f3;

  resize: none;
  outline: none;
  padding: 10px 30px;
  box-sizing: border-box;
  margin: 0;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  background-image: none;
}
.footer {
  height: 23%;
  width: 100%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  div {
    margin-right: 20px;
    color: #5c5c5c;
  }
  .el-button {
    max-height: 100%;
    margin-right: 20px;
  }
}
</style>
