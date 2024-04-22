<script setup>
import { ref } from 'vue'
import { getFriendListService } from '@/api/user'
import FriendBox from './FriendBox.vue'
// 获取好友信息相关
const friendList = ref([])
const getList = async () => {
  const res = await getFriendListService()
  friendList.value = res.data
  console.log(friendList.value)
}
getList()

// 侧边导航
const getIndex = (key) => {
  console.log(key)
}
</script>
<template>
  <div class="content">
    <div class="nav">
      <el-row class="tac">
        <el-col>
          <el-menu default-active="1-1" class="el-menu-vertical-demo">
            <el-sub-menu index="1">
              <template #title>
                <span>我的关注</span>
              </template>
              <el-menu-item index="1-1" @click="getIndex('1-1')">
                全部关注
              </el-menu-item>
              <el-menu-item index="1-2" @click="getIndex('1-2')">
                特别关注
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item index="2" @click="getIndex('2')">
              <span>我的粉丝</span>
            </el-menu-item>
          </el-menu>
        </el-col>
      </el-row>
    </div>
    <div class="list">
      <div class="span">全部关注</div>
      <friend-box
        v-for="(x, i) in friendList"
        :key="i"
        :friendInfo="x"
      ></friend-box>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.content {
  width: 1200px;
  height: 600px;
  .nav {
    width: 200px;
    height: 100%;
    background-color: #fff;
    box-sizing: border-box;
    border-right: 1px solid rgb(220, 223, 230);
    .el-col {
      width: 100%;
      .el-menu {
        border: none;
      }
    }
  }
  .list {
    width: 1000px;
    background-color: #ffffff;
    overflow: hidden;
    .span {
      height: 60px;
      line-height: 60px;
      margin-left: 20px;
      padding-left: 20px;
      font-size: 18px;
      border-bottom: 1px solid #dadada;
    }
  }
}
</style>
