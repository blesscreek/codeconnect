<script setup>
import { ref } from 'vue'
import { DocumentAdd } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { addTopicService } from '@/api/topic.js'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const pageData = ref({
  title: '添加题目',
  button: '发布'
})

if (route.path.split('/')[3] == 'add') {
  pageData.value.title = '添加题目'
  pageData.value.button = '发布'
}
if (route.path.split('/')[3] == 'edit') {
  pageData.value.title = '编辑题目'
  pageData.value.button = '确认'
}

// 绑定表单
const formData = {
  title: '',
  background: '',
  description: '',
  input: '',
  output: '',
  examples: [],
  tag: [],
  difficulty: '',
  timeLimit: '',
  memoryLimit: '',
  hint: '',
  auth: '0',
  type: '0',
  judgeMode: 'default'
}
const form = ref(formData)

// 获取富文本编辑器的值
const getHint = (val) => {
  form.value.hint = val
}
const getBackground = (val) => {
  form.value.background = val
}
const getDescription = (val) => {
  form.value.description = val
}

// 增加输入输出样例
let num = ref(0)
const addExample = () => {
  if (num.value == 3) {
    ElMessageBox.alert('测试样例最多有三个！', '提示', {
      confirmButtonText: '确认'
    })
    return
  }
  form.value.examples.push([])
  num.value++
}

const onSubmit = async () => {
  const data = getData()
  const res = await addTopicService(data)
  if (res.code == 200) ElMessage.success('添加成功')
}

const onCancel = async () => {
  await ElMessageBox.confirm('确定取消编辑吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  })
  form.value = formData
  router.go(-1)
}

// 提交时修改格式
const getData = () => {
  // 改成对应格式
  if (!form.value.title) {
    ElMessage.error('标题不能为空')
    return
  }
  if (!form.value.description) {
    ElMessage.error('题目描述不能为空')
    return
  }
  if (!form.value.timeLimit || !form.value.memoryLimit) {
    ElMessage.error('时间限制和空间限制不能为空')
    return
  }
  if (!form.value.input || !form.value.output) {
    ElMessage.error('输入描述和输出描述不能为空')
    return
  }
  if (form.value.examples.length == 0) {
    ElMessage.error('输入输出样例不能为空')
    return
  } else {
    for (let i = 0; i < form.value.examples.length; i++) {
      if (
        form.value.examples[i].length < 2 ||
        !form.value.examples[i][0] ||
        !form.value.examples[i][1]
      ) {
        ElMessage.error(`样例${i + 1}内容不能为空`)
        return
      }
    }
  }
  if (!form.value.difficulty) {
    ElMessage.error('题目难度不能为空')
    return
  }
  if (form.value.tag.length == 0) {
    ElMessage.error('算法标签不能为空')
    return
  }
  const data = {
    question: {
      title: form.value.title,
      background: form.value.background,
      description: form.value.description,
      input: form.value.input,
      output: form.value.output,
      examples: JSON.stringify(form.value.examples),
      timeLimit: form.value.timeLimit,
      memoryLimit: form.value.memoryLimit,
      hint: form.value.hint,
      judgeMode: form.value.judgeMode,
      difficulty: '',
      auth: '',
      type: ''
    },
    tags: []
  }
  if (form.value.difficulty == '简单') data.question.difficulty = 0
  if (form.value.difficulty == '中等') data.question.difficulty = 1
  if (form.value.difficulty == '困难') data.question.difficulty = 2
  data.question.auth = parseInt(form.value.auth)
  data.question.type = parseInt(form.value.type)
  for (let i = 0; i < form.value.tag.length; i++) {
    data.tags.push({ ['name']: form.value.tag[i] })
  }
  return data
}
</script>

<template>
  <div class="addTopicContent">
    <div class="span">{{ pageData.title }}</div>
    <el-form :model="form" label-width="auto" style="max-width: 800px">
      <el-form-item label="题目名称">
        <el-input v-model="form.title" />
      </el-form-item>

      <el-form-item label="说明/提示">
        <div class="editor">
          <!-- 局部定义的富文本组件 -->
          <my-editor @updateValue="getHint"></my-editor>
        </div>
      </el-form-item>
      <el-form-item label="题目背景">
        <div class="editor">
          <!-- 局部定义的富文本组件 -->
          <my-editor @updateValue="getBackground"></my-editor>
        </div>
      </el-form-item>
      <el-form-item label="题目描述">
        <div class="editor">
          <!-- 局部定义的富文本组件 -->
          <my-editor @updateValue="getDescription"></my-editor>
        </div>
      </el-form-item>

      <!-- 判断是否正确 -->
      <el-form-item>
        <el-form-item label="时间限制">
          <el-input v-model.number="form.timeLimit" />
        </el-form-item>
        <el-form-item label="内存限制">
          <el-input v-model.number="form.memoryLimit" />
        </el-form-item>
      </el-form-item>

      <el-form-item>
        <el-form-item label="输入描述">
          <el-input v-model="form.input" type="textarea" />
        </el-form-item>
        <el-form-item label="输出描述">
          <el-input v-model="form.output" type="textarea" />
        </el-form-item>
      </el-form-item>

      <el-form-item label="输入输出样例" class="in-out">
        <el-button @click="addExample" :icon="DocumentAdd" />
        <el-form-item class="example" v-for="i in num" :key="i">
          <el-form-item :label="`输入样例${i}`">
            <el-input v-model="form.examples[i - 1][0]" type="textarea" />
          </el-form-item>
          <el-form-item :label="`输出样例${i}`">
            <el-input v-model="form.examples[i - 1][1]" type="textarea" />
          </el-form-item>
        </el-form-item>
      </el-form-item>

      <el-form-item label="难度选择">
        <el-select v-model="form.difficulty" placeholder="请选择难度">
          <el-option label="简单" value="简单" />
          <el-option label="中等" value="中等" />
          <el-option label="困难" value="困难" />
        </el-select>
      </el-form-item>

      <el-form-item label="算法标签">
        <el-checkbox-group v-model="form.tag">
          <el-checkbox value="顺序结构" name="tag"> 顺序结构 </el-checkbox>
          <el-checkbox value="分支结构" name="tag"> 分支结构 </el-checkbox>
          <el-checkbox value="kfcvivo50" name="tag"> 疯狂星期四 </el-checkbox>
          <el-checkbox value="wcop" name="tag"> 不许玩原神 </el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="赛制：">
        <el-radio-group v-model="form.type">
          <el-radio value="0">OI</el-radio>
          <el-radio value="1">ACM</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="判题模式：">
        <el-radio-group v-model="form.judgeMode">
          <el-radio value="default">default</el-radio>
          <el-radio value="spj">spj</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="发布为：">
        <el-radio-group v-model="form.auth">
          <el-radio value="0">公开</el-radio>
          <el-radio value="1">私有</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- 按钮 -->
      <el-form-item class="button">
        <el-button type="primary" @click="onSubmit">
          {{ pageData.button }}
        </el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style lang="scss" scoped>
.addTopicContent {
  width: 100%;
  height: 100%;
  .span {
    width: 100%;
    height: 70px;
    text-align: center;
    font-size: 26px;
    color: #355bcc;
  }
}
.el-form {
  margin: 0 auto;
  padding-bottom: 30px;
  .button {
    width: 200px;
    margin: 0 auto;
  }
}
</style>
