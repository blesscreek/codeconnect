<script setup>
import { reactive, ref } from 'vue'
const formInline = reactive({
  difficulty: '',
  algorithm: '',
  date: ''
})
const emit = defineEmits(['onSearch'])
const num = ref(100)

const onSubmit = () => {
  emit('onSearch', formInline)
}
</script>

<template>
  <div class="searchContent">
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="查询条件">
        <el-select placeholder="请选择难度" v-model="formInline.difficulty">
          <el-option label="简单" value="简单" />
          <el-option label="中等" value="中等" />
          <el-option label="困难" value="困难" />
        </el-select>
      </el-form-item>

      <el-form-item label="">
        <el-select placeholder="请选择算法标签" v-model="formInline.algorithm">
          <el-option label="循环结构" value="循环结构" />
          <el-option label="分支结构" value="分支结构" />
          <el-option label="顺序结构" value="顺序结构" />
          <el-option label="疯狂星期四" value="疯狂星期四" />
        </el-select>
      </el-form-item>
      <el-form-item label="关键字：">
        <el-input
          v-model="formInline.date"
          placeholder="关键字、题号搜索"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
    <div class="result">
      <div class="selected">已选择 你还没有选择任何标签哦</div>
      <div class="display">
        <div>
          共计<span style="color: #000">{{ ' ' + num + ' ' }} </span>条结果
        </div>
        <div class="clear">清空所有筛选条件</div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.demo-form-inline .el-input {
  --el-input-width: 220px;
}

.demo-form-inline .el-select {
  --el-select-width: 220px;
}

.searchContent {
  width: 100%;
  height: 100%;
  font-size: 18px;
  color: rgb(96, 98, 102);
  .el-form {
    width: 100%;
    height: 50%;
    display: flex;
    align-items: center;
    justify-content: start;
    flex-wrap: wrap;
  }
  .el-form-item {
    margin-bottom: 0;
  }
  .result {
    width: 100%;
    height: 50%;
    display: flex;
    flex-wrap: wrap;
    div {
      width: 100%;
    }
    .display {
      width: 100%;
      display: flex;
      .clear {
        color: rgb(64, 158, 255);
        cursor: pointer;
        text-align: end;
        padding-right: 5%;
      }
      .clear:hover {
        color: rgb(54, 125, 195);
      }
    }
  }
}
</style>
