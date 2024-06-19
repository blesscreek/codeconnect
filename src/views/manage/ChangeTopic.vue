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
// import router from '@/router'
import { ref } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'
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
const handleUpdata = (index, row) => {
  dialogFormVisible.value = true
}
const handleDelete = (index, row) => {
  console.log(index, row)
}

// 上传测试点相关
const dialogFormVisible = ref(false)
const fileList = ref([])

// const beforeUpload = (file) => {
//   const isZip = file.type === 'application/zip' || file.name.endsWith('.zip')
//   const isLt500K = file.size / 1024 / 1024 < 0.5

//   if (!isZip) {
//     this.$message.error('上传文件只能是 ZIP 格式!')
//   }
//   if (!isLt500K) {
//     this.$message.error('上传文件大小不能超过 500KB!')
//   }
//   return isZip && isLt500K
// }

// const handleSuccess = (response, file, fileList) => {
//   this.$message.success('上传成功')
// }

// const handleRemove = (file, fileList) => {
//   this.$message.info('文件已删除')
// }
</script>

<template>
  <div class="box">
    <div class="header">
      <search-question></search-question>
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
  <!-- 上传测试点 -->
  <el-dialog v-model="dialogFormVisible" title="上传测试点" width="500">
    <el-upload
      class="upload-demo"
      drag
      :limit="1"
      action="http://123.60.15.140:8080/uploadQuestion/uploadQuestionCase"
      multiple
      :before-upload="beforeUpload"
      :on-success="handleSuccess"
      :file-list="fileList"
      :on-remove="handleRemove"
      accept=".zip"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">将文件拖到此处或 <em>单击上传</em></div>
      <template #tip>
        <div class="el-upload__tip">大小小于500kb的zip文件</div>
      </template>
    </el-upload>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogFormVisible = false">
          确认
        </el-button>
      </div>
    </template>
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
