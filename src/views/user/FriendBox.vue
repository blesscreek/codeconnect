<script setup>
import { useRouter } from 'vue-router'
import { useChatStore } from '@/stores'

const router = useRouter()
const chatStore = useChatStore()

const props = defineProps({
  friendInfo: Object
})

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
</script>
<template>
  <div class="friendContent">
    <div class="fridneLeft">
      <div class="headPic">
        <head-sculpture :url="props.friendInfo.headImage"></head-sculpture>
      </div>
      <div class="info">
        <div class="nickname">{{ props.friendInfo.nickname }}</div>
        <!-- <div class="nickname">{{ props.friendInfo.nickname }}</div> -->
      </div>
    </div>
    <div class="friendRight">
      <el-button @click="handleSendMessage">发消息</el-button>
      <el-button>已关注</el-button>
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
      .nickname {
        font-size: 18px;
        margin-left: 20px;
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
  }
}
</style>
