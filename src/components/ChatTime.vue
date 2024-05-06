<template>
  <!-- 处理时间的 -->
  <span>{{ formatDate }}</span>
</template>

<script setup>
import { ref, computed } from 'vue'
const props = defineProps({
  time: Number
})
const time = ref(props.time)

const formatDate = computed(() => {
  let timeValue = new Date(time.value)
  let strtime = ''

  let todayTime = new Date()
  todayTime.setHours(0, 0, 0, 0)
  let dayDiff = Math.floor(
    (todayTime.getTime() - timeValue.getTime()) / (24 * 3600 * 1000)
  )
  if (timeValue.getTime() > todayTime.getTime()) {
    strtime =
      timeValue.getHours() <= 9
        ? '0' + timeValue.getHours()
        : timeValue.getHours()
    strtime += ':'
    strtime +=
      timeValue.getMinutes() <= 9
        ? '0' + timeValue.getMinutes()
        : timeValue.getMinutes()
  } else if (dayDiff < 1) {
    strtime = '昨天'
  } else if (dayDiff < 7) {
    strtime = `${dayDiff + 1}天前`
  } else {
    strtime = timeValue.getMonth() + 1 + '月' + timeValue.getDate() + '日'
  }

  return strtime
})
</script>
<style></style>
