<!-- 提交题目结果的内容 -->
<script setup>
import { ref, watch } from 'vue'
import CodeEditor from '@/components/CodeEditor.vue'
// 获取消息
const props = defineProps({
  messages: Object
})

// 打开关闭
const dialogVisible = ref(false)
const setDialogVisible = () => {
  dialogVisible.value = true
}
// 向外保留打开消息框的方法
defineExpose({
  setDialogVisible
})
//总信息
const resultInformation = ref({
  judgeCaseInfoList: [
    {
      id: 0,
      caseId: 0,
      status: 0,
      columnName: 'columnName_12fe68f116ec',
      time: 0,
      memory: 0,
      showTime: 'showTime_70ffd784e639',
      showMemory: 'showMemory_326b1c81dd47',
      score: 0,
      color: 'color_6e0b2b426f3e'
    }
  ],
  id: 0,
  qid: 0,
  uid: 0,
  status: 0,
  errorMessage: 'errorMessage_a7717493f90b',
  score: 0,
  time: 0,
  memory: 0,
  showTime: 'showTime_862cdd4e2535',
  showMemory: 'showMemory_6e21630c7afc',
  length: 0,
  code: '#include<stdio.h>\nint main(){\treturn 0;\n}',
  language: 'C'
})

// 测试点表格信息
const tableData = ref([
  { caseId: 1, memory: 5, time: 10, columnName: 'ac', score: 10 },
  { caseId: 1, memory: 5, time: 10, columnName: 'ac', score: 10 },
  { caseId: 1, memory: 5, time: 10, columnName: 'ac', score: 10 },
  { caseId: 1, memory: 5, time: 10, columnName: 'ac', score: 10 }
])

watch(props, () => {
  // if()
  resultInformation.value = props.messages[props.messages.length - 1]
  tableData.value = props.messages[props.messages.length - 1].judgeCaseInfoList
})
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    title="测试点信息"
    width="800"
    top="10vh"
    :style="{ maxHeight: '80%', overflowY: 'auto' }"
  >
    <div class="content">
      <!-- <div class="span span1">提交结果信息</div> -->
      <div class="box">
        <div class="span">题目：{{ resultInformation.qid }}</div>
        <div class="span">用户：{{ resultInformation.uid }}</div>
        <div class="span">语言：{{ resultInformation.language }}</div>
        <div class="span">内存：{{ resultInformation.memory }}</div>
        <div class="span">时间：{{ resultInformation.showTime }}</div>
        <div class="span">状态：{{ resultInformation.status }}</div>
        <div class="span">分数：{{ resultInformation.score }}</div>
        <div class="span">提交时间：{{ resultInformation.time }}</div>
      </div>
    </div>
    <el-table v-show="true" :data="tableData" border style="width: 100%">
      <el-table-column prop="caseId" label="测试点" />
      <el-table-column prop="memory" label="内存" />
      <el-table-column prop="time" label="用时" />
      <el-table-column prop="columnName" label="结果" />
      <el-table-column prop="score" label="得分" />
    </el-table>
    <div class="code">
      <div class="span1">源代码</div>
      <code-editor
        :language="resultInformation.language"
        :content="resultInformation.code"
        :ban="true"
      ></code-editor>
    </div>
    <div class="out" v-show="false">
      <div class="span1">编译器输出</div>
      <code-editor
        :language="resultInformation.language"
        :content="resultInformation.code"
        :ban="true"
      ></code-editor>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>
<style lang="scss" scoped>
.content {
  margin: 10px 0;
  padding: 8px;
  border-radius: 10px;
  background-color: #f3f3f3;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  .box {
    width: 95%;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
  }
  .span {
    width: 50%;
    height: 28px;
    line-height: 28px;
    color: #000;
  }
  .span1 {
    width: 95%;
    font-size: 15px;
  }
}
.code {
  .span1 {
    width: 50%;
    height: 28px;
    line-height: 28px;
    font-size: 18px;
    color: #000;
    margin: 10px 0;
  }
}
.out {
  .span1 {
    width: 50%;
    height: 28px;
    line-height: 28px;
    font-size: 18px;
    color: #000;
    margin: 10px 0;
  }
}
</style>
