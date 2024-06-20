<!-- 修改算法标签的列表 -->
<script setup>
import { reactive, ref } from 'vue'
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
  },
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
  },
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
const data = ref(tableData)
const formInline = reactive({
  difficulty: '',
  algorithm: '',
  date: ''
})

const onSubmit = () => {
  console.log('submit!')
}
// 表格的编辑
const handleEdit = (index, row) => {
  console.log(index, row)
}
const handleDelete = (index, row) => {
  console.log(index, row)
}
</script>

<template>
  <div class="content">
    <div class="searchLabel">
      <!-- 查询 -->
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="关键字：">
          <el-input
            class="input"
            v-model="formInline.user"
            placeholder="关键字、题号搜索"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
          <el-button type="primary" @click="onSubmit">添加标签</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 编辑表单 -->
    <el-table class="topicTable" :data="tableData" stripe>
      <el-table-column prop="date" label="名称" width="390" />
      <el-table-column prop="name" label="编号" width="390" />
      <el-table-column prop="address" label="操作">
        <template #default="scope">
          <el-button @click="handleEdit(scope.$index, scope.row)">
            编辑
          </el-button>

          <el-button
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div class="demo-pagination-block">
      <el-pagination
        v-model:page-size="pageSize2"
        :page-sizes="[10, 20, 30]"
        :small="false"
        :background="true"
        layout="sizes, prev, pager, next"
        :total="data.length"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.content {
  width: 100%;
  height: 100%;
}

.demo-form-inline .el-input {
  --el-input-width: 220px;
}

.demo-form-inline .el-select {
  --el-select-width: 220px;
}

.searchLabel {
  width: 100%;
  font-size: 18px;
  color: rgb(96, 98, 102);
  justify-content: start;
  .el-form {
    height: 70px;
    display: flex;
    align-items: center;
  }
  .el-form-item {
    margin-bottom: 0;
  }
  .input {
    width: 270px;
  }
}

/* 分页器 */
.demo-pagination-block + .demo-pagination-block {
  margin-top: 10px;
}
.demo-pagination-block .demonstration {
  margin-bottom: 16px;
}
.demo-pagination-block {
  display: flex;
  justify-content: flex-end;
  background-color: #fff;
  height: 60px;
  padding-right: 30px;
}
</style>
