<script setup>
import { ref } from 'vue'
import { getQuestionListService } from '@/api/topic.js'
import { SemiSelect, Select } from '@element-plus/icons-vue'
const data = ref([
  {
    passingRate: '70',
    tags: ['循环结构', '分支结构'],
    difficulty: '困难',
    title: '46546546',
    questionNum: 'Q3',
    status: 90
  }
])
const total = ref(0)

// 要发请求的数据
const pageSizeData = ref({ pageNo: 1, pageSize: 11 })
const searchData = ref({ difficulty: '', tags: [], keyword: '' })
// 是否显示算法标签
const isShowTags = ref(false)

// 查找题目列表
const getTopicList = async () => {
  console.log(pageSizeData, searchData)
  const res = await getQuestionListService(pageSizeData.value, searchData.value)
  console.log(res.data)
  data.value = res.data.questionResturn
  // 将通过率改成数字类型
  for (let i of data.value) {
    i.passingRate = parseInt(i.passingRate)
  }
  total.value = res.data.questionCnt
}
getTopicList()

// 搜索后处理数据再发请求
const getSearchData = (val) => {
  searchData.value.difficulty = val.difficulty
  searchData.value.tags = []
  if (val.algorithm) searchData.value.tags.push(val.algorithm)
  searchData.value.keyword = val.date
  console.log(searchData)
  getTopicList()
}

//分页相关
// 分页器变化发请求
const handleSizeChange = (val) => {
  console.log(val)
  pageSizeData.value.pageSize = val
  getTopicList()
}
const handleCurrentChange = (val) => {
  console.log(val)
  pageSizeData.value.pageNo = val
  getTopicList()
}

// 点击题目跳到做题的页面
const goTopic = function (e) {
  console.log(e)
}

// 题目的tag标签--难度，算法
const onChangeTag = (val) => {
  console.log(val)
}
</script>

<template>
  <div class="content">
    <!-- 筛选 -->
    <el-header>
      <search-question
        :total="total"
        @onSearch="getSearchData"
      ></search-question>
    </el-header>

    <!-- 题目列表 -->
    <el-main>
      <el-table class="topicTable" :data="data" stripe @row-click="goTopic">
        <el-table-column
          align="center"
          class="status"
          prop="status"
          label="状态"
          max-width="100"
        >
          <template #default="{ row }">
            <span v-if="row.status == 100">
              <el-icon color="#00bc03"><Select /></el-icon>
            </span>
            <span v-if="row.status < 100 && row.status >= 80">
              <el-text type="success" size="large">{{ row.status }}</el-text>
            </span>
            <span v-if="row.status < 80 && row.status >= 60">
              <el-text type="warning" size="large">{{ row.status }}</el-text>
            </span>
            <span v-if="row.status < 60 && row.status >= 0">
              <el-text type="danger" size="large">{{ row.status }}</el-text>
            </span>

            <span v-if="row.status < 0">
              <el-icon><SemiSelect /></el-icon>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="questionNum" label="题号" max-width="150" />
        <el-table-column prop="title" label="题目名称" min-width="300">
          <template #default="{ row }">
            <router-link
              class="router-link-exact-active"
              :to="'/topic/detail?qid=' + row.questionNum.split('Q')[1]"
            >
              {{ row.title }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column
          align="left"
          prop="difficulty"
          label="难度"
          max-width="80"
        >
          <template #default="{ row }">
            <div class="gap">
              <el-check-tag
                v-if="row.difficulty == '简单'"
                :checked="true"
                type="success"
                @change="onChangeTag(row.difficulty)"
              >
                简单
              </el-check-tag>
              <el-check-tag
                :checked="true"
                @change="onChangeTag(row.difficulty)"
                type="warning"
                v-if="row.difficulty == '中等'"
              >
                中等
              </el-check-tag>
              <el-check-tag
                :checked="true"
                @change="onChangeTag(row.difficulty)"
                type="danger"
                v-if="row.difficulty == '困难'"
              >
                困难
              </el-check-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column align="left" prop="tags" label="算法" min-width="240">
          <template #header>
            <el-switch v-model="isShowTags" active-text="显示算法标签" />
          </template>
          <template #default="{ row }">
            <div v-show="isShowTags" class="gap-2">
              <el-check-tag
                v-for="(x, i) in row.tags"
                :key="i"
                :checked="true"
                type="primary"
                @change="onChangeTag(row.difficulty)"
              >
                {{ x }}
              </el-check-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="passingRate" label="通过率" min-width="140">
          <template #default="{ row }">
            <div class="demo-progress">
              <el-progress
                v-if="row.passingRate >= 0 && row.passingRate < 60"
                :text-inside="true"
                :stroke-width="20"
                :percentage="row.passingRate"
                status="exception"
              />
              <el-progress
                v-if="row.passingRate >= 60 && row.passingRate < 80"
                :text-inside="true"
                :stroke-width="22"
                :percentage="row.passingRate"
                status="warning"
              />
              <el-progress
                v-if="row.passingRate >= 80"
                :text-inside="true"
                :stroke-width="24"
                :percentage="row.passingRate"
                status="success"
              />
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-main>

    <!-- 分页器 -->
    <div class="demo-pagination-block">
      <el-pagination
        v-model:page-size="pageSizeData.pageSize"
        :page-sizes="[10, 20, 30]"
        :small="false"
        :background="true"
        layout="sizes, prev, pager, next"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.content {
  width: 1200px;
  margin: 0 auto;
}
.el-header {
  background-color: #fff;
  margin: 0 20px;
  height: 200px;
}
/*表格*/
.el-main {
  width: 100%;
  margin-bottom: 0;
  padding-bottom: 0;
}
// 关于表格的行高和自定义表头样式都在全局,还有算法标签
.el-table {
  width: 100%;
  font-size: 16px;

  // 视图
  .el-scrollbar__view,
  .el-table__header {
    width: 100%;
  }

  //状态相关样式
  .status {
    font-weight: 600;
  }

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
