<!-- 富文本编辑器-发消息的输入框 -->
<script setup>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { reactive, ref, toRaw } from 'vue'

const emit = defineEmits(['updateValue'])
const content = ref('')
const myQuillEditor = ref()
// 通过watch监听回显，笔者这边使用v-model:content 不能正常回显
const data = reactive({
  content: '',
  editorOption: {
    modules: {
      toolbar: null
    }
  }
})

// 记住上次聚焦的位置
const index = ref(0)
const updateLength = () => {
  const quill = toRaw(myQuillEditor.value).getQuill()
  if (quill.getSelection()) {
    index.value = quill.getSelection().index
  }
}

// 抛出更改内容，此处避免出错直接使用文档提供的getHTML方法
const setValue = () => {
  const text = toRaw(myQuillEditor.value).getHTML()
  emit('updateValue', text)
  updateLength()
}

// 处理emoji表情  向外导出
const handleEmoji = (val) => {
  const url = val
  if (url) {
    const quill = toRaw(myQuillEditor.value).getQuill()
    const length = index.value
    quill.insertEmbed(length, 'image', url)
    quill.setSelection(length + 1)
    index.value = length + 1
  }
}
// 提交后清空富文本框
const afterUpdata = () => {
  toRaw(myQuillEditor.value).setText('')
}
defineExpose({
  handleEmoji,
  afterUpdata
})

// 初始化index
</script>

<template>
  <div class="InputBox">
    <!-- 此处注意写法v-model:content -->
    <QuillEditor
      ref="myQuillEditor"
      theme="snow"
      v-model:content="content"
      :options="data.editorOption"
      contentType="html"
      @update:content="setValue()"
      @click="updateLength"
    />
  </div>
</template>

<style scoped lang="scss">
// 调整样式
.InputBox {
  width: 100%;
  height: 100%;
}
:deep(.ql-editor) {
  width: 100%;
  height: 100%;
}
</style>
