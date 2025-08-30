import React from 'react'

interface LoadingScreenProps {
  message?: string
  progress?: number
  subMessage?: string
  onCancel?: () => void
}

const LoadingScreen: React.FC<LoadingScreenProps> = ({
  message = "加载中...",
  progress,
  subMessage,
  onCancel
}) => {
  return (
    <div className="loading-screen">
      <div className="loading-content">
        {/* 加载动画 */}
        <div className="loading-animation">
          <div className="ai-icon">🤖</div>
          <div className="spinner">
            <div className="spinner-inner"></div>
          </div>
        </div>

        {/* 主消息 */}
        <div className="loading-message">
          <h2>{message}</h2>
          {subMessage && (
            <p className="sub-message">{subMessage}</p>
          )}
        </div>

        {/* 进度条 */}
        {progress !== undefined && (
          <div className="progress-container">
            <div className="progress-bar">
              <div 
                className="progress-fill"
                style={{ width: `${progress}%` }}
              />
            </div>
            <div className="progress-text">
              {Math.round(progress)}%
            </div>
          </div>
        )}

        {/* 加载提示 */}
        <div className="loading-tips">
          <div className="tip-item">
            <span className="tip-icon">⚡</span>
            <span>正在加载AI模型</span>
          </div>
          <div className="tip-item">
            <span className="tip-icon">📷</span>
            <span>初始化摄像头</span>
          </div>
          <div className="tip-item">
            <span className="tip-icon">🔍</span>
            <span>准备物体识别</span>
          </div>
        </div>

        {/* 取消按钮 */}
        {onCancel && (
          <button 
            className="cancel-button"
            onClick={onCancel}
          >
            取消加载
          </button>
        )}

        {/* 版本信息 */}
        <div className="version-info">
          EcoSorter Trash Bin v1.0.0
        </div>
      </div>

      <style jsx>{`
        .loading-screen {
          position: fixed;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: linear-gradient(135deg, #1e3a8a 0%, #22c55e 100%);
          display: flex;
          align-items: center;
          justify-content: center;
          z-index: 9999;
          color: white;
          font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
        }

        .loading-content {
          text-align: center;
          max-width: 400px;
          padding: 40px;
          background: rgba(255, 255, 255, 0.1);
          backdrop-filter: blur(20px);
          border-radius: 24px;
          border: 1px solid rgba(255, 255, 255, 0.2);
          box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
        }

        .loading-animation {
          position: relative;
          margin-bottom: 32px;
        }

        .ai-icon {
          font-size: 64px;
          margin-bottom: 24px;
          animation: float 3s ease-in-out infinite;
        }

        .spinner {
          width: 60px;
          height: 60px;
          margin: 0 auto;
          position: relative;
          animation: rotate 2s linear infinite;
        }

        .spinner-inner {
          width: 100%;
          height: 100%;
          border: 4px solid rgba(255, 255, 255, 0.3);
          border-top: 4px solid white;
          border-radius: 50%;
          animation: spin 1.5s ease-in-out infinite;
        }

        .loading-message {
          margin-bottom: 32px;
        }

        .loading-message h2 {
          margin: 0 0 12px 0;
          font-size: 24px;
          font-weight: 600;
        }

        .sub-message {
          margin: 0;
          font-size: 16px;
          opacity: 0.8;
          color: rgba(255, 255, 255, 0.9);
        }

        .progress-container {
          margin-bottom: 32px;
        }

        .progress-bar {
          width: 100%;
          height: 8px;
          background: rgba(255, 255, 255, 0.3);
          border-radius: 4px;
          overflow: hidden;
          margin-bottom: 12px;
        }

        .progress-fill {
          height: 100%;
          background: white;
          border-radius: 4px;
          transition: width 0.3s ease;
          box-shadow: 0 0 20px rgba(255, 255, 255, 0.5);
        }

        .progress-text {
          font-size: 14px;
          font-weight: 600;
          opacity: 0.9;
        }

        .loading-tips {
          display: flex;
          flex-direction: column;
          gap: 12px;
          margin-bottom: 24px;
        }

        .tip-item {
          display: flex;
          align-items: center;
          gap: 12px;
          font-size: 14px;
          opacity: 0.8;
          justify-content: center;
        }

        .tip-icon {
          font-size: 16px;
        }

        .cancel-button {
          background: rgba(255, 255, 255, 0.2);
          color: white;
          border: 1px solid rgba(255, 255, 255, 0.3);
          padding: 10px 24px;
          border-radius: 8px;
          font-size: 14px;
          cursor: pointer;
          transition: all 0.2s ease;
        }

        .cancel-button:hover {
          background: rgba(255, 255, 255, 0.3);
        }

        .version-info {
          margin-top: 24px;
          font-size: 12px;
          opacity: 0.6;
        }

        /* 动画定义 */
        @keyframes float {
          0%, 100% { transform: translateY(0px); }
          50% { transform: translateY(-10px); }
        }

        @keyframes rotate {
          0% { transform: rotate(0deg); }
          100% { transform: rotate(360deg); }
        }

        @keyframes spin {
          0% { transform: rotate(0deg); }
          100% { transform: rotate(360deg); }
        }

        /* 响应式设计 */
        @media (max-width: 768px) {
          .loading-content {
            margin: 20px;
            padding: 32px 24px;
            max-width: none;
          }

          .ai-icon {
            font-size: 48px;
          }

          .spinner {
            width: 50px;
            height: 50px;
          }

          .loading-message h2 {
            font-size: 20px;
          }

          .sub-message {
            font-size: 14px;
          }

          .loading-tips {
            gap: 8px;
          }

          .tip-item {
            font-size: 13px;
          }
        }

        /* 横屏模式优化 */
        @media (orientation: landscape) and (max-height: 500px) {
          .loading-content {
            padding: 24px;
            max-width: 90%;
          }

          .loading-animation {
            margin-bottom: 20px;
          }

          .ai-icon {
            font-size: 40px;
            margin-bottom: 16px;
          }

          .spinner {
            width: 40px;
            height: 40px;
          }

          .loading-message {
            margin-bottom: 20px;
          }

          .loading-message h2 {
            font-size: 18px;
            margin-bottom: 8px;
          }

          .progress-container {
            margin-bottom: 20px;
          }

          .loading-tips {
            flex-direction: row;
            flex-wrap: wrap;
            justify-content: center;
            gap: 16px;
            margin-bottom: 20px;
          }

          .tip-item {
            flex-direction: column;
            gap: 4px;
            text-align: center;
          }

          .version-info {
            margin-top: 16px;
          }
        }

        /* 高对比度模式 */
        @media (prefers-contrast: high) {
          .loading-content {
            border: 2px solid white;
            background: rgba(0, 0, 0, 0.9);
          }

          .progress-bar {
            border: 1px solid white;
          }

          .cancel-button {
            border: 2px solid white;
          }
        }

        /* 减少动画偏好 */
        @media (prefers-reduced-motion: reduce) {
          .ai-icon,
          .spinner,
          .spinner-inner {
            animation: none;
          }

          .progress-fill {
            transition: none;
          }
        }

        /* 暗色模式优化 */
        @media (prefers-color-scheme: dark) {
          .loading-screen {
            background: linear-gradient(135deg, #111827 0%, #166534 100%);
          }

          .loading-content {
            background: rgba(31, 41, 55, 0.95);
            border-color: rgba(255, 255, 255, 0.1);
          }

          .progress-bar {
            background: rgba(255, 255, 255, 0.2);
          }

          .cancel-button {
            background: rgba(255, 255, 255, 0.1);
            border-color: rgba(255, 255, 255, 0.2);
          }

          .cancel-button:hover {
            background: rgba(255, 255, 255, 0.15);
          }
        }
      `}</style>
    </div>
  )
}

export default LoadingScreen