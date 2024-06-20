<!-- 首页 -->
<script setup>
const tableData = [
  {
    date: 'No.1',
    name: 'bless',
    rate: 100,
    pingjia: '太棒啦！'
  },
  {
    date: 'No.2',
    name: '张鱼小丸子',
    rate: 90,
    pingjia: '真不错！'
  },
  {
    date: 'No.3',
    name: 'baiyin',
    rate: 70,
    pingjia: '继续加油哦！'
  },
  {
    date: 'No.4',
    name: '显示不出来',
    rate: 70,
    pingjia: '差不多得了'
  }
]
</script>
<template>
  <div class="content">
    <div class="box1">
      <el-carousel height="383px" motion-blur>
        <el-carousel-item v-for="item in 4" :key="item">
          <img src="@/assets/轮播图.png" alt="" />
        </el-carousel-item>
      </el-carousel>
      <div class="welcome">
        <div class="span header" style="font-size: 26px">
          <span>ο(=•ω＜=)ρ⌒☆</span>
        </div>
        <div class="all">
          <div class="span">欢迎使用 CodeConnect !</div>
          <div class="span">快开始今日份的学习吧~</div>
          <div class="toTopic">
            每日打卡:
            <el-button type="success">每日一题~</el-button>
          </div>
          <div class="toTopic">
            挑战一下:
            <el-button type="danger">随机一题~</el-button>
          </div>
        </div>
        <div class="span bottom" style="font-size: 26px">
          <span>ο(=•ω＜=)ρ⌒☆</span>
        </div>
      </div>
    </div>
    <div class="box2">
      <div class="bulletin-board">
        <div class="span">
          <span>公告栏</span>
        </div>
        <ul>
          <li>萌新必看</li>
          <li>万人聊天室发言规则</li>
          <li>帮助文档</li>
          <li>题目提交规则</li>
        </ul>
      </div>
      <div class="team-management">
        <div class="span">
          <span>团队管理</span>
        </div>
        <div class="team">
          <div class="header">学习小队（32）</div>
          <div class="nav">
            <div style="background-color: #f0f0f0">成员排名</div>
            <div
              style="
                border-left: 1px solid #88d49b;
                border-right: 1px solid #88d49b;
              "
            >
              做题情况
            </div>
            <div>团队公告</div>
          </div>
          <!-- 成员排名的表格 -->
          <el-table :data="tableData" stripe style="width: 100%">
            <el-table-column
              prop="date"
              align="center"
              label="排名"
              width="180"
            />
            <el-table-column prop="name" label="昵称" width="160" />
            <el-table-column prop="rate" label="通过率" width="250">
              <template #default="{ row }">
                <div class="demo-progress">
                  <el-progress
                    v-if="row.rate >= 0 && row.rate < 60"
                    :text-inside="true"
                    :stroke-width="20"
                    :percentage="row.rate"
                    status="exception"
                  />
                  <el-progress
                    v-if="row.rate >= 60 && row.rate < 80"
                    :text-inside="true"
                    :stroke-width="22"
                    :percentage="row.rate"
                    status="warning"
                  />
                  <el-progress
                    v-if="row.rate > 80"
                    :text-inside="true"
                    :stroke-width="24"
                    :percentage="row.rate"
                    status="success"
                  />
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="pingjia" label="评价" />
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.content {
  width: 100%;
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
}
// 轮播图加欢迎
.box1 {
  min-width: 1200px;
  height: 383px;
  display: flex;
  background-color: #fff;
  .welcome {
    width: 300px;
    height: 100%;
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    .all {
      width: 100%;
      height: 283px;
      box-sizing: border-box;
      padding-top: 10px;
      .span {
        width: 100%;
        font-size: 24px;
        font-weight: 550;
        color: #c89b20;
        text-align: center;
        padding-top: 10px;
      }
      .toTopic {
        font-size: 19px;
        height: 70px;
        line-height: 90px;
        margin-left: 20px;
        .el-button {
          width: 120px;
          height: 40px;
          font-size: 18px;
          margin-left: 7px;
        }
      }
    }
    // 这个是欢迎的样式
    .header,
    .bottom {
      width: 100%;
      height: 50px;
      font-size: 24px;
      line-height: 50px;
      text-align: center;
      background-color: #4d68d5;
      color: #fff;
    }
  }
}
// 公告栏和团队管理
.box2 {
  width: 1200px;
  margin-top: 20px;
  display: flex;
  // 公告栏
  .bulletin-board {
    width: 320px;
    height: 700px;
    background-color: #fff;
  }
  ul {
    padding-left: 50px;
    li {
      font-size: 21px;
      height: 60px;
      line-height: 60px;
      color: #1e50da;
    }
    li:hover {
      cursor: pointer;
      color: #153383;
    }
  }
  .team-management {
    width: 860px;
    height: 700px;
    background-color: #fff;
    margin-left: 20px;
    .team {
      width: 800px;
      height: 260px;
      margin: 10px auto;
      box-sizing: border-box;
      border-radius: 12px;
      border: 1px solid #88d49b;
      .header {
        height: 40px;
        line-height: 40px;
        border-top-left-radius: 12px;
        border-top-right-radius: 12px;
        background-color: #88d49b;
        text-align: center;
        font-size: 20px;
      }
      .nav {
        width: 100%;
        height: 40px;
        display: flex;
        div {
          flex-grow: 1;
          height: 100%;
          text-align: center;
          line-height: 40px;
          font-size: 18px;
          color: #3b3b3b;
          cursor: pointer;
          border-bottom: 1px solid #88d49b;
        }
        div:hover {
          background-color: #f0f0f0;
          color: #000;
        }
      }
      // 成员排名的表格
      .el-table {
        margin-top: 1px;
      }
    }
  }
  .span {
    background-color: #dfdfdf;
    margin: 0;
    height: 40px;
    line-height: 40px;
    text-align: center;
    font-size: 24px;
  }
}

// 轮播图
.demonstration {
  color: var(--el-text-color-secondary);
}
.el-carousel {
  width: 900px;
}
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 350px;
  margin: 0;
  text-align: center;
}
.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}
.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>
