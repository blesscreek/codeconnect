<script setup>
import { defineProps, defineEmits } from 'vue'
import { emoTextList, textToImg } from '@/utils/emoji'
import { ref } from 'vue'

const props = defineProps({
  pos: Object
})

const emit = defineEmits(['getEmoji'])

// 本地路径转为base64
const base64String = ref('')
const fileToBase64 = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = (error) => reject(error)
  })
}

const loadImageAsBase64 = async (filePath) => {
  try {
    const response = await fetch(filePath)
    const blob = await response.blob()
    base64String.value = await fileToBase64(blob)
  } catch (error) {
    console.error('转换失败：', error)
  }
}
loadImageAsBase64('/src/assets/emoji/0.gif') //所以让它先自己转一次

const clickHandler = async (emoText) => {
  // 直接返回url地址 ，可以直接渲染在富文本框里
  // 第一个转的就会有问题，不知道为什么
  let idx = emoTextList.indexOf(emoText)
  let url = `/src/assets/emoji/${idx}.gif`
  await loadImageAsBase64(url)
  url = base64String.value
  emit('getEmoji', url)
}
</script>

<template>
  <div
    class="emotion-box"
    :style="{ left: props.pos.x - 275 + 'px', top: props.pos.y - 230 + 'px' }"
  >
    <el-scrollbar style="height: 200px">
      <div class="emotion-item-list">
        <div
          class="emotion-item"
          v-for="(emoText, i) in emoTextList"
          :key="i"
          v-html="textToImg(emoText)"
          @click="clickHandler(emoText)"
        ></div>
      </div>
    </el-scrollbar>
    <span class="span2"> </span>
    <span class="span1"> </span>
  </div>
</template>

<style scoped lang="scss">
.emotion-box {
  position: fixed;
  width: 400px;
  box-sizing: border-box;
  padding: 5px;
  border: 1px solid #b3b3b3;
  border-radius: 5px;
  background-color: #f5f5f5;
  box-shadow: 0px 0px 10px #d9d9d9;
  .emotion-item-list {
    display: flex;
    flex-wrap: wrap;

    .emotion-item {
      width: 40px;
      height: 40px;
      text-align: center;
      cursor: pointer;
    }
  }
  .span2 {
    content: '';
    position: absolute;
    left: 185px;
    bottom: -30px;
    width: 0;
    height: 0;
    border-style: solid dashed dashed;
    border-color: #b3b3b3 transparent transparent;
    border-width: 15px;
  }
  .span1 {
    content: '';
    position: absolute;
    left: 185px;
    bottom: -29px;
    width: 0;
    height: 0;
    border-style: solid dashed dashed;
    border-color: #f5f5f5 transparent transparent;
    border-width: 15px;
  }
}
</style>
