<script setup>
import { ref } from 'vue'
import { CloseBold } from '@element-plus/icons-vue'
import { useChatStore } from '@/stores'
const props = defineProps({
  info: Object,
  index: Number
})
// 删除消息列表
const chatStore = useChatStore()
const show = ref(false)
const showDelete = () => {
  show.value = true
}
const leaveDelete = () => {
  show.value = false
}
const deleteChat = () => {
  chatStore.deleteChat(props.info.targetId)
}
</script>
<template>
  <!-- 左侧好友列表 -->
  <div class="box" @mouseenter="showDelete" @mouseleave="leaveDelete">
    <el-badge
      class="item"
      :value="props.info.unreadCount"
      :hidden="props.info.unreadCount == 0"
      :offset="[-10, 0]"
    >
      <div class="left">
        <head-sculpture :url="props.info.headImage"></head-sculpture>
      </div>
    </el-badge>
    <div class="right">
      <div class="content">
        <span class="name">{{ props.info.showName }}</span>
        <span class="FriendMessage" v-html="props.info.lastContent"> </span>
      </div>
    </div>
    <div class="delete" v-show="show" @click="deleteChat">
      <el-icon><CloseBold /></el-icon>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.box {
  position: relative;
  width: 215px;
  height: 80px;
  display: flex;
  align-items: center;

  .delete {
    position: absolute;
    right: 0;
    width: 40px;
    height: 80px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
.item {
  width: 50px;
  height: 50px;
  margin-left: 10px;
}
.left {
  width: 100%;
  height: 100%;
}
.right {
  width: 150px;
  height: 50px;
  margin-left: 5px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  padding-left: 5px;
  box-sizing: border-box;
  .content {
    height: 54px;
  }
  span {
    display: block;
    height: 27px;
  }
  .name {
    font-size: 17px;
  }
  .FriendMessage {
    height: 22px;
    line-height: 22px;
    font-size: 14px;
    color: #616161;
    overflow: hidden;
  }
}
</style>
