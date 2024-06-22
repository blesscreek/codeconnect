<!-- 封装题目难度，在题目列表和题目详情可以用到 -->
<script setup>
import { computed } from 'vue'

const props = defineProps({
  difficulty: String
})
const difficulty = computed(() => {
  return props.difficulty
})
const onChangeTag = (x) => {
  console.log(x)
}
</script>
<template>
  <div class="gap">
    <el-check-tag
      v-if="difficulty == '简单'"
      :checked="true"
      type="success"
      @change="onChangeTag(difficulty)"
    >
      简单
    </el-check-tag>
    <el-check-tag
      v-if="difficulty == '中等'"
      :checked="true"
      @change="onChangeTag(difficulty)"
      type="warning"
    >
      中等
    </el-check-tag>
    <el-check-tag
      v-if="difficulty == '困难'"
      :checked="true"
      @change="onChangeTag(difficulty)"
      type="danger"
    >
      困难
    </el-check-tag>
  </div>
</template>
