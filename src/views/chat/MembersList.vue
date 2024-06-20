<!-- 聊天室成员小组件 -->
<script setup>
const props = defineProps({
  info: Object
})
</script>
<template>
  <!-- 左侧聊天室成员列表 -->
  <div class="mbox">
    <div class="left">
      <head-sculpture :url="props.info.headImage"></head-sculpture>
    </div>
    <div class="right">
      <span>{{ props.info.memberNickname }}</span>
    </div>
  </div>
</template>
<style scoped>
.mbox {
  width: 215px;
  height: 60px;
  display: flex;
}
.box div {
  display: flex;
  align-items: center;
}
.left {
  width: 40px;
  height: 40px;
  justify-content: center;
  margin-left: 10px;
}
.right {
  width: 160px;
}
span {
  font-size: 16px;
  margin-left: 10px;
}
</style>
