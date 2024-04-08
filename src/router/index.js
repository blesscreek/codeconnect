// import { useUserStore } from '@/stores'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/login', component: () => import('@/views/login/LoginPage.vue') },
    {
      path: '/',
      component: () => import('@/views/layout/LayoutPage.vue'),
      redirect: '/topic',
      children: [
        {
          path: '/topic',
          component: () => import('@/views/topic/TopicPage.vue')
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
  ]
})
// 路由前置守卫
// router.beforeEach((to) => {
//   // 访问管理中心，个人中心，答题页面时拦截
//   // console.log(to.matched[0].path)
//   const userStore = useUserStore()
//   if (!userStore.token && to.matched[0].path === '/manage') return '/login'
// })

export default router
