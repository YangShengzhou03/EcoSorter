import React, { useState, useEffect, useRef } from 'react'
import { useCamera } from './hooks/useCamera'
import { useObjectDetection } from './hooks/useObjectDetection'
import { useAuth } from './hooks/useAuth'
import CameraView from './components/CameraView'
import DetectionOverlay from './components/DetectionOverlay'
import UserPanel from './components/UserPanel'
import LoadingScreen from './components/LoadingScreen'
import ErrorBoundary from './components/ErrorBoundary'
import './App.css'

// 垃圾类型定义
type TrashType = 'recyclable' | 'hazardous' | 'household' | 'other'

// 检测结果接口
interface DetectionResult {
  class: string
  score: number
  bbox: [number, number, number, number] // [x, y, width, height]
  trashType?: TrashType
}

// 用户信息接口
interface UserInfo {
  id: string
  name: string
  points: number
  avatar?: string
}

function App() {
  const [detections, setDetections] = useState<DetectionResult[]>([])
  const [currentUser, setCurrentUser] = useState<UserInfo | null>(null)
  const [isProcessing, setIsProcessing] = useState(false)
  const [lastPointsEarned, setLastPointsEarned] = useState(0)
  const [error, setError] = useState<string>('')

  // 使用自定义hooks
  const {
    videoRef,
    canvasRef,
    isCameraActive,
    startCamera,
    stopCamera,
    captureFrame,
    hasCameraAccess
  } = useCamera()

  const {
    isModelLoaded,
    detectObjects,
    loading: modelLoading,
    error: modelError
  } = useObjectDetection()

  const { 
    isAuthenticated, 
    userInfo, 
    login, 
    logout, 
    updatePoints 
  } = useAuth()

  // 初始化摄像头和模型
  useEffect(() => {
    const initialize = async () => {
      try {
        await startCamera()
        setError('')
      } catch (err) {
        setError(err instanceof Error ? err.message : '摄像头初始化失败')
      }
    }

    initialize()

    return () => {
      stopCamera()
    }
  }, [])

  // 更新用户信息
  useEffect(() => {
    if (userInfo) {
      setCurrentUser(userInfo)
    }
  }, [userInfo])

  // 处理模型错误
  useEffect(() => {
    if (modelError) {
      setError(modelError)
    }
  }, [modelError])

  // 定期执行物体检测
  useEffect(() => {
    if (!isCameraActive || !isModelLoaded || isProcessing) return

    const detectionInterval = setInterval(async () => {
      try {
        setIsProcessing(true)
        const frame = captureFrame()
        if (!frame) return

        const results = await detectObjects(frame)
        const trashDetections = results.map(result => ({
          ...result,
          trashType: classifyTrash(result.class)
        }))

        setDetections(trashDetections)

        // 如果检测到垃圾且用户已登录，自动添加积分
        if (trashDetections.length > 0 && currentUser) {
          const pointsEarned = calculatePoints(trashDetections)
          if (pointsEarned > 0) {
            await updatePoints(pointsEarned)
            setLastPointsEarned(pointsEarned)
            
            // 显示积分获得提示（2秒后消失）
            setTimeout(() => setLastPointsEarned(0), 2000)
          }
        }

      } catch (err) {
        console.error('检测错误:', err)
      } finally {
        setIsProcessing(false)
      }
    }, 2000) // 每2秒检测一次

    return () => clearInterval(detectionInterval)
  }, [isCameraActive, isModelLoaded, isProcessing, currentUser])

  // 垃圾分类逻辑
  const classifyTrash = (className: string): TrashType => {
    const lowerClass = className.toLowerCase()
    
    // 可回收垃圾
    if (['bottle', 'can', 'paper', 'cardboard', 'plastic'].some(word => 
      lowerClass.includes(word))) {
      return 'recyclable'
    }
    
    // 有害垃圾
    if (['battery', 'chemical', 'medicine', 'electronic'].some(word => 
      lowerClass.includes(word))) {
      return 'hazardous'
    }
    
    // 厨余垃圾
    if (['food', 'fruit', 'vegetable', 'organic'].some(word => 
      lowerClass.includes(word))) {
      return 'household'
    }
    
    return 'other'
  }

  // 积分计算逻辑
  const calculatePoints = (detections: DetectionResult[]): number => {
    return detections.reduce((total, detection) => {
      switch (detection.trashType) {
        case 'recyclable':
          return total + 5
        case 'hazardous':
          return total + 10
        case 'household':
          return total + 3
        default:
          return total + 1
      }
    }, 0)
  }

  // 处理用户登录
  const handleLogin = async (userId: string) => {
    try {
      await login(userId)
      setError('')
    } catch (err) {
      setError(err instanceof Error ? err.message : '登录失败')
    }
  }

  // 处理用户登出
  const handleLogout = () => {
    logout()
    setCurrentUser(null)
  }

  if (modelLoading) {
    return <LoadingScreen message="AI模型加载中..." />
  }

  if (error) {
    return (
      <div className="error-container">
        <div className="error-content">
          <h2>😕 出错了</h2>
          <p>{error}</p>
          <button 
            onClick={() => window.location.reload()} 
            className="retry-button"
          >
            重新加载
          </button>
        </div>
      </div>
    )
  }

  return (
    <ErrorBoundary>
      <div className="app">
        {/* 摄像头视图 */}
        <div className="camera-container">
          <CameraView 
            videoRef={videoRef}
            canvasRef={canvasRef}
            isActive={isCameraActive}
          />
          
          {/* 检测结果叠加层 */}
          <DetectionOverlay 
            detections={detections}
            videoWidth={videoRef.current?.videoWidth || 640}
            videoHeight={videoRef.current?.videoHeight || 480}
          />

          {/* 积分获得提示 */}
          {lastPointsEarned > 0 && (
            <div className="points-notification">
              +{lastPointsEarned} 积分！
            </div>
          )}
        </div>

        {/* 用户面板 */}
        <UserPanel
          user={currentUser}
          isAuthenticated={isAuthenticated}
          onLogin={handleLogin}
          onLogout={handleLogout}
          detections={detections}
          hasCameraAccess={hasCameraAccess}
        />

        {/* 处理状态指示器 */}
        {isProcessing && (
          <div className="processing-indicator">
            <div className="spinner"></div>
            <span>识别中...</span>
          </div>
        )}
      </div>
    </ErrorBoundary>
  )
}

export default App