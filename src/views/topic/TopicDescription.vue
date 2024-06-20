<!-- 题目详情左边的题目信息 -->
<script setup>
import { Tickets, Key, Timer } from '@element-plus/icons-vue'
import { computed } from 'vue'
const props = defineProps({
  topicData: Object
})
console.log(props.topicData)
const data = computed(() => {
  for (let x of props.topicData.examples) {
    // 测试样例的回车要转义一下
    x[0] = x[0].replace(/\\n/g, '\n')
    x[1] = x[1].replace(/\\n/g, '\n')
  }
  console.log(props.topicData.examples)
  return props.topicData
})
console.log(data)
</script>

<template>
  <div class="content">
    <div class="header">
      <div>
        <el-icon><Tickets /></el-icon>&nbsp;题目描述
      </div>
      <div>
        <el-icon><Key /></el-icon>&nbsp;题解
      </div>
      <div>
        <el-icon><Timer /></el-icon>&nbsp;提交记录
      </div>
    </div>
    <div class="box">
      <!-- 题目名称和标签 -->
      <div class="topic-name">
        <span>{{ data.title }}</span>
      </div>
      <div class="topic-label">
        <div class="difficulty">{{ data.difficulty }}</div>
        <div class="algorithm">相关算法标签</div>
        <!-- 历史分数 -->
        <div class="score">无</div>
      </div>
      <!-- 题目背景等 -->
      <!-- 多用一个div包了一下，防止影响题目和标签 -->
      <div class="description-data">
        <div class="topic-background">
          <span>题目背景</span>
          <div v-html="data.background"></div>
        </div>
        <div class="topic-description">
          <span>题目描述</span>
          <div v-html="data.description"></div>
        </div>
        <div class="format">
          <span>输入描述</span>
          <div v-html="data.input"></div>
          <span>输出描述</span>
          <div v-html="data.output"></div>
        </div>
        <div class="example">
          <span>输入样例</span>
          <div>
            <el-input
              v-for="(x, i) in data.examples"
              :key="i"
              v-model="x[0]"
              type="textarea"
              :disabled="true"
            />
          </div>
          <span>输出样例</span>
          <div>
            <el-input
              v-for="(x, i) in data.examples"
              :key="i"
              v-model="x[1]"
              type="textarea"
              :disabled="true"
            />
          </div>
        </div>
        <div class="prompt">
          <span>提示</span>
          <div v-html="data.hint"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.content {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  flex-wrap: wrap;
  .header {
    width: 100%;
    height: 45px;
    background-color: #f0f0f0;
    border-top-left-radius: 20px;
    border-top-right-radius: 20px;
    display: flex;
    justify-content: start;
    align-items: center;
    padding-left: 20px;
    // 左侧头部题目描述等的样式
    div {
      // width: 90px;
      height: 100%;
      margin: 0 10px;
      box-sizing: border-box;
      border-bottom: 2px solid #00000000;
      display: flex;
      align-items: center;
      justify-content: center;
      .el-icon {
        color: #3b82f680;
      }
    }
    // 应该是点击后换颜色
    div:hover {
      cursor: pointer;
      border-bottom: 2px solid #3b82f6;
      .el-icon {
        color: #3b82f6;
      }
    }
  }
}
.box {
  width: 100%;
  height: 95%;
  padding: 0 2%;
  overflow-y: auto;
  // 标签之后的样式
  // 多用一个div包了一下，防止影响题目和标签
  div {
    div {
      span {
        font-size: 19px;
        font-weight: 550;
        color: #2d2d2d;
        display: block;
        padding-top: 20px;
      }
      div {
        margin-top: 15px;
        color: #363636;
      }
    }
  }
}
// 标题
.topic-name {
  width: 100%;
  height: 45px;
  margin-top: 20px;
  font-size: 30px;
  font-weight: 550;
}
// 标签
.topic-label {
  width: 100%;
  height: 40px;
  display: flex;
  align-items: center;
  div {
    height: 25px;
    font-size: 15px;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 10px;
    background-color: #efefef;
  }
  .difficulty {
    width: 60px;
    // color: #f21313;
    color: #000;
  }
  .algorithm {
    width: 150px;
    color: #2b2b2b;
    margin: 0 15px;
  }
  .score {
    width: 50px;
    color: #2b2b2b;
  }
}
// 题目背景
.topic-background {
}
// 题目描述
.topic-description {
}
// 输入输出描述
.format {
  span {
    display: block;
  }
}
// 输入输出样例
.example {
  span {
    display: block;
  }
  .el-input {
    width: 200px;
    height: 50px;
  }
}
// 提示
.prompt {
}
</style>
