<script setup>
//浏览器代码编辑器，使用时要传递参数响应式参数language
// <code-editor :language="language"></code-editor>

import { reactive, shallowRef, onMounted, computed } from 'vue'
import { useTopicStore } from '@/stores'
import { javascript } from '@codemirror/lang-javascript'
import { java } from '@codemirror/lang-java'
import { cpp } from '@codemirror/lang-cpp'
import { python } from '@codemirror/lang-python'
// import { go } from '@codemirror/lang-go'
// src 里的那一堆ts
import { Codemirror } from '@/codets'
import { watch } from 'vue'

const topicStore = useTopicStore()
const languages = {
  JavaScript: javascript(),
  Java: java(),
  Cpp: cpp(),
  C: cpp(),
  Python: python()
  // Go: go()
}

const code = shallowRef(``)
const view = shallowRef()
const props = defineProps({
  language: String,
  content: String, //内容
  ban: Boolean //是否被禁止
})
const languageData = computed(() => {
  const result = props.language
  return result
})
const isBan = computed(() => {
  if (props.ban) return props.ban
  else return false
})
const config = reactive({
  disabled: isBan,
  indentWithTab: true,
  tabSize: 4,
  placeholder: 'input...',
  backgroundColor: 'red',
  language: languageData,
  theme: 'default',
  phrases: 'en-us'
})

console.log(languageData)
const extensions = computed(() => {
  const result = []
  result.push(languages[config.language])
  return result
})

const handleReady = (payload) => {
  console.log('handleReady payload:', payload)
}

onMounted(() => {
  console.log('mounted view:', view)
  // 初始化编辑器内容和禁用状态
  if (props.content) {
    code.value = props.content
  }
})
// 拿到代码内容属性并监听
const content = computed(() => {
  return props.content
})
watch(content, () => {
  code.value = props.content
  console.log(code.value)
})
const getContent = (e) => {
  topicStore.setContent(e)
}
</script>

<template>
  <div class="example" :language="language">
    <!-- <div class="toolbar">
      <div class="config">
        <el-select id="language" v-model="config.language" style="width: 150px">
          <el-option
            v-for="option in ['javascript', 'java', 'cpp', 'python']"
            :key="option"
            :label="option"
            :value="option"
          />
        </el-select>
      </div>
    </div> -->
    <div class="content">
      <codemirror
        class="codemirror"
        :placeholder="config.placeholder"
        :indentWithTab="config.indentWithTab"
        :tabSize="config.tabSize"
        :disabled="config.disabled"
        :style="{ backgroundColor: 'white' }"
        :phrases="{}"
        :extensions="extensions"
        v-model="code"
        @ready="handleReady"
        @change="getContent"
      />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.example {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: flex-start;
  .content {
    width: 100%;
    height: 88%;
    .codemirror {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
