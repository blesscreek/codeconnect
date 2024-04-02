<script setup>
// import { ref } from 'vue'
// const active = ref(1)
</script>

<template>
  <el-row class="tac">
    <img src="@/assets/logo.png" alt="" />
    <el-col>
      <el-menu
        default-active="changetopic"
        :router="true"
        class="el-menu-vertical-demo"
      >
        <el-menu-item index="addtopic">
          <span>添加题目</span>
        </el-menu-item>
        <el-menu-item index="changetopic">
          <span>编辑题目</span>
        </el-menu-item>
        <el-menu-item index="changelabel">
          <span>编辑标签</span>
        </el-menu-item>
        <el-menu-item index="back">
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
