<script setup>
// import router from '@/router'
import { ref } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'
import { uploadTestPointsService } from '@/api/updata'
import { getQuestionListService } from '@/api/topic.js'

// 题目列表
// import { SemiSelect, Select } from '@element-plus/icons-vue'
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
const qId = ref(0)

// 要发请求的数据
const pageSizeData = ref({ pageNo: 1, pageSize: 11 })
const searchData = ref({ difficulty: '', tags: [], keyword: '' })
// 是否显示算法标签
const isShowTags = ref(false)

// 查找题目列表
const getTopicList = async () => {
  console.log(pageSizeData, searchData)
  // 题目列表信息
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

// 表格的编辑
const handleEdit = (index, row) => {
  console.log(index, row)
}
const handleUpdata = (index) => {
  qId.value = data.value[index].qid
  dialogFormVisible.value = true
}
const handleDelete = (index, row) => {
  console.log(index, row)
}

// 上传测试点相关
const dialogFormVisible = ref(false)
const upload = ref()

const onSelectFile = async (uploadFile) => {
  const isZip =
    uploadFile.type === 'application/zip' || uploadFile.name.endsWith('.zip')
  const isLt500K = uploadFile.size / 1024 / 1024 < 0.5
  if (!isZip) {
    ElMessage.error('上传文件只能是 ZIP 格式!')
    return
  }
  if (!isLt500K) {
    ElMessage.error('上传文件大小不能超过 500KB!')
    return
  }
  const file = new FormData()
  file.append('zipFileData', uploadFile)
  file.append('qid', qId.value)
  await uploadTestPointsService(file)
  dialogFormVisible.value = false
  ElMessage.success('上传成功')
}
</script>

<template>
  <div class="box">
    <div class="header">
      <search-question
        :total="total"
        @onSearch="getSearchData"
      ></search-question>
    </div>
    <!-- 添加题目 -->
    <div class="addTopic">
      <el-button
        type="primary"
        style="height: 37px; width: 95px"
        @click="$router.push('/manage/changetopic/add')"
        >添加题目</el-button
      >
    </div>
    <div class="content">
      <el-main>
        <el-table class="topicTable" :data="data" stripe @row-click="goTopic">
          <el-table-column prop="questionNum" label="题号" max-width="100" />
          <el-table-column prop="title" label="题目名称" min-width="200">
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
          <el-table-column
            align="left"
            prop="tags"
            label="算法"
            min-width="200"
          >
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
          <el-table-column prop="address" label="操作" min-width="200px">
            <template #default="scope">
              <el-button @click="handleEdit(scope.$index, scope.row)">
                编辑
              </el-button>
              <el-button
                type="primary"
                @click="handleUpdata(scope.$index, scope.row)"
              >
                上传测试点
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
  </div>
  <!-- 上传测试点 -->
  <el-dialog v-model="dialogFormVisible" title="上传测试点" width="500">
    <el-upload
      ref="upload"
      class="upload-demo"
      drag
      :limit="1"
      :before-upload="onSelectFile"
      accept=".zip"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">将文件拖到此处或 <em>单击上传</em></div>
      <template #tip>
        <div class="el-upload__tip">大小小于500kb的zip文件</div>
      </template>
    </el-upload>
  </el-dialog>
</template>

<style lang="scss" scoped>
.header {
  width: 100%;
  height: 22vh;
  background-color: #fff;
  border-bottom: 1px solid #e3e3e3;
  box-sizing: border-box;
}
.addTopic {
  width: 100%;
  height: 60px;
  display: flex;
  justify-content: end;
  align-items: center;
  .el-button {
    margin: 55px;
  }
}
.content {
  width: 100%;
}
/*表格*/
.el-table {
  width: 100%;
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
