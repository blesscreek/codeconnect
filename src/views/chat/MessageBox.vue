<script setup>
import { ref } from 'vue'

const titleData = ref(true)
</script>
<template>
  <!-- 渲染聊天消息 -->
  <div class="box">
    <div class="head">
      <div>{{ titleData ? '聊天室' : '昵称最多七个字' }}</div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.box {
  width: 100%;
  height: 73%;
  border-radius: 10px;
  background-color: #f3f3f3;
  .head {
    width: 100%;
    height: 65px;
    background-color: #fff;
    font-size: 19px;
    box-sizing: border-box;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
</style>
