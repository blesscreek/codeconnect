<script setup>
import { computed } from 'vue'

// 好友名片
const props = defineProps({
  data: Object
})
const userInfo = computed(() => {
  if (!props.data.data) return {}
  return props.data.data
})
</script>
<template>
  <div
    class="box"
    :style="{ left: props.data.clientX + 'px', top: props.data.clientY + 'px' }"
  >
    <div class="content">
      <div class="left">
        <head-sculpture
          :url="userInfo.headImageThumb || '@/assets/head-sculpture.jpg'"
        ></head-sculpture>
      </div>
      <div class="right">
        <div class="span">昵称：{{ userInfo.nickname || '该用户不存在' }}</div>
        <div class="span">性别：{{ userInfo.sex ? '女' : '男' || '' }}</div>
        <div class="span">年龄：{{ userInfo.age || '0' }}</div>
        <div class="span">
          个性签名：{{ userInfo.signature || '这个人很神秘哦~' }}
        </div>
      </div>
    </div>
    <div class="button">
      <el-button type="primary">发消息</el-button>
      <el-button type="primary">加好友</el-button>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.box {
  position: fixed;
  top: 200px;
  left: 300px;

  width: 430px;
  height: 230px;
  background-color: #fff;
  border: 1px solid #cfcfcf;
}
.content {
  height: 160px;
  box-sizing: border-box;
  padding: 5px;
  border-bottom: 1px solid #cfcfcf;
  display: flex;
  .left {
    width: 110px;
    height: 100%;
    .head-sculpture {
      width: 80px;
      height: 80px;
      margin-left: 7px;
      margin-top: 7px;
    }
  }
  .right {
    width: 320px;
    height: 100%;
    .span {
      font-size: 16px;
      width: 100%;
      height: 30px;
      line-height: 30px;
    }
  }
  .button {
    width: 100%;
    height: 70px;
    display: flex;
    justify-content: center;
    align-items: center;
    .el-button {
      width: 125px;
      height: 40px;
      margin: 0 25px;
      font-size: 16px;
    }
  }
}
</style>
