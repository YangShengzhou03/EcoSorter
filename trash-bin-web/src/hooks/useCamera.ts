import { useState, useRef, useCallback, useEffect } from 'react'

interface UseCameraReturn {
  videoRef: React.RefObject<HTMLVideoElement>
  canvasRef: React.RefObject<HTMLCanvasElement>
  isCameraActive: boolean
  hasCameraAccess: boolean
  startCamera: () => Promise<void>
  stopCamera: () => void
  captureFrame: () => ImageData | null
  switchCamera: () => Promise<void>
  error: string | null
}

export const useCamera = (): UseCameraReturn => {
  const videoRef = useRef<HTMLVideoElement>(null)
  const canvasRef = useRef<HTMLCanvasElement>(null)
  const [isCameraActive, setIsCameraActive] = useState(false)
  const [hasCameraAccess, setHasCameraAccess] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [stream, setStream] = useState<MediaStream | null>(null)
  const [facingMode, setFacingMode] = useState<'user' | 'environment'>('environment')

  // 检查摄像头支持
  const checkCameraSupport = useCallback(async (): Promise<boolean> => {
    try {
      if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
        throw new Error('浏览器不支持摄像头功能')
      }

      // 检查摄像头权限状态
      const permissionStatus = await navigator.permissions.query({ name: 'camera' as PermissionName })
      if (permissionStatus.state === 'denied') {
        throw new Error('摄像头权限已被拒绝，请检查浏览器设置')
      }

      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : '摄像头检查失败')
      return false
    }
  }, [])

  // 获取摄像头流
  const getCameraStream = useCallback(async (): Promise<MediaStream> => {
    try {
      const constraints: MediaStreamConstraints = {
        video: {
          facingMode: facingMode,
          width: { ideal: 1280 },
          height: { ideal: 720 },
          frameRate: { ideal: 30 }
        },
        audio: false
      }

      return await navigator.mediaDevices.getUserMedia(constraints)
    } catch (err) {
      if (err instanceof Error && err.name === 'NotAllowedError') {
        throw new Error('摄像头权限被拒绝')
      } else if (err instanceof Error && err.name === 'NotFoundError') {
        throw new Error('未找到可用的摄像头')
      } else if (err instanceof Error && err.name === 'NotReadableError') {
        throw new Error('摄像头正在被其他程序使用')
      }
      throw new Error('无法访问摄像头')
    }
  }, [facingMode])

  // 启动摄像头
  const startCamera = useCallback(async (): Promise<void> => {
    try {
      setError(null)

      // 检查支持
      const isSupported = await checkCameraSupport()
      if (!isSupported) return

      // 获取摄像头流
      const cameraStream = await getCameraStream()
      setStream(cameraStream)
      setHasCameraAccess(true)

      // 设置视频元素
      if (videoRef.current) {
        videoRef.current.srcObject = cameraStream
        
        // 等待视频加载完成
        await new Promise<void>((resolve, reject) => {
          if (!videoRef.current) {
            reject(new Error('视频元素未找到'))
            return
          }

          videoRef.current.onloadedmetadata = () => {
            videoRef.current?.play().then(resolve).catch(reject)
          }

          videoRef.current.onerror = () => {
            reject(new Error('视频播放失败'))
          }

          // 超时处理
          setTimeout(() => reject(new Error('视频加载超时')), 5000)
        })

        setIsCameraActive(true)
        setError(null)
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : '摄像头启动失败')
      setIsCameraActive(false)
      setHasCameraAccess(false)
    }
  }, [checkCameraSupport, getCameraStream])

  // 停止摄像头
  const stopCamera = useCallback((): void => {
    if (stream) {
      stream.getTracks().forEach(track => {
        track.stop()
      })
      setStream(null)
    }

    if (videoRef.current) {
      videoRef.current.srcObject = null
    }

    setIsCameraActive(false)
    setError(null)
  }, [stream])

  // 切换摄像头（前后置）
  const switchCamera = useCallback(async (): Promise<void> => {
    try {
      stopCamera()
      setFacingMode(prev => prev === 'environment' ? 'user' : 'environment')
      
      // 给摄像头一些时间切换
      await new Promise(resolve => setTimeout(resolve, 500))
      
      await startCamera()
    } catch (err) {
      setError(err instanceof Error ? err.message : '摄像头切换失败')
    }
  }, [stopCamera, startCamera])

  // 捕获当前帧
  const captureFrame = useCallback((): ImageData | null => {
    if (!videoRef.current || !canvasRef.current || !isCameraActive) {
      return null
    }

    const video = videoRef.current
    const canvas = canvasRef.current
    const context = canvas.getContext('2d')

    if (!context) {
      return null
    }

    // 设置canvas尺寸匹配视频
    canvas.width = video.videoWidth
    canvas.height = video.videoHeight

    // 绘制当前帧到canvas
    context.drawImage(video, 0, 0, canvas.width, canvas.height)

    // 获取图像数据
    return context.getImageData(0, 0, canvas.width, canvas.height)
  }, [isCameraActive])

  // 清理函数
  useEffect(() => {
    return () => {
      stopCamera()
    }
  }, [stopCamera])

  // 错误处理
  useEffect(() => {
    const handleVisibilityChange = () => {
      if (document.hidden && isCameraActive) {
        // 页面隐藏时暂停摄像头以节省资源
        stopCamera()
      } else if (!document.hidden && !isCameraActive && hasCameraAccess) {
        // 页面重新显示时恢复摄像头
        startCamera()
      }
    }

    document.addEventListener('visibilitychange', handleVisibilityChange)

    return () => {
      document.removeEventListener('visibilitychange', handleVisibilityChange)
    }
  }, [isCameraActive, hasCameraAccess, startCamera, stopCamera])

  return {
    videoRef,
    canvasRef,
    isCameraActive,
    hasCameraAccess,
    startCamera,
    stopCamera,
    captureFrame,
    switchCamera,
    error
  }
}