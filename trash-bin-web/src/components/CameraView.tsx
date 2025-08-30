import React, { useEffect } from 'react'

interface CameraViewProps {
  videoRef: React.RefObject<HTMLVideoElement>
  canvasRef: React.RefObject<HTMLCanvasElement>
  isActive: boolean
  className?: string
}

const CameraView: React.FC<CameraViewProps> = ({
  videoRef,
  canvasRef,
  isActive,
  className = ''
}) => {
  // 处理视频元素尺寸变化
  useEffect(() => {
    const video = videoRef.current
    if (!video) return

    const handleResize = () => {
      // 可以在这里处理视频尺寸变化逻辑
      console.log('Video dimensions:', video.videoWidth, video.videoHeight)
    }

    video.addEventListener('resize', handleResize)
    
    return () => {
      video.removeEventListener('resize', handleResize)
    }
  }, [videoRef])

  // 处理视频加载状态
  useEffect(() => {
    const video = videoRef.current
    if (!video) return

    const handleLoadStart = () => {
      console.log('视频开始加载')
    }

    const handleLoadedData = () => {
      console.log('视频数据加载完成')
    }

    const handleCanPlay = () => {
      console.log('视频可以播放')
    }

    video.addEventListener('loadstart', handleLoadStart)
    video.addEventListener('loadeddata', handleLoadedData)
    video.addEventListener('canplay', handleCanPlay)

    return () => {
      video.removeEventListener('loadstart', handleLoadStart)
      video.removeEventListener('loadeddata', handleLoadedData)
      video.removeEventListener('canplay', handleCanPlay)
    }
  }, [videoRef])

  return (
    <div className={`camera-view ${className}`}>
      {/* 视频元素 - 显示摄像头流 */}
      <video
        ref={videoRef}
        className="camera-video"
        autoPlay
        playsInline
        muted
        style={{
          width: '100%',
          height: '100%',
          objectFit: 'cover',
          transform: 'scaleX(-1)', // 水平翻转镜像
          borderRadius: '12px',
          backgroundColor: '#000',
          display: isActive ? 'block' : 'none'
        }}
      />

      {/* 画布元素 - 用于图像处理和检测 */}
      <canvas
        ref={canvasRef}
        className="camera-canvas"
        style={{
          position: 'absolute',
          top: 0,
          left: 0,
          width: '100%',
          height: '100%',
          pointerEvents: 'none',
          display: 'none' // 隐藏canvas，仅用于处理
        }}
      />

      {/* 摄像头未激活状态 */}
      {!isActive && (
        <div className="camera-placeholder">
          <div className="placeholder-content">
            <div className="camera-icon">📷</div>
            <p className="placeholder-text">
              摄像头未启动
            </p>
            <p className="placeholder-subtext">
              请确保已授予摄像头权限
            </p>
          </div>
        </div>
      )}

      {/* 摄像头控制覆盖层 */}
      <div className="camera-overlay">
        {/* 可以在这里添加控制按钮 */}
      </div>

      <style jsx>{`
        .camera-view {
          position: relative;
          width: 100%;
          height: 100%;
          overflow: hidden;
          border-radius: 16px;
          background: linear-gradient(135deg, #1e3a8a 0%, #22c55e 100%);
          box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
        }

        .camera-placeholder {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 100%;
          height: 100%;
          background: rgba(0, 0, 0, 0.8);
          color: white;
        }

        .placeholder-content {
          text-align: center;
          padding: 20px;
        }

        .camera-icon {
          font-size: 48px;
          margin-bottom: 16px;
          opacity: 0.7;
        }

        .placeholder-text {
          font-size: 18px;
          font-weight: 600;
          margin-bottom: 8px;
          color: #f8fafc;
        }

        .placeholder-subtext {
          font-size: 14px;
          color: #94a3b8;
          opacity: 0.8;
        }

        .camera-overlay {
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          pointer-events: none;
        }

        /* 响应式设计 */
        @media (max-width: 768px) {
          .camera-view {
            border-radius: 12px;
          }
          
          .placeholder-text {
            font-size: 16px;
          }
          
          .placeholder-subtext {
            font-size: 12px;
          }
        }

        /* 横屏模式优化 */
        @media (orientation: landscape) {
          .camera-view {
            max-height: 80vh;
          }
        }

        /* 暗色模式支持 */
        @media (prefers-color-scheme: dark) {
          .camera-placeholder {
            background: rgba(0, 0, 0, 0.9);
          }
        }

        /* 高对比度模式 */
        @media (prefers-contrast: high) {
          .placeholder-text {
            color: #ffffff;
          }
          
          .placeholder-subtext {
            color: #cccccc;
          }
        }

        /* 减少动画偏好 */
        @media (prefers-reduced-motion: reduce) {
          .camera-view {
            transition: none;
          }
        }
      `}</style>
    </div>
  )
}

export default CameraView