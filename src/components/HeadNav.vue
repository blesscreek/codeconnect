<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore, useChatStore } from '@/stores'
import {
  House,
  Grid,
  ChatLineSquare,
  Management,
  UserFilled
} from '@element-plus/icons-vue'
// 导航栏高亮相关
const route = useRoute()
const userStore = useUserStore()
const chatStore = useChatStore()
const nav = ref({
  home: false,
  topic: false,
  chat: false,
  user: false,
  manage: false
})
// 改变高亮
const getlh = () => {
  const path = route.path.split('/')[1]
  nav.value = {
    home: false,
    topic: false,
    chat: false,
    user: false,
    manage: false
  }
  nav.value[path] = true
}
getlh()
// 监视路由
watch(route, () => {
  getlh()
})

// 个人中心和登出的菜单
const menu = ref(false)
const logout = () => {
  userStore.removeUser()
}

// 聊天室未读消息
</script>

<template>
  <div class="head_class">
    <div class="left">
      <div class="logo"><img src="@/assets/logo.png" /></div>
      <div
        class="li"
        :class="{ active: nav.home }"
        @click="$router.push('/home')"
      >
        <el-icon><House /></el-icon>
        <span>首页</span>
      </div>
      <div
        class="li"
        :class="{ active: nav.topic }"
        @click="$router.push('/topic')"
      >
        <el-icon><Grid /></el-icon>
        <span>题库</span>
      </div>
      <div
        class="li"
        :class="{ active: nav.chat }"
        @click="$router.push('/chat')"
      >
        <el-badge
          class="item"
          is-dot
          :hidden="chatStore.groupUnread + chatStore.msgUnread == 0"
        >
          <el-icon><ChatLineSquare /></el-icon>
          <span>交流</span>
        </el-badge>
      </div>

      <div
        class="li"
        :class="{ active: nav.user }"
        @click="$router.push('/user')"
      >
        <el-icon><UserFilled /></el-icon>
        <span>个人中心</span>
      </div>
      <div
        class="li"
        :class="{ active: nav.manage }"
        @click="$router.push('/manage')"
      >
        <el-icon><Management /></el-icon>
        <span>管理中心</span>
      </div>
    </div>
    <div class="right">
      <div class="head_sculpture">
        <head-sculpture :url="userStore.userInfo.headImageThumb">
        </head-sculpture>
      </div>
      <div
        @click="$router.push('/login')"
        class="login"
        v-if="!userStore.token"
      >
        登录 | 注册
      </div>
      <div v-else class="login" @click="menu = menu ? false : true">
        {{ userStore.userInfo.nickname }}
      </div>
      <div class="menu" v-show="menu" @click="menu = menu ? false : true">
        <div class="span" @click="$router.push('/user')">个人中心</div>
        <div class="span" @click="logout">登出</div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.head_class {
  width: 100%;
  min-width: 1200px;
  height: 100%;
  display: flex;
  flex-wrap: nowrap;
  justify-content: space-between;
  // border-top: 1px solid #e1e1e1;
}
.head_class div {
  height: 100%;
}
.left {
  display: flex;
  height: 100%;
}
.logo {
  height: 100%;
  margin-left: 30px;
  margin-right: 20px;
  img {
    width: 100%;
    height: 100%;
  }
}
.left {
  .li {
    width: 110px;
    margin-left: 10px;
    font-size: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #363636;
    border-bottom: 2px solid #fff;
    box-sizing: border-box;
    .item {
      height: 70%;
      width: 80%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    /*样式的过度*/
    /*transition: background-color 0.3s;*/
    span {
      margin-left: 5px;
    }
  }
  .li:hover {
    cursor: pointer;
    color: #005ac7;
    /*背景色渐变*/
    background-image: linear-gradient(to right, #ffffff, #eef7ff);
    border-bottom: 2px solid #005ac7;
  }
}
.active {
  color: #005ac7 !important;
  border-bottom: 2px solid #005ac7 !important;
  /*背景色渐变*/
  background-image: linear-gradient(to right, #ffffff, #eef7ff);
}
.right {
  position: relative;
  display: flex;
  align-items: center;
  .login {
    margin-right: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 17px;
  }
  .login:hover {
    color: #2a7fee;
    cursor: pointer;
  }
  .head_sculpture {
    width: 40px;
    height: 40px;
    margin-right: 20px;
  }
  .menu {
    position: absolute;
    top: 100%;
    width: 150px;
    height: 100px;
    background-color: #fff;
    .span {
      height: 40px;
      line-height: 40px;
      font-size: 17px;
      padding: 5px 15px;
      cursor: pointer;
    }
    .span:hover {
      background-color: #d6d6d6;
    }
  }
}
</style>
