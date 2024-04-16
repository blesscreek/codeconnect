<script setup>
import MembersList from './MembersList.vue'
import FriendsList from './FriendsList.vue'
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
const data = ref([1, 2, 3, 4])
const route = useRoute()
const listVal = ref(false)
// 好友还是用户列表
const listChange = () => {
  let path = route.path.split('/')[2]
  if (path == 'chatroom') {
    listVal.value = true
  } else {
    listVal.value = false
  }
}
listChange()
watch(route, () => {
  listChange()
})
</script>
<template>
  <div class="box">
    <div class="head">
      <span>{{ listVal ? '全部人员(o゜▽゜)o☆' : '最近消息(o゜▽゜)o☆' }}</span>
    </div>
    <div v-if="listVal">
      <div class="li" style="height: 60px" v-for="(x, i) in data" :key="i">
        <members-list></members-list>
      </div>
    </div>
    <div v-else>
      <div class="li" style="height: 80px" v-for="(x, i) in data" :key="i">
        <friends-list></friends-list>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.box {
  width: 100%;
  height: 100%;
  .head {
    width: 100%;
    height: 65px;
    display: flex;
    align-items: center;
    color: #004ed5;
    font-size: 18px;
    span {
      display: block;
      margin-left: 10px;
    }
  }
  div .li:first-child {
    background-color: #eee;
  }
}
.li {
  width: 100%;
}
.li:hover {
  cursor: pointer;
  background-color: #eee;
}
</style>
