<!-- 右键消息的菜单 -->
<script setup>
const props = defineProps({
  data: Object
})
</script>
<template>
  <div
    class="box"
    :style="{ left: props.data.clientX + 'px', top: props.data.clientY + 'px' }"
  >
    <div class="delete">删除</div>
    <div class="withdraw" v-show="props.data.mine">撤回</div>
  </div>
</template>
<style lang="scss" scoped>
.box {
  position: fixed;
  width: 70px;
  background-color: #ffffff;
  border: 1px solid #dddddd;
  div {
    width: 100%;
    height: 40px;
    line-height: 40px;
    padding-left: 12px;
    box-sizing: border-box;
  }
  div:hover {
    cursor: pointer;
    background-color: #dddddd;
  }
}
</style>
