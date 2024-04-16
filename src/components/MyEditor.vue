<script setup>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { reactive, onMounted, ref, toRaw } from 'vue'
import { addTopicPictureService } from '@/api/topic.js'

// const props = defineProps(['value'])
const emit = defineEmits(['updateValue'])
const content = ref('')
const myQuillEditor = ref()
// 通过watch监听回显，笔者这边使用v-model:content 不能正常回显
// watch(
//   () => props.value,
//   (val) => {
//     toRaw(myQuillEditor.value).setHTML(val)
//   },
//   { deep: true }
// )
const fileBtn = ref()
const data = reactive({
  content: '',
  editorOption: {
    modules: {
      toolbar: [
        ['bold', 'italic', 'underline', 'strike'],
        [{ size: ['small', false, 'large', 'huge'] }],
        [{ align: [] }],
        [{ list: 'ordered' }, { list: 'bullet' }],
        [{ indent: '-1' }, { indent: '+1' }],
        [{ header: 1 }, { header: 2 }],
        ['image'],
        [{ color: [] }, { background: [] }]
      ]
    },
    placeholder: '请输入内容...'
  }
})
// watch(
//   () => data.content,
//   () => {
//     emit('updateValue', data.content)
//   },
//   { deep: true }
// )
const imgHandler = (state) => {
  if (state) {
    fileBtn.value.click()
  }
}
// 抛出更改内容，此处避免出错直接使用文档提供的getHTML方法
const setValue = () => {
  const text = toRaw(myQuillEditor.value).getHTML()
  emit('updateValue', text)
}

// 提交图片时要转成网络链接
const handleUpload = async (e) => {
  const files = Array.prototype.slice.call(e.target.files)
  // console.log(files, "files")
  if (!files) {
    return
  }
  const formdata = new FormData()
  console.log(files[0])
  formdata.append('imagedata', files[0])
  // backsite 是一个命名为 uploadFile 的后端接口的调用者
  // 通常情况下，backsite 可能是您的后端服务的一个对象或模块，
  // 用于处理与后端的通信，例如发送 HTTP 请求以上传文件或获取数据
  // 在这段代码中，backsite.uploadFile(formdata) 调用了一个上传文件的后端接口，并传递了一个 FormData 对象作为参数，用于上传文件到服务器。
  const res = await addTopicPictureService(formdata)
  // res.data 就是url
  if (res.data.url) {
    const quill = toRaw(myQuillEditor.value).getQuill()
    const length = quill.getSelection().index
    quill.insertEmbed(length, 'image', res.data.url)
    quill.setSelection(length + 1)
  }
}
// 初始化编辑器
onMounted(() => {
  const quill = toRaw(myQuillEditor.value).getQuill()
  if (myQuillEditor.value) {
    quill.getModule('toolbar').addHandler('image', imgHandler)
  }
})
</script>

<template>
  <div>
    <!-- 此处注意写法v-model:content -->
    <QuillEditor
      ref="myQuillEditor"
      theme="snow"
      v-model:content="content"
      :options="data.editorOption"
      contentType="html"
      @update:content="setValue()"
    />
    <!-- 使用自定义图片上传 -->
    <input
      type="file"
      hidden
      accept=".jpg,.png"
      ref="fileBtn"
      @change="handleUpload"
    />
  </div>
</template>

<style scoped lang="scss">
// 调整样式
:deep(.ql-editor) {
  min-height: 180px;
}
:deep(.ql-formats) {
  height: 21px;
  line-height: 21px;
}
</style>
