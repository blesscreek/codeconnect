<script setup>
import { ref, watch } from 'vue'
import { useTopicStore } from '@/stores'
const topicStore = useTopicStore()
const language = ref('javascript')
watch(language, () => {
  topicStore.setLanguage(language.value)
})
</script>
<template>
  <div class="toolbar">
    <div class="config">
      <el-select v-model="language" style="width: 150px">
        <el-option
          v-for="option in ['javascript', 'java', 'cpp', 'python']"
          :key="option"
          :label="option"
          :value="option"
        />
      </el-select>
    </div>
  </div>
  <code-editor :language="language"></code-editor>
</template>
<style lang="scss" scoped>
.toolbar {
  display: flex;
  align-items: center;
  width: 100%;
  height: 45px;
  background-color: #f0f0f0;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;

  // 切换语言的下拉框
  .config {
    height: 50%;
    margin-left: 10px;
    font-size: 18px;
    display: flex;
    align-items: center;
  }
}
</style>
