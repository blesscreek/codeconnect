<script setup>
const props = defineProps({
  info: Object
})
</script>
<template>
  <!-- 左侧好友列表 -->
  <div class="box">
    <div class="left">
      <head-sculpture :url="props.info.headImage"></head-sculpture>
    </div>
    <div class="right">
      <div class="content">
        <span class="name">{{ props.info.showName }}</span>
        <span class="message">
          {{ props.info.lastContent || '你们已经是好友啦...' }}
        </span>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.box {
  width: 215px;
  height: 80px;
  display: flex;
  align-items: center;
}
.left {
  width: 50px;
  height: 50px;
  margin-left: 10px;
}
.right {
  width: 150px;
  height: 50px;
  margin-left: 5px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  padding-left: 5px;
  box-sizing: border-box;
  .content {
    height: 54px;
  }
  span {
    display: block;
    height: 27px;
  }
  .name {
    font-size: 17px;
  }
  .message {
    font-size: 14px;
    color: #616161;
    overflow: hidden;
  }
}
</style>
