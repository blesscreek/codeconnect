<template>
  <el-header>
    <!-- 选则 -->
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="关键字：">
        <el-input
          v-model="formInline.user"
          placeholder="关键字搜索"
          clearable
        />
      </el-form-item>
      <el-form-item label="难度：">
        <el-select v-model="formInline.region" placeholder="请选择难度">
          <el-option label="简单" value="简单" />
          <el-option label="难" value="难" />
          <el-option label="全部" value="全部" />
        </el-select>
      </el-form-item>
      <el-form-item label="算法：">
        <el-input
          v-model="formInline.user"
          placeholder="通过算法查询"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">Query</el-button>
      </el-form-item>
    </el-form>
  </el-header>
  <el-main>
    <!-- 题目列表 -->
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="date" label="Date" width="190" />
      <el-table-column prop="name" label="Name" width="190" />
      <el-table-column prop="address" label="Address" />
    </el-table>
  </el-main>
  <!-- 分页器 -->
  <el-pagination
    small
    background
    layout="prev, pager, next"
    :total="50"
    class="mt-4"
  />
</template>

<script setup>
const tableData = [
  {
    date: '2016-05-03',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles'
  },
  {
    date: '2016-05-02',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles'
  },
  {
    date: '2016-05-04',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles'
  },
  {
    date: '2016-05-01',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles'
  }
]

import { reactive } from 'vue'
const formInline = reactive({
  user: '',
  region: '',
  date: ''
})

const onSubmit = () => {
  console.log('submit!')
}

import request from '@/utils/request'
const getcc = async () => {
  const res = await request.get('/test/1')
  //   const res = await request.get('/my/cate/list')
  console.log(res)
}
getcc()
</script>
<style>
.demo-form-inline .el-input {
  --el-input-width: 220px;
}

.demo-form-inline .el-select {
  --el-select-width: 220px;
}
</style>
