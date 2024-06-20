<!-- 好友盒子，渲染好友列表的 -->
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useChatStore } from '@/stores'
import {
  getUserInfoService,
  deleteFriendService,
  addFriendService
} from '@/api/user.js'

const router = useRouter()
const chatStore = useChatStore()

const props = defineProps({
  friendInfo: Object
})

// 发消息
const handleSendMessage = () => {
  const user = props.friendInfo
  const chat = {
    type: 'PRIVATE',
    targetId: user.friendId,
    showName: user.nickname,
    headImage: user.headImage
  }
  chatStore.openChat(chat)
  chatStore.activeChat(0)
  router.replace('/chat/friend')
}
// 关注状态
const state = ref('')
const setState = (user) => {
  if (!user.isConcern) {
    state.value = '关注'
  } else {
    if (!user.isFans) {
      state.value = '已关注'
    } else {
      state.value = '已互关'
    }
  }
}
setState(props.friendInfo)
// 改变关注状态
const changeState = async () => {
  // 关注->取关  没关注->关注
  if (state.value == '关注') {
    // 关注
    await addFriendService(props.friendInfo.friendId)
    ElMessage.success('关注成功！')
  } else {
    // 删除
    await deleteFriendService(props.friendInfo.friendId)
    ElMessage.success('取关成功！')
  }
  // 改变按钮状态
  const res = await getUserInfoService(props.friendInfo.friendId)
  setState(res.data)
}
</script>
<template>
  <div class="friendContent">
    <div class="fridneLeft">
      <div class="headPic">
        <head-sculpture :url="props.friendInfo.headImage"></head-sculpture>
      </div>
      <div class="info">
        <div class="nickname">{{ props.friendInfo.nickname }}</div>
        <div class="signature">
          {{
            props.friendInfo.signature
              ? props.friendInfo.signature
              : '这个人很神秘~'
          }}
        </div>
      </div>
    </div>
    <div class="friendRight">
      <el-button @click="handleSendMessage">发消息</el-button>
      <el-button @click="changeState">{{ state }}</el-button>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.friendContent {
  width: 980px;
  height: 80px;
  margin-left: 20px;
  box-sizing: border-box;
  border-bottom: 1px solid #dadada;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  .fridneLeft {
    width: 600px;
    display: flex;
    .headPic {
      width: 55px;
      height: 55px;
      margin-left: 20px;
    }
    .info {
      height: 55px;
      margin-left: 20px;
      .nickname {
        font-size: 18px;
        height: 35px;
      }
      .signature {
        font-size: 14px;
        color: #707070;
      }
    }
  }
  .friendRight {
    width: 330px;
    height: 100%;
    margin-right: 50px;
    display: flex;
    justify-content: right;
    align-items: center;
    .el-button {
      width: 73px;
    }
  }
}
</style>
