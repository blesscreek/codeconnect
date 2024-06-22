<!-- 题目详情右下的测试用例，提交题目和显示提交结果也在这里 -->
<script setup>
import { submitJudgeQuestion } from '@/api/topic.js'
import { useTopicStore, useUserStore } from '@/stores'
import { ref, onUnmounted } from 'vue'
import SubmitResults from './SubmitResults.vue'

// 从父组件获取数据
const props = defineProps({
  id: Number,
  examplesData: Object
})
// 提交结果相关
const topicStore = useTopicStore()
// 测试用例相关
const textarea = ref('3 3 5\n1 2 3\n4 5 6\n7 8 9')
// 测试结果
const result = ref()
const submit = async () => {
  const obj = {
    qid: props.id,
    language: topicStore.language,
    code: topicStore.content,
    cid: 0,
    gid: 0,
    tid: 0
  }
  // 拿到子组件方法
  result.value.setDialogVisible()
  console.log(obj)
  if (!useUserStore().token) return
  await startSSE()
  await submitJudgeQuestion(obj)
}

// 提交题目的sse
const messages = ref([]) // 存储接收到的消息
const eventSource = ref(null) // 存储 EventSource 实例
// 启动 SSE 连接
const startSSE = async () => {
  if (eventSource.value) {
    return // 如果已连接，则不执行操作
  }
  try {
    // 获取 SSE URL
    const sseUrl = 'http://123.60.15.140:8080/sse/connect/3' // 假设服务器返回的 SSE URL 在 sseUrl 字段中
    // 创建新的 EventSource 实例，连接到 SSE 端点
    eventSource.value = new EventSource(sseUrl)
    // 监听消息事件
    eventSource.value.onmessage = (event) => {
      // 判断一下只有当前题目的才能加进去？
      messages.value.push(JSON.parse(event.data))
      console.log(messages.value[messages.value.length - 1])
    }
    // 监听错误事件
    eventSource.value.onerror = (error) => {
      console.error('SSE 错误:', error)
      stopSSE() // 遇到错误时停止 SSE 连接
    }
  } catch (error) {
    console.error('初始化 SSE 失败:', error)
  }
}
// 停止 SSE 连接
const stopSSE = () => {
  if (eventSource.value) {
    eventSource.value.close() // 关闭连接
    eventSource.value = null // 清空 eventSource
  }
}
// 组件卸载时停止 SSE 连接
onUnmounted(() => {
  stopSSE()
})
</script>

<template>
  <div class="content">
    <div class="head">
      <div class="test">
        <span>测试用例</span>
        <span>运行结果</span>
      </div>
      <!-- 提交 -->
      <div class="run-submit">
        <el-button color="#ededed" class="run" type="info" text bg>
          <img src="@/assets/play.svg" alt="" />
          <div style="padding-left: 5px">运行</div>
        </el-button>
        <el-button class="submit" type="info" text bg @click="submit">
          <img src="@/assets/submit.svg" alt="" />
          <div style="padding-left: 5px">提交</div>
        </el-button>
      </div>
    </div>
    <div class="box">
      <div class="case">测试样例</div>
      <el-input
        v-model="textarea"
        autosize
        type="textarea"
        placeholder="Please input"
        :disabled="true"
      />
    </div>
  </div>
  <submit-results ref="result" :messages="messages"></submit-results>
</template>

<style lang="scss" scoped>
.content {
  width: 100%;
  height: 100%;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
  overflow: hidden;
}

.head {
  width: 100%;
  height: 40px;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
  background-color: #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .test {
    margin-left: 20px;
    display: flex;

    span {
      color: #4a4a4a;
      margin: 0 10px;
      cursor: pointer;
    }

    span:hover {
      color: #000;
    }
  }
}

// 提交和运行按钮
.run-submit {
  height: 100%;
  margin-right: 20px;

  display: flex;
  justify-content: center;
  align-items: center;

  .el-button {
    border: none;
    width: 100px;
    height: 80%;
    margin: 0 1px;
    font-size: 17px;
  }

  .run {
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
    color: #323232;

    img {
      width: 25px;
    }
  }

  .submit {
    border-top-left-radius: 0;
    border-bottom-left-radius: 0;
    color: rgb(0, 179, 39);

    img {
      width: 25px;
    }
  }
}

.box {
  width: 100%;
  height: 85%;
  padding-left: 30px;
  box-sizing: border-box;

  .case {
    width: 100%;
    height: 40px;
    line-height: 40px;
  }

  textarea {
    // 设置没有边框和不可拖拽改变大小，现在没有效果
    outline: none;
    resize: none;
  }
}
</style>
