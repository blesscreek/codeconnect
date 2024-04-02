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
        }
      ]
    },
    {
      path: '/manage',
      component: () => import('@/views/manage/ManagementCenter.vue'),
      redirect: '/manage/changetopic',
      children: [
        {
          path: '/manage/addtopic',
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
      path: '/manage/back',
      component: () => import('@/views/layout/LayoutPage.vue'),
      redirect: '/topic'
    }
  ]
})

export default router
