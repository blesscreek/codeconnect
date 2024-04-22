<script setup>
import { ref } from 'vue'
const props = defineProps({
  chatInfo: Object
})
</script>
<template>
  <!-- 渲染聊天消息 -->
  <div class="Mbox">
    <div class="head">
      <div>
        {{ props.chatInfo.showName ? props.chatInfo.showName : '聊天室' }}
      </div>
    </div>
  </div>

  <!-- 发消息的输入框 -->
  <div class="Ibox">
    <div class="fun">
      <el-icon><img src="@/assets/picture.svg" alt="" /></el-icon>
      <el-icon><img src="@/assets/smile.svg" alt="" /></el-icon>
    </div>
    <textarea cols="30" rows="10"></textarea>
    <div class="footer">
      <div>15/300</div>
      <el-button type="primary">发送</el-button>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.Mbox {
  width: 100%;
  height: 73%;
  border-radius: 10px;
  background-color: #f3f3f3;
  .head {
    width: 100%;
    height: 60px;
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
.Ibox {
  width: 100%;
  height: 27%;
  background-color: #f3f3f3;
  border-top: 1px solid #c7c7c7;
  box-sizing: border-box;

  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
}
.fun {
  width: 100%;
  padding-left: 20px;
  height: 18%;
  display: flex;
  align-items: center;
  .el-icon {
    width: 45px;
    height: 28px;
    margin-top: 2px;
    opacity: 60%;
    cursor: pointer;
  }
  .el-icon:hover {
    opacity: 80%;
  }
  img {
    width: 28px;
  }
}
textarea {
  width: 100%;
  height: 59%;
  border: none;
  font-size: 20px;
  background-color: #f3f3f3;

  resize: none;
  outline: none;
  padding: 10px 30px;
  box-sizing: border-box;
  margin: 0;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  background-image: none;
}
.footer {
  height: 23%;
  width: 100%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  div {
    margin-right: 20px;
    color: #5c5c5c;
  }
  .el-button {
    max-height: 100%;
    margin-right: 20px;
  }
}
</style>
