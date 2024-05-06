<script setup>
import { ref, computed } from 'vue'
import { transform } from '@/utils/emoji.js'
import { Document } from '@element-plus/icons-vue'
const props = defineProps({
  msgInfo: Object,
  mine: Boolean,
  headImage: String,
  showName: String
})

// 图片信息相关
const loading = computed(() => {
  return props.msgInfo.loadStatus && props.msgInfo.loadStatus === 'loading'
})

const loadFail = ref(false)
const showFullImageBox = () => {
  let imageUrl = JSON.parse(props.msgInfo.content).url
  console.log(imageUrl)
}
const handleSendFail = (val) => {
  console.log(val)
}

// 文件信息相关
// const data = ref(null)
const data = computed(() => {
  if (props.msgInfo.type == 2) {
    return JSON.parse(props.msgInfo.content)
  } else {
    return {}
  }
})

const fileSize = computed(() => {
  let size = data.value ? data.value.size || '' : ''
  if (size > 1024 * 1024) {
    return Math.round(size / 1024 / 1024) + 'M'
  }
  if (size > 1024) {
    return Math.round(size / 1024) + 'KB'
  }
  return size + 'B'
})
</script>

<template>
  <div class="chat-msg-item">
    <!-- 如果是系统提示 -->
    <div class="chat-msg-tip" v-show="props.msgInfo.type == 10">
      {{ props.msgInfo.content }}
    </div>
    <!-- 如果不是系统提示 -->
    <div
      class="chat-msg-normal"
      v-show="props.msgInfo.type != 10"
      :class="{ 'chat-msg-mine': props.mine }"
    >
      <!-- 头像 -->
      <div class="head-image">
        <head-sculpture
          :url="props.headImage"
          :id="msgInfo.sendId"
        ></head-sculpture>
      </div>
      <div class="chat-msg-content">
        <!-- 昵称和时间 -->
        <div class="chat-msg-top">
          <span>{{ props.showName }}</span>
          <chat-time :time="msgInfo.sendTime"></chat-time>
        </div>
        <!-- 消息内容 -->
        <!-- <div
          class="chat-msg-bottom"
          @contextmenu.prevent="showRightMenu($event)"
        > -->
        <div class="chat-msg-bottom">
          <!-- 如果是文字 -->
          <span
            class="chat-msg-text"
            v-if="props.msgInfo.type == 0"
            v-html="transform(props.msgInfo.content)"
          ></span>
          <!-- 如果是图片 -->
          <div class="chat-msg-image" v-if="props.msgInfo.type == 1">
            <div
              element-loading-text="上传中.."
              element-loading-background="rgba(0, 0, 0, 0.4)"
              :fullscreen="false"
              v-loading="loading"
            >
              <div class="img-load-box">
                <img
                  class="send-image"
                  :src="JSON.parse(props.msgInfo.content).thumbUrl"
                  @click="showFullImageBox()"
                />
              </div>
            </div>

            <!-- 感觉是个没用的东西 -->
            <span
              title="发送失败"
              v-show="loadFail"
              @click="handleSendFail"
              class="send-fail el-icon-warning"
            ></span>
          </div>
          <!-- 如果是文件 -->
          <div class="chat-msg-file" v-if="msgInfo.type == 2">
            <div class="chat-file-box" v-loading="loading">
              <div class="chat-file-info">
                <el-link
                  class="chat-file-name"
                  :underline="true"
                  target="_blank"
                  type="primary"
                  :href="data ? data.url || '' : ''"
                >
                  {{ data ? data.name || '' : '' }}
                </el-link>
                <div class="chat-file-size">{{ fileSize }}</div>
              </div>
              <div class="chat-file-icon">
                <el-icon type="primary"><Document /></el-icon>
              </div>
            </div>
            <span
              title="发送失败"
              v-show="loadFail"
              @click="handleSendFail"
              class="send-fail el-icon-warning"
            ></span>
          </div>
        </div>
      </div>
    </div>
    <!-- 右键后弹出来的菜单，是个组件 -->
    <!-- <right-menu
      v-show="menu && rightMenu.show"
      :pos="rightMenu.pos"
      :items="menuItems"
      @close="rightMenu.show = false"
      @select="handleSelectMenu"
    ></right-menu> -->
  </div>
