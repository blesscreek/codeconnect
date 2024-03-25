<script setup>
import {
  House,
  Grid,
  ChatLineSquare,
  Management
} from '@element-plus/icons-vue'
</script>

<template>
  <div class="head_class">
    <div class="left">
      <div class="logo"><img src="@/assets/logo.png" /></div>
      <div class="li">
        <el-icon><House /></el-icon>
        <span>首页</span>
      </div>
      <div class="li">
        <el-icon><Grid /></el-icon>
        <span>题目</span>
      </div>
      <div class="li">
        <el-icon><ChatLineSquare /></el-icon>
        <span>讨论</span>
      </div>
      <div class="li">
        <el-icon><Management /></el-icon>
        <span>管理中心</span>
      </div>
    </div>
    <div class="login" v-if="true">
      <el-button type="primary">登录</el-button>
      <el-button type="primary">注册</el-button>
    </div>
    <div class="head_sculpture" v-else>
      <img src="@/assets/head-sculpture.jpg" alt="" />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.head_class {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: nowrap;
  justify-content: space-between;
  border-top: 1px solid #e1e1e1;
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
}
.logo img {
  width: 100%;
  height: 100%;
}
.left .li {
  width: 110px;
  margin-left: 10px;
  font-size: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #000;
  border-bottom: 2px solid #fff;
  box-sizing: border-box;
  /*样式的过度*/
  /*transition: background-color 0.3s;*/
  span {
    margin-left: 5px;
  }
}
.left .li:hover {
  cursor: pointer;
  color: #094691;
  /*背景色渐变*/
  background-image: linear-gradient(to right, #ffffff, #e9f4fe);
  border-bottom: 2px solid #094792;
}
.login {
  margin-right: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  .el-button {
    width: 70px;
    height: 35px;
    font-size: 18px;
  }
}
.head_sculpture {
  margin-right: 30px;
  display: flex;
  align-items: center;
  img {
    width: 50px;
    height: 50px;
    border-radius: 50%;
  }
}
</style>
