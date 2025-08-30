import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import './index.css'

// 全局错误处理
const handleGlobalError = (event: ErrorEvent) => {
  console.error('Global error caught:', event.error)
  // 可以发送错误日志到服务器
}

const handleRejection = (event: PromiseRejectionEvent) => {
  console.error('Unhandled rejection:', event.reason)
  event.preventDefault()
}

// 注册全局错误处理器
window.addEventListener('error', handleGlobalError)
window.addEventListener('unhandledrejection', handleRejection)

// 初始化应用
async function initializeApp() {
  try {
    // 检查设备兼容性
    const deviceInfo = JSON.parse(localStorage.getItem('deviceInfo') || '{}')
    
    if (!deviceInfo.supportsCamera) {
      throw new Error('设备不支持摄像头功能')
    }

    // 预加载TensorFlow.js（如果CDN加载失败）
    if (typeof window.tf === 'undefined') {
      console.warn('TensorFlow.js CDN加载失败，尝试动态加载...')
      await loadTensorFlowFallback()
    }

    ReactDOM.createRoot(document.getElementById('root')!).render(
      <React.StrictMode>
        <App />
      </React.StrictMode>,
    )
  } catch (error) {
    console.error('应用初始化失败:', error)
    // 显示错误页面
    renderErrorPage(error)
  }
}

// TensorFlow.js 备用加载方案
async function loadTensorFlowFallback(): Promise<void> {
  return new Promise((resolve, reject) => {
    const script = document.createElement('script')
    script.src = 'https://cdn.jsdelivr.net/npm/@tensorflow/tfjs@4.10.0/dist/tf.min.js'
    script.onload = () => {
      if (typeof window.tf !== 'undefined') {
        resolve()
      } else {
        reject(new Error('TensorFlow.js加载失败'))
      }
    }
    script.onerror = () => reject(new Error('TensorFlow.js脚本加载错误'))
    document.head.appendChild(script)
  })
}

// 错误页面渲染
function renderErrorPage(error: unknown): void {
  const root = document.getElementById('root')
  if (!root) return

  const errorMessage = error instanceof Error ? error.message : '未知错误'
  
  root.innerHTML = `
    <div style="
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: 100vh;
      background: #000;
      color: #fff;
      text-align: center;
      padding: 20px;
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
    ">
      <div style="font-size: 48px; margin-bottom: 20px;">⚠️</div>
      <h1 style="font-size: 24px; margin-bottom: 16px; color: #ef4444;">
        初始化失败
      </h1>
      <p style="font-size: 16px; margin-bottom: 24px; color: #94a3b8;">
        ${errorMessage}
      </p>
      <div style="font-size: 14px; color: #64748b; margin-top: 32px;">
        <p>请检查：</p>
        <ul style="text-align: left; margin: 16px 0; padding-left: 24px;">
          <li>摄像头权限是否开启</li>
          <li>网络连接是否正常</li>
          <li>浏览器是否支持WebRTC</li>
        </ul>
      </div>
      <button 
        onclick="window.location.reload()" 
        style="
          background: #22c55e;
          color: white;
          border: none;
          padding: 12px 24px;
          border-radius: 8px;
          font-size: 16px;
          cursor: pointer;
          margin-top: 20px;
        "
      >
        重新加载
      </button>
    </div>
  `
}

// 启动应用
initializeApp().catch(console.error)