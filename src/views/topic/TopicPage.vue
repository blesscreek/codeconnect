<script setup>
const tableData = [
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
  },
  {
    name: '肯德基',
    label: '疯狂星期四',
    id: 'vivo50',
    state: '我吃吃吃吃',
    situation: [50, 50]
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

// 点击题目跳到做题的页面
const goTopic = function (e) {
  console.log(e)
}
</script>

<template>
  <div class="content">
    <!-- 筛选 -->
    <el-header><search-question></search-question></el-header>

    <!-- 题目列表 -->
    <el-main>
      <el-table
        class="topicTable"
        :data="tableData"
        stripe
        @row-click="goTopic"
      >
        <el-table-column prop="state" label="状态" width="120" />
        <el-table-column prop="id" label="题号" width="150" />
        <el-table-column prop="name" label="题目名称" width="390">
          <template #default="{ row }">
            <router-link class="router-link-exact-active" to="">
              {{ row.name }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="label" label="算法" width="280" />
        <el-table-column prop="situation" label="通过率" width="240" />
      </el-table>
    </el-main>

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
</template>

<style lang="scss" scoped>
.content {
  width: 80%;
  margin: 0 auto;
}
.el-header {
  background-color: #fff;
  margin: 0 20px;
  height: 200px;
}
/*表格*/
.el-main {
  margin-bottom: 0;
  padding-bottom: 0;
}
.el-table {
  width: 100%;
  font-size: 17px;
  .router-link-exact-active {
    color: rgb(96, 98, 102);
    text-decoration: none;
  }
  .router-link-exact-active:hover {
    // color: #3498db;
    color: #007aff;
  }
  .router-link-exact-active:active {
    // color: #3498db;
    color: #005bbd;
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
  margin: 0 20px;
  height: 60px;
  margin-bottom: 30px;
  padding-right: 30px;
}
</style>
