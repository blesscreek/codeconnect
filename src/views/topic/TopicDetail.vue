<!-- 题目详情总界面，可以拖动 -->
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
    if (startLeftWidth + delta > 500 && startRightWidth - delta > 500) {
      leftWidth.value = startLeftWidth + delta
      rightWidth.value = startRightWidth - delta
    }
  }
  if (draggingV.value) {
    // 计算垂直拖动的距离
    const delta = e.clientY - startY
    // 更新上下区域的高度
    if (startTopHeight + delta > 200 && startBottomHeight - delta > 200) {
      topHeight.value = startTopHeight + delta
      bottomHeight.value = startBottomHeight - delta
    }
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
// 测试样例
import TestCase from './TestCase.vue'
import { useRoute } from 'vue-router'
import { getQuestionService } from '@/api/topic.js'

// 拿到题目id
const route = useRoute()
console.log(route.fullPath)
const qid = route.fullPath.split('qid=')[1]
const topicData = ref({})
const examplesData = ref([])
// 解构相关数据
const getTopic = async () => {
  const res = await getQuestionService(qid)
  console.log(res.data)
  // console.log(res.data)
  // 数据分成两份，一份给左边的题目详情，一份给右下的测试样例
  let {
    background,
    description,
    difficulty,
    examples,
    hint,
    id,
    input,
    output,
    title
  } = res.data
  if (difficulty == 0) difficulty = '简单'
  if (difficulty == 1) difficulty = '中等'
  if (difficulty == 2) difficulty = '困难'
  examples = examples.split('"')
  examples = [[examples[1], examples[3]]]
  console.log(examples)
  topicData.value = {
    background: background,
    description: description,
    difficulty: difficulty,
    examples: examples,
    hint: hint,
    id: id,
    input: input,
    output: output,
    title: title
  }
  examplesData.value = examples
  // console.log(topicData.value)
}
getTopic()
</script>

<template>
  <div class="topic-detail">
    <!-- 头部导航 -->
    <el-header>
      <head-nav></head-nav>
    </el-header>
    <div class="container">
      <div class="left" :style="{ width: leftWidth + 'px' }">
        <topic-description :topicData="topicData"></topic-description>
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
          <test-case
            :examplesData="examplesData"
            :id="topicData.id"
          ></test-case>
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
  padding: 0;
  height: 6%;
  background-color: #fff;
}
.container {
  width: 100%;
  height: 94%;
  padding: 10px 20px;
  padding-top: 0;
  box-sizing: border-box;
  display: flex;

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
    // border: 1px solid #dbdbdb;
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
}
</style>
