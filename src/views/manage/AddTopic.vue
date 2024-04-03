<script setup>
import { ref } from 'vue'

// do not use same name with ref
const form = ref({
  name: '',
  region: '',
  date1: '',
  date2: '',
  delivery: false,
  type: [],
  resource: '',
  desc: ''
})

const onSubmit = () => {
  console.log('submit!')
}

// 富文本
</script>

<template>
  <div class="addTopicContent">
    <div class="span">添加题目</div>
    <el-form :model="form" label-width="auto" style="max-width: 800px">
      <el-form-item label="题目名称">
        <el-input v-model="form.name" />
      </el-form-item>

      <el-form-item label="说明/提示">
        <div class="editor">
          <!-- 局部定义的富文本组件 -->
          <my-editor></my-editor>
        </div>
      </el-form-item>
      <el-form-item label="题目背景">
        <div class="editor">
          <!-- 局部定义的富文本组件 -->
          <my-editor></my-editor>
        </div>
      </el-form-item>
      <el-form-item label="题目描述">
        <div class="editor">
          <!-- 局部定义的富文本组件 -->
          <my-editor></my-editor>
        </div>
      </el-form-item>

      <!-- 判断是否正确 -->
      <el-form-item>
        <el-form-item label="时间限制">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="内存限制">
          <el-input v-model="form.name" />
        </el-form-item>
      </el-form-item>

      <el-form-item
        ><el-form-item label="输入格式">
          <el-input v-model="form.desc" type="textarea" />
        </el-form-item>
        <el-form-item label="输出格式">
          <el-input v-model="form.desc" type="textarea" />
        </el-form-item>
      </el-form-item>

      <el-form-item>
        <el-form-item label="输入样例">
          <el-input v-model="form.desc" type="textarea" />
        </el-form-item>
        <el-form-item label="输出样例">
          <el-input v-model="form.desc" type="textarea" />
        </el-form-item>
      </el-form-item>

      <el-form-item label="难度选择">
        <el-select v-model="form.region" placeholder="请选择难度">
          <el-option label="简单" value="简单" />
          <el-option label="中等" value="中等" />
          <el-option label="难" value="难" />
        </el-select>
      </el-form-item>

      <!-- <el-form-item label="Instant delivery">
      <el-switch v-model="form.delivery" />
    </el-form-item> -->
      <el-form-item label="算法标签">
        <el-checkbox-group v-model="form.type">
          <el-checkbox value="dfs" name="type"> 深搜 </el-checkbox>
          <el-checkbox value="bfs" name="type"> 广搜 </el-checkbox>
          <el-checkbox value="kfcvivo50" name="type"> 疯狂星期四 </el-checkbox>
          <el-checkbox value="wcop" name="type"> 不许玩原神 </el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="发布为：">
        <el-radio-group v-model="form.resource">
          <el-radio value="Sponsor">草稿</el-radio>
          <el-radio value="Venue">发布</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="Activity form">
        <el-input v-model="form.desc" type="textarea" />
      </el-form-item>
      <!-- 按钮 -->
      <el-form-item>
        <el-button type="primary" @click="onSubmit">Create</el-button>
        <el-button>Cancel</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style lang="scss" scoped>
.addTopicContent {
  width: 100%;
  height: 100%;
  .span {
    width: 100%;
    height: 60px;
    text-align: center;
    font-size: 25px;
    color: #355bcc;
  }
}
.el-form {
  margin: 0 auto;
}
</style>
