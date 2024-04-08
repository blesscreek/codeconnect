<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
const route = useRoute()
const active = ref(
  '/' + route.path.split('/')[1] + '/' + route.path.split('/')[2]
)
console.log(active.value)
// 根据路由提示高亮
watch(route, () => {
  active.value = '/' + route.path.split('/')[1] + '/' + route.path.split('/')[2]
  console.log(active.value)
})
</script>

<template>
  <el-row class="tac">
    <img src="@/assets/logo.png" alt="" />
    <el-col>
      <el-menu
        :default-active="active"
        :router="true"
        class="el-menu-vertical-demo"
      >
        <el-menu-item index="/manage/changetopic">
          <span>编辑题目</span>
        </el-menu-item>
        <el-menu-item index="/manage/changelabel">
          <span>编辑标签</span>
        </el-menu-item>
        <el-menu-item index="/">
          <span>回到首页</span>
        </el-menu-item>
      </el-menu>
    </el-col>
  </el-row>
</template>

<style lang="scss" scoped>
img {
  width: 170px;
  margin: 20px auto;
}
.el-menu {
  border: none !important;
  span {
    font-size: 18px;
    display: block;
    padding-left: 20px;
  }
}
</style>
