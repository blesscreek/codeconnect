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
import { ref } from 'vue'
//分页相关
const data = ref(tableData)
const pageSize2 = ref(10)
const handleSizeChange = (val) => {
  console.log(`${val} items per page`)
}
const handleCurrentChange = (val) => {
  console.log(`current page: ${val}`)
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
  <div class="box">
    <div class="header">
      <search-question></search-question>
    </div>
    <div class="content">
      <!-- 题目列表 -->
      <el-table class="topicTable" :data="tableData" stripe>
        <el-table-column prop="date" label="题目" width="190" />
        <el-table-column prop="name" label="题号" width="190" />
        <el-table-column prop="address" label="算法" />
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

      <!-- 分页器 -->
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
  </div>
</template>

<style lang="scss" scoped>
.header {
  width: 100%;
  height: 22vh;
  background-color: #fff;
  border-bottom: 1px solid #e3e3e3;
  box-sizing: border-box;
}
.content {
  width: 100%;
}
/*表格*/
.el-table {
  width: 100%;
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
