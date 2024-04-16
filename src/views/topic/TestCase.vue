<script setup>
import { ref } from 'vue'
// 测试用例相关
const textarea = ref('3 3 5\n1 2 3\n4 5 6\n7 8 9')
</script>
<template>
  <div class="content">
    <div class="head">
      <div class="test">
        <span>测试用例</span>
        <span>运行结果</span>
      </div>
      <!-- 提交 -->
      <div class="run-submit">
        <el-button color="#ededed" class="run" type="info" text bg>
          <img src="@/assets/play.svg" alt="" />
          <div style="padding-left: 5px">运行</div>
        </el-button>
        <el-button class="submit" type="info" text bg>
          <img src="@/assets/submit.svg" alt="" />
          <div style="padding-left: 5px">提交</div>
        </el-button>
      </div>
    </div>
    <div class="box">
      <div class="case">测试样例</div>
      <el-input
        v-model="textarea"
        autosize
        type="textarea"
        placeholder="Please input"
      />
    </div>
  </div>
</template>
<style lang="scss" scoped>
.content {
  width: 100%;
  height: 100%;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
  overflow: hidden;
}
.head {
  width: 100%;
  height: 40px;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
  background-color: #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  .test {
    margin-left: 20px;
    display: flex;
    span {
      color: #4a4a4a;
      margin: 0 10px;
      cursor: pointer;
    }
    span:hover {
      color: #000;
    }
  }
}
// 提交和运行按钮
.run-submit {
  height: 100%;
  margin-right: 20px;

  display: flex;
  justify-content: center;
  align-items: center;
  .el-button {
    border: none;
    width: 100px;
    height: 80%;
    margin: 0 1px;
    font-size: 17px;
  }
  .run {
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
    color: #323232;
    img {
      width: 25px;
    }
  }
  .submit {
    border-top-left-radius: 0;
    border-bottom-left-radius: 0;
    color: rgb(0, 179, 39);
    img {
      width: 25px;
    }
  }
}

.box {
  width: 100%;
  height: 85%;
  padding-left: 30px;
  box-sizing: border-box;
  .case {
    width: 100%;
    height: 40px;
    line-height: 40px;
  }
  textarea {
    // 设置没有边框和不可拖拽改变大小，现在没有效果
    outline: none;
    resize: none;
  }
}
</style>
