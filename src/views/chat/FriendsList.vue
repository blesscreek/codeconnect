<template>
  <!-- 左侧好友列表 -->
  <div class="box">
    <div class="left">
      <img src="@/assets/23.jpg" alt="" />
    </div>
    <div class="right">
      <div class="content">
        <span class="name">昵称最多七个字</span>
        <span class="message">你们已经是好友啦...</span>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.box {
  width: 100%;
  height: 80px;
  display: flex;
}
.box div {
  height: 100%;
  display: flex;
  flex-wrap: wrap;
}
.left {
  width: 28%;
  justify-content: center;
  align-items: center;
}
.right {
  width: 72%;
  align-items: center;
  padding-left: 5px;
  box-sizing: border-box;
  .content {
    height: 57px;
  }
  span {
    display: block;
  }
  .name {
    font-size: 16px;
  }
  .message {
    font-size: 15px;
    color: #616161;
  }
}
img {
  width: 48px;
  height: 48px;
  border-radius: 50%;
}
</style>
