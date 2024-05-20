<script setup>
// import { reactive, ref } from 'vue'
import { useUserStore } from '@/stores'
const userStore = useUserStore()
// userStore.getUserInfoServer()
</script>
<template>
  <div class="content">
    <div class="header">
      <div class="information">
        <div class="box">
          <div class="head">
            <head-sculpture
              :url="userStore.userInfo.headImageThumb"
            ></head-sculpture>
          </div>
          <div class="span">
            <div class="name">{{ userStore.userInfo.nickname }}</div>
            <div class="autograph">
              {{
                userStore.userInfo.signature || '这个人很神秘哦，神秘都没有写~'
              }}
            </div>
          </div>
        </div>
      </div>
      <div class="head_nav">
        <div class="li">主页</div>
        <div class="li">动态</div>
        <div class="li">练习</div>
        <div class="li">题库</div>
        <div class="li">收藏</div>
        <div class="li" @click="$router.push('/user/follow')">关注</div>
        <div class="li">我的</div>
        <div class="li" @click="$router.push('/user/setting')">设置</div>
      </div>
    </div>
    <div class="main">
      <router-view></router-view>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.content {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
}
.header {
  width: 1200px;
  height: 250px;
  // 头部的个人信息
  .information {
    width: 100%;
    height: 200px;
    background: url('@/assets/背景3.jpg') no-repeat center;
    background-size: cover;
    display: flex;
    align-items: end;
    .box {
      width: 600px;
      height: 120px;
      box-sizing: border-box;
      padding: 20px;
      display: flex;
      flex-wrap: wrap;
      .head {
        width: 80px;
        height: 80px;
        border-radius: 50%;
        border: 1px #fff solid;
        box-sizing: border-box;
      }
      .span {
        margin-left: 20px;
        margin-top: 5px;
        .name {
          font-size: 24px;
          font-weight: 600;
          color: #fff;
        }
        .autograph {
          margin-top: 20px;
          font-size: 14px;
          color: #fff;
        }
      }
    }
  }
  // 头部导航栏
  .head_nav {
    width: 100%;
    height: 50px;
    background-color: #fff;
    display: flex;
    .li {
      height: 50px;
      width: 80px;
      font-size: 17px;
      line-height: 50px;
      text-align: center;
      box-sizing: border-box;
      border-bottom: 2px solid #fff;
    }
    .li:hover {
      cursor: pointer;
      color: #2457d9;
      border-bottom: 2px solid #2f65ee;
    }
  }
}
.main {
  width: 1200px;
  height: 100%;
  margin: 20px;
  background-color: #fff;
}
</style>
