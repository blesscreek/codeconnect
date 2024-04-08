<script setup>
import { Tickets, Key, Timer } from '@element-plus/icons-vue'
</script>

<template>
  <div class="content">
    <div class="header">
      <div>
        <el-icon><Tickets /></el-icon>&nbsp;题目描述
      </div>
      <span></span>
      <div>
        <el-icon><Key /></el-icon>&nbsp;题解
      </div>
      <span></span>
      <div>
        <el-icon><Timer /></el-icon>&nbsp;提交记录
      </div>
    </div>
    <div class="box">
      <div class="topic-name">
        <span>P1001.肯德基疯狂星期四</span>
      </div>
      <div class="topic-label">
        <div class="difficulty">难度</div>
        <div class="algorithm">是否显示算法标签</div>
        <!-- 历史分数 -->
        <div class="score">无</div>
      </div>

      <div class="topic-background">
        <span>题目背景</span>
        <div>可以没有,疯狂星期四就要v我50</div>
      </div>
      <div class="topic-description">
        <span>题目描述</span>
        <div>
          小渊和小轩是好朋友也是同班同学，他们在一起总有谈不完的话题。一次素质拓展活动中，班上同学安排坐成一个
          m 行 n
          列的矩阵，而小渊和小轩被安排在矩阵对角线的两端，因此，他们就无法直接交谈了。幸运的是，他们可以通过传纸条来进行交流。纸条要经由许多同学传到对方手里，小渊坐在矩阵的左上角，坐标
          ( 1 , 1 ) (1,1)，小轩坐在矩阵的右下角，坐标 ( , )
          (m,n)。从小渊传到小轩的纸条只可以向下或者向右传递，从小轩传给小渊的纸条只可以向上或者向左传递。
        </div>
      </div>
      <div class="format">
        <span>输入描述</span>
        <div>
          第一行有两个用空格隔开的整数 m 和 n，表示班里有 m 行 n 列。 接下来的 m
          行是一个 × m×n 的矩阵，矩阵中第 i 行 j 列的整数表示坐在第 i 行 j
          列的学生的好心程度。每行的 n 个整数之间用空格隔开。
        </div>
        <span>输出描述</span>
        <div>
          输出文件共一行一个整数，表示来回两条路上参与传递纸条的学生的好心程度之和的最大值。
        </div>
      </div>
      <div class="example">
        <span>输入样例</span>
        <div></div>
        <span>输出样例</span>
        <div></div>
      </div>
      <div class="prompt">
        <span>提示</span>
        <div>
          【数据范围】 对于 30 % 30% 的数据，满足 1 ≤ , ≤ 10 1≤m,n≤10。 对于 100
          % 100% 的数据，满足 1 ≤ , ≤ 50 1≤m,n≤50。
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.content {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  .header {
    width: 100%;
    height: 6%;
    background-color: #ececec;
    border-top-left-radius: 20px;
    border-top-right-radius: 20px;
    display: flex;
    justify-content: start;
    align-items: center;
    padding-left: 25px;
    // 左侧头部题目描述等的样式
    span {
      width: 2px;
      height: 50%;
      border-radius: 1px;
      background-color: #d3d3d3;
    }
    div {
      // width: 90px;
      height: 80%;
      padding: 1px 10px;
      border-radius: 7px;
      display: flex;
      align-items: center;
      justify-content: center;
      .el-icon {
        color: #3b82f680;
      }
    }
    // 应该是点击后换颜色
    div:hover {
      cursor: pointer;
      background-color: #dbdbdb;
      .el-icon {
        color: #3b82f6;
      }
    }
  }
}
.box {
  width: 100%;
  height: 95%;
  padding: 0 2%;
  overflow-y: auto;
}
// 标题
.topic-name {
  width: 100%;
  height: 45px;
  margin-top: 20px;
  font-size: 30px;
  font-weight: 550;
}
// 标签
.topic-label {
  width: 100%;
  height: 40px;
  display: flex;
  align-items: center;
  .difficulty {
    width: 60px;
    height: 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 13px;
    color: #f21313;
    background-color: #e4e4e4;
  }
  .algorithm {
    width: 150px;
    height: 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 13px;
    color: #2b2b2b;
    background-color: #e4e4e4;
    margin: 0 15px;
  }
  .score {
    width: 50px;
    height: 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 13px;
    color: #2b2b2b;
    background-color: #e4e4e4;
  }
}
// 题目背景
.topic-background {
  margin-top: 20px;
  span {
    font-size: 21px;
    font-weight: 550;
  }
  div {
    margin-top: 15px;
    color: #2b2b2b;
  }
}
// 题目描述
.topic-description {
  margin-top: 10px;
  span {
    font-size: 21px;
    font-weight: 550;
  }
  div {
    margin-top: 15px;
    color: #2b2b2b;
  }
}
// 输入输出描述
.format {
  span {
    display: block;
    margin-top: 10px;
    font-size: 21px;
    font-weight: 550;
  }
  div {
    margin-top: 15px;
    color: #2b2b2b;
  }
}
// 输入输出样例
.example {
  span {
    display: block;
    margin-top: 10px;
    font-size: 21px;
    font-weight: 550;
  }
  div {
    margin-top: 15px;
    color: #2b2b2b;
  }
}
// 提示
.prompt {
  margin-top: 10px;
  span {
    font-size: 21px;
    font-weight: 550;
  }
  div {
    margin-top: 15px;
    color: #2b2b2b;
  }
}
</style>