</template>
<style lang="scss" scoped>
.chat-msg-item {
  .chat-msg-tip {
    line-height: 50px;
  }

  .chat-msg-normal {
    position: relative;
    font-size: 0;
    margin-bottom: 10px;
    padding-left: 60px;
    min-height: 68px;

    .head-image {
      position: absolute;
      width: 40px;
      height: 40px;
      top: 0;
      left: 0;
    }

    .chat-msg-content {
      display: flex;
      flex-direction: column;

      .chat-msg-top {
        display: flex;
        flex-wrap: nowrap;
        color: #333;
        font-size: 14px;
        line-height: 20px;

        span {
          margin-right: 12px;
        }
      }

      .chat-msg-bottom {
        text-align: left;

        .chat-msg-text {
          max-width: 680px;
          position: relative;
          line-height: 22px;
          margin-top: 10px;
          padding: 10px;
          background-color: #ffffff;
          border-radius: 3px;
          color: #333;
          display: inline-block;
          font-size: 14px;
          text-align: left;

          &:after {
            content: '';
            position: absolute;
            left: -10px;
            top: 13px;
            width: 0;
            height: 0;
            border-style: solid dashed dashed;
            border-color: #ffffff transparent transparent;
            overflow: hidden;
            border-width: 10px;
          }
        }

        .chat-msg-image {
          display: flex;
          flex-wrap: nowrap;
          flex-direction: row;
          align-items: center;

          .send-image {
            min-width: 300px;
            min-height: 200px;
            max-width: 600px;
            max-height: 400px;
            border: #dddddd solid 1px;
            cursor: pointer;
          }

          .send-fail {
            color: #e60c0c;
            font-size: 30px;
            cursor: pointer;
            margin: 0 20px;
          }
        }

        .chat-msg-file {
          display: flex;
          flex-wrap: nowrap;
          flex-direction: row;
          align-items: center;
          cursor: pointer;

          .chat-file-box {
            display: flex;
            flex-wrap: nowrap;
            align-items: center;
            max-width: 100%;
            min-height: 80px;
            border: #dddddd solid 1px;
            border-radius: 3px;
            background-color: #eeeeee;
            padding: 10px 15px;

            .chat-file-info {
              flex: 1;
              height: 100%;
              text-align: left;
              font-size: 14px;

              .chat-file-name {
                font-size: 16px;
                font-weight: 600;
                margin-bottom: 15px;
                display: flex;
              }
            }

            .chat-file-icon {
              font-size: 50px;
              color: #d42e07;
            }
          }

          .send-fail {
            color: #e60c0c;
            font-size: 30px;
            cursor: pointer;
            margin: 0 20px;
          }
        }

        .chat-msg-voice {
          font-size: 14px;
          cursor: pointer;

          audio {
            height: 45px;
            padding: 5px 0;
          }
        }
      }
    }

    &.chat-msg-mine {
      text-align: right;
      padding-left: 0;
      padding-right: 60px;

      .head-image {
        left: auto;
        right: 0;
      }

      .chat-msg-content {
        .chat-msg-top {
          flex-direction: row-reverse;

          span {
            margin-left: 12px;
            margin-right: 0;
          }
        }

        .chat-msg-bottom {
          text-align: right;

          .chat-msg-text {
            margin-left: 10px;
            background-color: #5fb878;
            color: #fff;
            display: inline-block;
            vertical-align: top;
            font-size: 14px;

            &:after {
              left: auto;
              right: -10px;
              border-top-color: #5fb878;
            }
          }

          .chat-msg-image {
            flex-direction: row-reverse;
          }

          .chat-msg-file {
            flex-direction: row-reverse;
          }
        }
      }
    }
  }
}
</style>
