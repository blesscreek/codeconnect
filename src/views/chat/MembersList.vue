<template>
  <!-- 左侧聊天室成员列表 -->
  <div class="mbox">
    <div class="left">
      <img src="@/assets/head-sculpture.jpg" alt="" />
    </div>
    <div class="right">
      <span>昵称最多七个字</span>
    </div>
  </div>
</template>
<style scoped>
.mbox {
  width: 100%;
  height: 60px;
  display: flex;
}
.box div {
  height: 100%;
  display: flex;
  align-items: center;
}
.left {
  width: 25%;
  justify-content: center;
}
.right {
  width: 75%;
}
img {
  width: 42px;
  border-radius: 50%;
}
span {
  font-size: 16px;
  margin-left: 5px;
}
</style>
