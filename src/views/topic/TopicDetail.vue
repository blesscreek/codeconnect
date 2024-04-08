<script setup>
// 页面区域调节相关
import { ref, onMounted, onBeforeUnmount } from 'vue'

const leftWidth = ref(window.innerWidth / 2) // 左边区域的宽度
const rightWidth = ref(window.innerWidth / 2) // 右边区域的宽度
const topHeight = ref(300) // 右边上面区域的高度
const bottomHeight = ref(300) // 右边下面区域的高度
const draggingH = ref(false) // 是否正在水平拖动
const draggingV = ref(false) // 是否正在垂直拖动
let startX = 0 // 水平拖动开始时的鼠标位置
let startY = 0 // 垂直拖动开始时的鼠标位置
let startLeftWidth = 0 // 水平拖动开始时的左边区域宽度
let startRightWidth = 0
let startTopHeight = 0 // 垂直拖动开始时的右边上面区域高度
let startBottomHeight = 0

const startDragH = (e) => {
  draggingH.value = true
  startX = e.clientX
  startLeftWidth = leftWidth.value
  startRightWidth = rightWidth.value
}

const startDragV = (e) => {
  draggingV.value = true
  startY = e.clientY
  startTopHeight = topHeight.value
  startBottomHeight = bottomHeight.value
}

const onDrag = (e) => {
  if (draggingH.value) {
    // 计算水平拖动的距离
    const delta = e.clientX - startX
    // 更新左右区域的宽度
    leftWidth.value = startLeftWidth + delta
    rightWidth.value = startRightWidth - delta
  }
  if (draggingV.value) {
    // 计算垂直拖动的距离
    const delta = e.clientY - startY
    // 更新上下区域的高度
    topHeight.value = startTopHeight + delta
    bottomHeight.value = startBottomHeight - delta
  }
}

const endDrag = () => {
  draggingH.value = false
  draggingV.value = false
}

const onresize = () => {
  leftWidth.value = window.innerWidth * 0.5 - 5
  rightWidth.value = window.innerWidth * 0.5 - 5
  topHeight.value = window.innerHeight * 0.5 - 5
  bottomHeight.value = window.innerHeight * 0.5 - 5
}

onMounted(() => {
  // 监听鼠标移动和松开事件
  document.addEventListener('mousemove', onDrag)
  document.addEventListener('mouseup', endDrag)
  window.addEventListener('resize', onresize)
  // 初始左右的宽度
  leftWidth.value = window.innerWidth * 0.4 - 5
  rightWidth.value = window.innerWidth * 0.6 - 5
  topHeight.value = window.innerHeight * 0.7 - 5
  bottomHeight.value = window.innerHeight * 0.3 - 5
})

onBeforeUnmount(() => {
  // 移除事件监听
  document.removeEventListener('mousemove', onDrag)
  document.removeEventListener('mouseup', endDrag)
})

// 题目描述相关
import TopicDescription from './TopicDescription.vue'
// 代码编辑器相关
import CodeSubmission from './CodeSubmission.vue'
</script>

<template>
  <div class="topic-detail">
    <!-- 头部导航 -->
    <el-header>
      <!-- 左边 -->
      <div class="head-left">
        <img src="@/assets/logo.png" alt="" />
        <div class="li">题库</div>
        <div class="li">上一道</div>
        <div class="li">下一道</div>
      </div>
      <!-- 中间 -->
      <div class="head-center">
        <el-button class="run" type="info" text bg>
          <img src="@/assets/play.svg" alt="" />
          <div style="padding-left: 5px">运行</div>
        </el-button>
        <el-button class="submit" type="info" text bg>
          <img src="@/assets/submit.svg" alt="" />
          <div style="padding-left: 5px">提交</div>
        </el-button>
      </div>
      <div class="head-right">
        <div class="box">个人中心</div>
      </div>
    </el-header>
    <div class="container">
      <div class="left" :style="{ width: leftWidth + 'px' }">
        <topic-description></topic-description>
      </div>
      <div class="divider-h" @mousedown="startDragH">
        <span></span>
      </div>
      <div class="right" :style="{ width: rightWidth + 'px' }">
        <div class="top" :style="{ height: topHeight + 'px' }">
          <code-submission></code-submission>
        </div>
        <div class="divider-v" @mousedown="startDragV">
          <span></span>
        </div>
        <div class="bottom" :style="{ height: bottomHeight + 'px' }">
          <p>测试用例</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.topic-detail {
  width: 100vw;
  height: 100vh;
  background-color: rgb(245, 245, 245);
}
.el-header {
  width: 100%;
  height: 6%;
  margin: 0;
  padding: 0;
  display: flex;
  .head-left {
    width: 20%;
    display: flex;
    align-items: center;
    img {
      height: 100%;
      margin-left: 20px;
    }
    .li {
      width: 50px;
      margin-left: 1px;
      text-align: center;
      height: 30px;
      background-color: #b66767;
    }
  }
  .head-center {
    height: 100%;
    width: 60%;
    justify-self: center;

    display: flex;
    justify-content: center;
    align-items: center;
    .el-button {
      border: none;
      background-color: #e7e7e7;
      width: 120px;
      height: 85%;
      margin: 0 1px;
      font-size: 18px;
    }
    .el-button:hover {
      background-color: #e3e3e3;
    }
    .run {
      border-top-right-radius: 0;
      border-bottom-right-radius: 0;
      color: #323232;
      img {
        width: 30px;
      }
    }
    .submit {
      border-top-left-radius: 0;
      border-bottom-left-radius: 0;
      color: rgb(0, 179, 39);
      img {
        width: 30px;
      }
    }
  }
  .head-right {
    width: 20%;
  }
}
.container {
  width: 100%;
  height: 94%;
  padding: 10px 20px;
  padding-top: 0;
  box-sizing: border-box;
  display: flex;
}

.left {
  display: flex;
  flex-direction: column;
  background-color: #fff;
  height: 100%;
  width: 50%;
  border-radius: 20px;
}

.right {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 50%;
}

.top {
  background-color: #fff;
  border-radius: 20px;
  box-sizing: border-box;
  border: 1px solid #dbdbdb;
}

.bottom {
  background-color: #fff;
  border-radius: 20px;
}

.divider-h {
  width: 10px;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: e-resize;
  span {
    display: block;
    height: 50px;
    width: 4px;
    border-radius: 2px;
    background-color: #bdbdbd;
  }
}

.divider-v {
  height: 10px;
  width: 100%;
  cursor: row-resize;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: n-resize;
  span {
    display: block;
    width: 50px;
    height: 4px;
    border-radius: 2px;
    background-color: #bdbdbd;
  }
}
</style>
