<script setup>
import { ref } from 'vue'

const props = defineProps({
  url: String
})
const isUrl = ref(false)
if (props.url) {
  isUrl.value = true
}
</script>
<template>
  <div class="head-sculpture">
    <el-avatar v-if="isUrl" :src="props.url" />
    <img v-else src="@/assets/head-sculpture.jpg" />
  </div>
</template>
<style lang="scss" scoped>
.head-sculpture {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  .el-avatar {
    width: 100%;
    height: 100%;
  }
  img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
  }
}
</style>
