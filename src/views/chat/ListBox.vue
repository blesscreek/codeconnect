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
<style scoped>
.box {
  width: 100%;
  height: 100%;
}
.li {
  width: 100%;
}
.li:hover {
  cursor: pointer;
  background-color: #e9e9e9;
}
</style>
