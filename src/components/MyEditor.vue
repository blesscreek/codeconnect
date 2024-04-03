<script setup>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { ref } from 'vue'
const formModel = ref({
  content: ''
})
const editorRef = ref()
</script>
<template>
  <quill-editor
    ref="editorRef"
    v-model:content="formModel.content"
    content-type="html"
    theme="snow"
  ></quill-editor>
</template>
<style lang="scss" scoped>
.editor {
  width: 100%;
  height: 300px;
  /* 富文本框会和下面的东西重叠，不知道为什么，加底边太暴力了 */
  margin-bottom: 50px;
  :deep(.ql-editor) {
    min-height: 200px;
  }
}
</style>
