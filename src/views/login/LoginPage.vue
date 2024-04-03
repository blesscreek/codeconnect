<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref, watch } from 'vue'
import { userLoginService, userRegisterService } from '@/api/user.js'
import { useUserStore } from '@/stores'
import { useRouter } from 'vue-router'
// 在eslintrc中全局配置了还要引入吗？？？
import { ElMessage } from 'element-plus'
const userStore = useUserStore()
const isRegister = ref(false)
const form = ref()
const rememberCheck = ref(false)
const router = useRouter()
// 登录注册的表单内容
const formModel = ref({
  account: '',
  password: '',
  checkPassword: ''
})

// 表单的验证规则
const rules = {
  account: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, max: 10, message: '用户名是5-10位的字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^\S{6,15}$/,
      message: '密码必须是6-15位的非空字符',
      trigger: 'blur'
    }
  ],
  checkPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^\S{6,15}$/,
      message: '密码必须是6-15位的非空字符',
      trigger: 'blur'
    },
    {
      // rule 当前校验规则相关的信息
      // value 所校验的表单元素目前的表单值
      // callback 无论成功还是失败都需要callback回调
      // callback()成功
      // callback(new Error(错误信息)) 校验失败
      validator: (rule, value, callback) => {
        if (value !== formModel.value.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          //校验成功也需要回调
          callback()
        }
      },
      message: '两次输入密码不一致',
      trigger: 'blur'
    }
  ]
}

// 记住我相关
if (isRegister.value === false) {
  if (userStore.remember) {
    rememberCheck.value = userStore.getRemember()
    formModel.value = userStore.getAccount()
  }
}
// 登录注册切换
watch(isRegister, () => {
  formModel.value = { account: '', password: '', checkPassword: '' }
})

// 注册
const register = async () => {
  await form.value.validate()
  await userRegisterService(formModel.value)
  Element.success('注册成功')
  isRegister.value = false
}
// 登录
const login = async () => {
  // 组件自带的表单验证
  await form.value.validate()
  console.log(formModel.value)
  const res = await userLoginService(formModel.value)
  userStore.setToken(res.data.token)
  ElMessage.success('登录成功')
  // 是否记得账号密码
  if (rememberCheck.value) {
    userStore.setRemember(true)
    userStore.setAccount(formModel.value)
  } else {
    userStore.setRemember(false)
    userStore.setAccount({})
  }
  router.push('/')
}
</script>

<template>
  <el-row class="login-page">
    <el-col :span="6" :offset="8" class="form">
      <el-form
        :model="formModel"
        :rules="rules"
        ref="form"
        size="large"
        autocomplete="off"
        v-if="isRegister"
      >
        <!-- 注册 -->
        <el-form-item>
          <h1>注册</h1>
        </el-form-item>
        <el-form-item prop="account">
          <el-input
            :prefix-icon="User"
            v-model="formModel.account"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            :prefix-icon="Lock"
            type="password"
            v-model="formModel.password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item prop="checkPassword">
          <el-input
            :prefix-icon="Lock"
            type="password"
            v-model="formModel.checkPassword"
            placeholder="请输入再次密码"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            @click="register"
            class="button"
            type="primary"
            auto-insert-space
          >
            注册
          </el-button>
        </el-form-item>
        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="isRegister = false">
            ← 返回
          </el-link>
        </el-form-item>
      </el-form>
      <el-form
        :model="formModel"
        :rules="rules"
        ref="form"
        size="large"
        autocomplete="off"
        v-else
      >
        <!-- 登录 -->
        <el-form-item>
          <h1>登录</h1>
        </el-form-item>
        <el-form-item prop="account">
          <el-input
            :prefix-icon="User"
            v-model="formModel.account"
            placeholder="请输入用户名"
          >
            <img src="" alt="" />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            name="password"
            :prefix-icon="Lock"
            type="password"
            v-model="formModel.password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item class="flex">
          <div class="flex">
            <el-checkbox v-model="rememberCheck">记住我</el-checkbox>
            <el-checkbox v-model="freeCheck">七天免密登录</el-checkbox>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button
            class="button"
            type="primary"
            auto-insert-space
            @click="login"
            >登录</el-button
          >
        </el-form-item>
        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="isRegister = true">
            注册 →
          </el-link>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style lang="scss" scoped>
.login-page {
  height: 100vh;
  background-color: #fff;

  .form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    user-select: none;

    .title {
      margin: 0 auto;
    }

    .button {
      width: 100%;
    }

    .flex {
      width: 100%;
      display: flex;
      justify-content: space-between;
    }
  }
}
</style>
