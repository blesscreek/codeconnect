<script setup>
import { computed, ref, watch } from 'vue'
import { useChatStore } from '@/stores'
import { useRouter } from 'vue-router'
import {
  addFriendService,
  deleteFriendService,
  getUserInfoService
} from '@/api/user'

const chatStore = useChatStore()
const router = useRouter()

// 好友名片
const props = defineProps({
  data: Object
})
const userInfo = computed(() => {
  if (!props.data.data) return {}
  return props.data.data
})

// 发消息
const handleSendMessage = () => {
  const user = userInfo
  const chat = {
    type: 'PRIVATE',
    targetId: user.value.id,
    showName: user.value.nickname,
    headImage: user.value.headImage
  }
  chatStore.openChat(chat)
  chatStore.activeChat(0)
  router.replace('/chat/friend')
}

// 加关注
const show = ref('')
const setShow = (user) => {
  if (user.isConcern) {
    console.log(user.isConcern, user.isFans)
    if (user.isFans) {
      show.value = '已互关'
    } else {
      show.value = '已关注'
    }
  } else {
    show.value = '加关注'
  }
}
watch(
  userInfo,
  () => {
    setShow(userInfo.value)
  },
  { deep: true }
)
const changeState = async () => {
  if (show.value == '加关注') {
    // 关注
    await addFriendService(userInfo.value.id)
    ElMessage.success('关注成功！')
  } else {
    // 删除
    await deleteFriendService(userInfo.value.id)
    ElMessage.success('取关成功！')
  }
  // 改变按钮状态
  const res = await getUserInfoService(userInfo.value.id)
  console.log(res.data)
  setShow(res.data)
}
</script>
<template>
  <div
    class="box"
    :style="{ left: props.data.clientX + 'px', top: props.data.clientY + 'px' }"
  >
    <div class="content">
      <div class="left">
        <head-sculpture
          :url="userInfo.headImageThumb || '@/assets/head-sculpture.jpg'"
        ></head-sculpture>
      </div>
      <div class="right">
        <div class="span">昵称：{{ userInfo.nickname || '该用户不存在' }}</div>
        <div class="span">性别：{{ userInfo.sex ? '女' : '男' || '' }}</div>
        <div class="span">年龄：{{ userInfo.age || '0' }}</div>
        <div class="span">
          个性签名：{{ userInfo.signature || '这个人很神秘哦~' }}
        </div>
      </div>
    </div>
    <div class="button">
      <el-button type="primary" @click="handleSendMessage">发消息</el-button>
      <el-button type="primary" @click="changeState">{{ show }}</el-button>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.box {
  position: fixed;
  top: 200px;
  left: 300px;

  width: 430px;
  height: 230px;
  background-color: #fff;
  border: 1px solid #cfcfcf;
}
.content {
  height: 160px;
  box-sizing: border-box;
  padding: 5px;
  border-bottom: 1px solid #cfcfcf;
  display: flex;
  .left {
    width: 110px;
    height: 100%;
    .head-sculpture {
      width: 80px;
      height: 80px;
      margin-left: 7px;
      margin-top: 7px;
    }
  }
  .right {
    width: 320px;
    height: 100%;
    .span {
      font-size: 16px;
      width: 100%;
      height: 30px;
      line-height: 30px;
    }
  }
  .button {
    width: 100%;
    height: 70px;
    display: flex;
    justify-content: center;
    align-items: center;
    .el-button {
      width: 125px;
      height: 40px;
      margin: 0 25px;
      font-size: 16px;
    }
  }
}
</style>
