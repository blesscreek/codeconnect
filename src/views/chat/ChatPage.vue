<script setup>
import InputBox from './InputBox.vue'
import MessageBox from './MessageBox.vue'
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
const chatRoom = ref(false)
const friend = ref(false)
const route = useRoute()

// 侧边栏高亮
const highlightChange = () => {
  let path = route.path.split('/')[2]
  if (path === 'chatroom') {
    chatRoom.value = true
    friend.value = false
  }
  if (path === 'friend') {
    friend.value = true
    chatRoom.value = false
  }
}
highlightChange()
// 路由变化高亮变化
watch(route, () => {
  highlightChange()
})
</script>

<template>
  <!-- 聊天区总页面 -->
  <div class="common-layout">
    <el-container>
      <el-aside>
        <div class="aside_nav">
          <div class="first_nav">
            <div
              class="li"
              :class="{ active: chatRoom }"
              @click="$router.push('/chat/chatroom')"
            >
              聊天室
            </div>
            <div
              class="li"
              :class="{ active: friend }"
              @click="$router.push('/chat/friend')"
            >
              好友
            </div>
          </div>
          <div class="second_nav">
            <router-view></router-view>
          </div>
        </div>
      </el-aside>
      <el-main>
        <div class="content">
          <message-box></message-box>
          <input-box></input-box>
        </div>
      </el-main>
    </el-container>
  </div>
</template>
<style lang="scss" scoped>
.common-layout {
  height: 100%;
  width: 100%;
}
.el-container {
  margin: 0 auto;
  width: 75vw;
  height: 100%;
  background-color: #fff;
  box-shadow: 5px 5px 5px #e6e6e6;
}
.el-aside {
  width: 320px;
  .aside_nav {
    width: 100%;
    height: 100%;
    display: flex;
    .first_nav {
      width: 32%;
      height: 100%;
      .li {
        width: 100%;
        height: 50px;
        margin-top: 5px;
        font-size: 18px;
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
      width: 68%;
      height: 100%;
    }
  }
}
.el-main {
  padding: 0;
  .content {
    width: 100%;
    height: 100%;
  }
}
</style>
