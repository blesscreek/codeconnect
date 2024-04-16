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
          <!-- 左侧导航栏 -->
          <div class="first_nav">
            <img src="@/assets/logo.png" alt="" />
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
              <!-- 好友 -->
              消息
            </div>
            <!-- <div
              class="li"
              :class="{ active: friend }"
              @click="$router.push('/chat/friend')"
            >
              团队
            </div> -->
            <div
              class="li"
              :class="{ active: back }"
              @click="$router.push('/')"
            >
              首页
            </div>
          </div>
          <!-- 显示好友等的地方 -->
          <div class="second_nav">
            <router-view></router-view>
          </div>
        </div>
      </el-aside>
      <!-- 内容区 -->
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
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  // background-image: url('@/assets/bi.png');
}
.el-container {
  max-width: 77vw;
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
