<script setup>
//浏览器代码编辑器，使用时要传递参数响应式参数language
// <code-editor :language="language"></code-editor>

import { reactive, shallowRef, onMounted, computed } from 'vue'
import { useTopicStore } from '@/stores'
import { javascript } from '@codemirror/lang-javascript'
import { java } from '@codemirror/lang-java'
import { cpp } from '@codemirror/lang-cpp'
import { python } from '@codemirror/lang-python'
// src 里的那一堆ts
import { Codemirror } from '@/codets'

const topicStore = useTopicStore()
const languages = {
  javascript: javascript(),
  java: java(),
  cpp: cpp(),
  c: cpp(),
  python: python()
}

const code = shallowRef(``)
const view = shallowRef()
const language = defineProps({
  language: String
})
const languageData = computed(() => {
  const result = language.language
  return result
})
const config = reactive({
  disabled: false,
  indentWithTab: true,
  tabSize: 4,
  placeholder: 'input...',
  backgroundColor: 'red',
  language: languageData,
  theme: 'default',
  phrases: 'en-us'
})
console.log(config)

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
