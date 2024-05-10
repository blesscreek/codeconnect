<script setup>
import MessageBox from './MessageBox.vue'
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useChatStore } from '@/stores'
import { findGroupServe, getGroupMemberServe } from '@/api/chat.js'

const chatRoom = ref(false)
const friend = ref(false)
const route = useRoute()
const chatStore = useChatStore()
const chatInfo = ref({}) //控制消息窗口头部的内容
// 聊天室信息
const chatRoomMembers = ref([]) //聊天室成员

// 获取聊天室的信息
const getChatRoomInfo = async () => {
  const res = await findGroupServe(16)
  chatInfo.value = {
    type: 'GROUP',
    targetId: 16,
    showName: res.data.name,
    headImage: res.data.imageThumb
  }
}

// 获取聊天室成员
const getChatRoomMember = async () => {
  const res = await getGroupMemberServe(16)
  chatRoomMembers.value = res.data
}

// 侧边栏高亮
const highlightChange = () => {
  let path = route.path.split('/')[2]
  if (path === 'chatroom') {
    chatRoom.value = true
    friend.value = false
    getChatRoomInfo()
    getChatRoomMember()
  }
  if (path === 'friend') {
    friend.value = true
    chatRoom.value = false
    if (chatStore.chats[chatStore.activeIndex]) {
      chatInfo.value = chatStore.chats[chatStore.activeIndex]
    } else {
      chatInfo.value = []
    }
  }
}
highlightChange()
// 路由变化高亮变化
watch(route, () => {
  highlightChange()
})

// 能不能拿到发消息对象的信息  可以路由那个也是父组件
const getChatInfo = (val) => {
  chatInfo.value = {}
  chatInfo.value = val
}

// 未读消息
console.log(chatStore.groupUnread, chatStore.msgUnread)
</script>

<template>
  <!-- 聊天区总页面 -->
  <div class="common-layout">
    <el-container>
      <el-aside>
        <div class="aside_nav">
          <!-- 左侧导航栏 -->
          <div class="first_nav">
            <img src="@/assets/logo.png" alt="" />
            <div
              class="li"
              :class="{ active: chatRoom }"
              @click="$router.replace('/chat/chatroom')"
            >
              <el-badge
                class="item"
                :value="chatStore.groupUnread"
                :hidden="chatStore.groupUnread == 0"
                :offset="[-10, 0]"
              >
                聊天室
              </el-badge>
            </div>

            <div
              class="li"
              :class="{ active: friend }"
              @click="$router.replace('/chat/friend')"
            >
              <el-badge
                class="item"
                :value="chatStore.msgUnread"
                :hidden="chatStore.msgUnread == 0"
                :offset="[-10, 0]"
              >
                消息
              </el-badge>
            </div>
            <!-- <div
              class="li"
              :class="{ active: friend }"
              @click="$router.push('/chat/friend')"
            >
              团队
            </div> -->
            <div class="li" @click="$router.replace('/')">首页</div>
          </div>
          <!-- 显示好友等的地方 -->
          <div class="second_nav">
            <router-view
              @chatInfo="getChatInfo"
              :chatRoomMembers="chatRoomMembers"
            ></router-view>
          </div>
        </div>
      </el-aside>
      <!-- 内容区 -->
      <el-main>
        <div class="content">
          <message-box
            :chatInfo="chatInfo"
            :groupMembers="chatRoomMembers"
          ></message-box>
        </div>
      </el-main>
    </el-container>
  </div>
</template>
<style lang="scss" scoped>
.common-layout {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
}
.el-container {
  max-width: 1250px;
  height: 95vh;
  margin: 0 auto;
}
.el-aside {
  width: 340px;
  .aside_nav {
    width: 100%;
    height: 100%;
    display: flex;
    .first_nav {
      width: 95px;
      height: 100%;
      border-radius: 10px;
      background-color: #fff;
      .item {
        width: 80%;
        height: 100%;
      }
      img {
        width: 100%;
        margin: 10px 0;
      }
      .li {
        width: 100%;
        height: 50px;
        margin-top: 5px;
        font-size: 17px;
        text-align: center;
        line-height: 45px;
        color: #353535;
        border-left: 3px solid #ffffff;
        box-sizing: border-box;
      }
      .active {
        color: #358bf4;
        cursor: pointer;
        border-left: 3px solid #1169e5;
      }
      .li:hover {
        color: #358bf4;
        cursor: pointer;
        border-left: 3px solid #1169e5;
      }
    }
    .second_nav {
      width: 215px;
      height: 100%;
      // 聊天室的两个样式
      margin: 0 15px;
      box-sizing: border-box;
      border-radius: 10px;
      background-color: #fff;
    }
  }
}
.el-main {
  min-width: 910px;
  padding: 0;
  box-sizing: border-box;
  border: 1px solid #e6e6e6;
  border-radius: 10px;
  box-shadow: 5px 5px 5px #e6e6e6;
  .content {
    width: 100%;
    height: 100%;
  }
}
</style>
