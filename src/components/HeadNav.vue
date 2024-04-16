<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores'
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

// 登录后头像和昵称的渲染
const token = userStore.token
// if (token) {
// }
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
        <el-icon><ChatLineSquare /></el-icon>
        <span>交流</span>
      </div>
      <div
        class="li"
        :class="{ active: nav.user }"
        @click="$router.push('/chat')"
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
        <head-sculpture></head-sculpture>
      </div>
      <div @click="$router.push('/login')" class="login" v-if="!token">
        登录 | 注册
      </div>
      <div v-else class="login">{{ 'username' }}</div>
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
  width: 100px;
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
    font-size: 18px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #363636;
    border-bottom: 2px solid #fff;
    box-sizing: border-box;
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
  display: flex;
  align-items: center;
  .login {
    margin-right: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
  }
  .login:hover {
    color: #2a7fee;
    cursor: pointer;
  }
  .head_sculpture {
    width: 45px;
    height: 45px;
    margin-right: 20px;
  }
}
</style>
