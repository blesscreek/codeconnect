import { defineStore } from 'pinia'
import { ref } from 'vue'
// 用户模块 token相关
export const useTopicStore = defineStore('oj-Topic', () => {
  const content = ref('')
  const language = ref('')
  const setLanguage = (val) => {
    language.value = val
  }
  const setContent = (val) => {
    content.value = val
  }
  return {
    content,
    setContent,
    language,
    setLanguage
  }
})
