<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <!-- 头部导航 -->
        <!-- 可以用插槽，判断是不是管理员，然后显示对应的内容 -->
        <!-- 组件 -->
        <head-nav></head-nav>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>
<style scoped>
.el-container {
  height: 100vh;
}
.el-header {
  padding: 0;
  background-color: #fff;
}
.el-main {
  height: 100%;
}
</style>
