<script setup>
import MembersList from './MembersList.vue'
import FriendsList from './FriendsList.vue'
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useChatStore } from '@/stores'

const route = useRoute()
const chatStore = useChatStore()

const listVal = ref(false)
let chats = chatStore.chats // 获取消息列表

// 好友还是用户列表
const listChange = () => {
  let path = route.path.split('/')[2]
  if (path == 'chatroom') {
    listVal.value = true
  } else {
    listVal.value = false
  }
}
listChange()
watch(route, () => {
  listChange()
})

// 点击其他消息列表时
const emit = defineEmits(['chatInfo'])
const handleSendMessage = (user, index) => {
  // 通过activeIndex给高亮
  // 增加高亮，并给
  chatStore.activeChat(index)
  // 把发消息对象的信息传给父组件再给渲染消息的组件
  emit('chatInfo', user)
}
</script>
<template>
  <div class="box">
    <div class="head">
      <span>{{ listVal ? '全部人员(o゜▽゜)o☆' : '最近消息(o゜▽゜)o☆' }}</span>
    </div>
    <div v-if="listVal">
      <div class="li" style="height: 60px" v-for="(x, i) in chats" :key="i">
        <members-list :info="x"></members-list>
      </div>
    </div>
    <div v-else>
      <div
        class="li"
        style="height: 80px"
        v-for="(x, i) in chats"
        :key="i"
        @click="handleSendMessage(x, i)"
        :class="{ active: i == chatStore.activeIndex }"
      >
        <friends-list :info="x"></friends-list>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.box {
  width: 215px;
  height: 100%;
  .head {
    width: 100%;
    height: 60px;
    display: flex;
    align-items: center;
    color: #004ed5;
    font-size: 18px;
    span {
      display: block;
      margin-left: 10px;
    }
  }
  .active {
    background-color: #eee;
  }
}
.li {
  width: 100%;
}
.li:hover {
  cursor: pointer;
  background-color: #eee;
}
</style>
