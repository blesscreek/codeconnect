import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import pinia from '@/stores/index'
// Element相关
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import '@/assets/main.scss'

const app = createApp(App)
app.use(ElementPlus)
app.use(pinia)
app.use(router)

app.mount('#app')
