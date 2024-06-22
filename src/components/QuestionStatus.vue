<!-- 题目分数状态 -->
<script setup>
import { computed } from 'vue'
import { SemiSelect, Select } from '@element-plus/icons-vue'

const props = defineProps({
  status: Number
})
const status = computed(() => {
  console.log(props.status)
  return props.status
})
</script>
<template>
  <span v-if="status == 100">
    <el-icon color="#00bc03"><Select /></el-icon>
  </span>
  <span v-if="status < 100 && status >= 80">
    <el-text type="success" size="large">{{ status }}</el-text>
  </span>
  <span v-if="status < 80 && status >= 60">
    <el-text type="warning" size="large">{{ status }}</el-text>
  </span>
  <span v-if="status < 60 && status >= 0">
    <el-text type="danger" size="large">{{ status }}</el-text>
  </span>
  <span v-if="status < 0">
    <el-icon><SemiSelect /></el-icon>
  </span>
</template>
