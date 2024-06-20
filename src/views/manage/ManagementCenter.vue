<!-- 管理中心大框架 -->
<script setup>
import AsideNav from './AsideNav.vue'
</script>
<template>
  <div class="common-layout">
    <el-container>
      <el-aside width="250px"><aside-nav></aside-nav></el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>
<style lang="scss" scoped>
.common-layout {
  width: 100vw;
  margin: 0;
  padding: 0;
}
.el-container {
  width: 80vw;
  height: 100vh;
  padding: 0;
  margin: 0 auto;
  // margin-top: 2vh;
}
.el-aside {
  background-color: #fff;
}
.el-main {
  background-color: #ffffff;
  box-sizing: border-box;
  border-left: 1px solid #e3e3e3;
}
</style>
