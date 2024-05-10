// 用户登录后链接

// WebSocket对象
let websocket = null

// 延迟请求代码 的存储变量
let rec

// 链接状态标识，避免重复链接
let isConnect = false

// WebSocket链接地址
let wsurl = ''

// 用户ID
let userId = null

// 消息回调函数
let messageCallBack = null

// 链接打开回调函数
let openCallBack = null

// 登录状态标识
let hasLogin = false

// 心跳设置对象
const heartCheck = {
  timeout: 5000, // 心跳超时时间，单位为毫秒，这里设置为5秒
  timeoutObj: null, // 延时发送消息对象的引用，用于启动心跳后重置对象
  start: function () {
    if (isConnect) {
      console.log('发送WebSocket心跳') // 发送WebSocket心跳的日志输出
      let heartBeat = {
        cmd: 1, // 心跳命令代码
        data: {
          userId: userId // 用户ID
        }
      }
      websocket.send(JSON.stringify(heartBeat)) // 发送心跳包
    }
  },

  reset: function () {
    clearTimeout(this.timeoutObj) // 清除上次的延时发送对象
    this.timeoutObj = setTimeout(function () {
      heartCheck.start() // 重新启动心跳
    }, this.timeout)
  }
}

// 定义重连函数
let reConnect = () => {
  console.log('尝试重新连接')
  if (isConnect) return //如果已经连上就不在重连了
  rec && clearTimeout(rec)
  rec = setTimeout(function () {
    // 延迟5秒重连  避免过多次过频繁请求重连
    initWebSocket(wsurl)
  }, 5000)
}

// 初始化WebSocket链接函数
let initWebSocket = () => {
  try {
    console.log('初始化WebSocket')
    hasLogin = false
    websocket = new WebSocket(wsurl)
    websocket.onmessage = function (e) {
      // console.log(hasLogin)
      let sendInfo = JSON.parse(e.data)
      console.log(sendInfo)
      if (sendInfo.cmd == 0) {
        hasLogin = true
        heartCheck.start()
        console.log('WebSocket登录成功')
        openCallBack && openCallBack()
      } else if (sendInfo.cmd == 1) {
        // 重新开启心跳定时
        heartCheck.reset()
      } else {
        // 其他消息转发出去
        messageCallBack && messageCallBack(sendInfo.cmd, sendInfo.data)
      }
    }

    websocket.onclose = function () {
      console.log('WebSocket连接关闭')
      isConnect = false //表示链接断开
    }

    websocket.onopen = function () {
      console.log('WebSocket连接成功')
      isConnect = true
      // 发送登录命令
      const loginInfo = {
        cmd: 0,
        data: { userId: userId }
      }
      websocket.send(JSON.stringify(loginInfo))
    }
  } catch (e) {
    console.log('尝试创建连接失败')
    reConnect() //如果无法连接上webSocket 那么重新连接！可能会因为服务器重新部署，或者短暂断网等导致无法创建连接
  }
}

// 创建WebSocket
let createWebSocket = (url, id) => {
  if (websocket) closeWebSocket()
  wsurl = url
  userId = id
  initWebSocket()
}

// 发送消息函数 //没用上
function sendMessage(agentData) {
  if (websocket.readyState === websocket.OPEN) {
    // ws开启
    websocket.send(JSON.stringify(agentData))
  } else if (websocket.readyState === websocket.CONNECTING) {
    // 如果是正在开启状态，则等1s后重新调用
    setTimeout(function () {
      sendMessage(agentData)
    }, 1000)
  }
}
// 消息接收回调函数
function onmessage(callback) {
  messageCallBack = callback
}

// 连接打开回调函数-----这是干什么的
function onopen(callback) {
  openCallBack = callback
  if (hasLogin) {
    openCallBack()
  }
}

// 关闭连接函数
let closeWebSocket = () => {
  websocket.close()
}

// 导出方法
export { createWebSocket, closeWebSocket, sendMessage, onmessage, onopen }
