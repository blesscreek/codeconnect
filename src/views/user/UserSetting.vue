<script setup>
import { reactive, ref } from 'vue'
import { useUserStore } from '@/stores'
import { userUpdateService } from '@/api/user.js'
import { uploadImageService } from '@/api/updata.js'
// 拿到用户数据
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
const userStore = useUserStore()
userStore.getUserInfoServer()
const userInfo = userStore.userInfo

// 表单数据和验证
const ruleFormRef = ref()
let {
  age,
  headImage,
  headImageThumb,
  id,
  nickname,
  online,
  sex,
  signature,
  username
} = userInfo
const obj = {
  age,
  headImage,
  headImageThumb,
  id,
  nickname,
  online,
  sex,
  signature,
  username
}
const ruleForm = reactive(obj)
// 性别处理成字符串
ruleForm.sex = '' + ruleForm.sex

const rules = reactive({
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 1, max: 7, message: '昵称不得多于七个字符', trigger: 'blur' }
  ],
  age: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入年龄'))
        } else if (!Number.isInteger(value)) {
          callback(new Error('年龄应为数字'))
        } else {
          if (value > 120 || value < 0) {
            callback(new Error('年龄应小于120并且大于0'))
          }
        }
        callback()
      },
      trigger: 'blur'
    }
  ],
  signature: [{ max: 30, message: '个性签名不能多于30字', trigger: 'blur' }]
})

// 上传用户头像相关

const uploadRef = ref()

const imgFile = ref(null)

const onSelectFile = (uploadFile) => {
  // 基于 FileReader 读取图片做预览，转为base64
  const reader = new FileReader()
  // 存一下文件
  imgFile.value = uploadFile.raw
  reader.readAsDataURL(uploadFile.raw)
  reader.onload = () => {
    ruleForm.headImage = reader.result
  }
}
// 提交修改
const submit = async () => {
  // 如果修改了头像 先提交头像
  if (imgFile.value) {
    const file = new FormData()
    file.append('file', imgFile.value)
    const img = await uploadImageService(file)
    console.log(img)
    ruleForm.headImage = img.data.url
    ruleForm.headImageThumb = img.data.thumbUrl
  }
  // 再提交全部的修改
  ruleForm.sex = parseInt(ruleForm.sex)
  const res = await userUpdateService(ruleForm)
  console.log(res)
  ElMessage.success('修改成功')
  userStore.getUserInfoServer()
}
</script>

<template>
  <div class="setting">
    <div class="span">修改个人信息</div>
    <el-form
      ref="ruleFormRef"
      style="min-width: 600px"
      :model="ruleForm"
      :rules="rules"
      label-width="auto"
    >
      <el-form-item label="头像">
        <el-upload
          ref="uploadRef"
          class="avatar-uploader"
          :show-file-list="false"
          :auto-upload="false"
          :on-change="onSelectFile"
        >
          <img
            v-if="ruleForm.headImage"
            :src="ruleForm.headImage"
            class="avatar"
          />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>

      <el-form-item label="用户名">
        <el-input v-model="ruleForm.username" disabled />
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="ruleForm.nickname" />
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input v-model.number="ruleForm.age" />
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-radio-group v-model="ruleForm.sex">
          <el-radio value="1">女</el-radio>
          <el-radio value="0">男</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="个性签名" prop="signature">
        <el-input v-model.number="ruleForm.signature" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit()">修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style lang="scss" scoped>
.setting {
  width: 1200px;
  //   margin: 15px;
  background-color: #fff;
  .span {
    width: 100%;
    height: 40px;
    font-size: 19px;
    margin-left: 30px;
    margin-top: 20px;
  }
  .el-form {
    width: 800px;
    margin: 0 auto;
    margin-top: 20px;
    .el-button {
      margin: 0 auto;
      width: 90px;
      height: 35px;
      font-size: 17px;
    }
  }
}

/*上传用户头像*/
.avatar-uploader {
  :deep() {
    .avatar {
      width: 278px;
      height: 278px;
      display: block;
    }
    .el-upload {
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: var(--el-transition-duration-fast);
    }
    .el-upload:hover {
      border-color: var(--el-color-primary);
    }
    .el-icon.avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 278px;
      height: 278px;
      text-align: center;
    }
  }
}
</style>
