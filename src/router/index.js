// import { useUserStore } from '@/stores'
import { createRouter, createWebHistory } from 'vue-router'
console.log(import.meta.env.BASE_URL)
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 登录注册
    { path: '/login', component: () => import('@/views/login/LoginPage.vue') },
    // 主框架，有首页、题目列表、个人中心
    {
      path: '/',
      component: () => import('@/views/layout/LayoutPage.vue'),
      redirect: '/home',
      children: [
        { path: '/home', component: () => import('@/views/home/HomePage.vue') },
        {
          path: '/topic',
          component: () => import('@/views/topic/TopicPage.vue')
        },
        {
          path: '/user',
          component: () => import('@/views/user/PersonalCenter.vue'),
          redirect: '/user/setting',
          children: [
            {
              path: '/user/setting',
              component: () => import('@/views/user/UserSetting.vue')
            },
            {
              path: '/user/follow',
              component: () => import('@/views/user/FollowList.vue')
            }
          ]
        }
      ]
    },
    {
      path: '/chat',
      component: () => import('@/views/chat/ChatPage.vue'),
      redirect: '/chat/chatroom',
      children: [
        {
          path: '/chat/chatroom',
          component: () => import('@/views/chat/ListBox.vue')
        },
        {
          path: '/chat/friend',
          component: () => import('@/views/chat/ListBox.vue')
        }
      ]
    },
    {
      path: '/manage',
      component: () => import('@/views/manage/ManagementCenter.vue'),
      redirect: '/manage/changetopic',
      children: [
        {
          path: '/manage/changetopic/add',
          component: () => import('@/views/manage/AddTopic.vue')
        },
        {
          path: '/manage/changetopic',
          component: () => import('@/views/manage/ChangeTopic.vue')
        },
        {
          path: '/manage/changelabel',
          component: () => import('@/views/manage/ChangeLabel.vue')
        }
      ]
    },
    {
      path: '/topic/detail',
      component: () => import('@/views/topic/TopicDetail.vue')
    }
  ],
  base: '/'
})
// 路由前置守卫
// router.beforeEach((to) => {
//   // 访问管理中心，个人中心，答题页面时拦截
//   // console.log(to.matched[0].path)
//   const userStore = useUserStore()
//   if (!userStore.token && to.matched[0].path === '/manage') return '/login'
// })

export default router
