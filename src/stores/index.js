import { createPinia } from 'pinia'
import persist from 'pinia-plugin-persistedstate'

const pinia = createPinia()
pinia.use(persist)

export default pinia
//接收user模块的所有按需导出
export * from './modules/user'
export * from './modules/chat'
export * from './modules/friend'
export * from './modules/topic'
