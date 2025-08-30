import React, { useState } from 'react'

interface UserInfo {
  id: string
  name: string
  points: number
  avatar?: string
  level?: number
}

interface DetectionResult {
  class: string
  score: number
  bbox: [number, number, number, number]
  trashType?: 'recyclable' | 'hazardous' | 'household' | 'other'
}

interface UserPanelProps {
  user: UserInfo | null
  isAuthenticated: boolean
  onLogin: (userId: string) => Promise<void>
  onLogout: () => void
  detections: DetectionResult[]
  hasCameraAccess: boolean
}

const UserPanel: React.FC<UserPanelProps> = ({
  user,
  isAuthenticated,
  onLogin,
  onLogout,
  detections,
  hasCameraAccess
}) => {
  const [isLoginOpen, setIsLoginOpen] = useState(false)
  const [userIdInput, setUserIdInput] = useState('')
  const [isLoggingIn, setIsLoggingIn] = useState(false)
  const [loginError, setLoginError] = useState('')

  // 处理用户登录
  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!userIdInput.trim()) return

    setIsLoggingIn(true)
    setLoginError('')

    try {
      await onLogin(userIdInput.trim())
      setUserIdInput('')
      setIsLoginOpen(false)
    } catch (error) {
      setLoginError(error instanceof Error ? error.message : '登录失败')
    } finally {
      setIsLoggingIn(false)
    }
  }

  // 计算当前积分收益
  const calculateCurrentPoints = () => {
    return detections.reduce((total, detection) => {
      switch (detection.trashType) {
        case 'recyclable': return total + 5
        case 'hazardous': return total + 10
        case 'household': return total + 3
        default: return total + 1
      }
    }, 0)
  }

  // 获取用户等级信息
  const getUserLevelInfo = (points: number) => {
    if (points >= 500) return { level: 5, next: 0, progress: 100 }
    if (points >= 300) return { level: 4, next: 500, progress: (points - 300) / 200 * 100 }
    if (points >= 150) return { level: 3, next: 300, progress: (points - 150) / 150 * 100 }
    if (points >= 50) return { level: 2, next: 150, progress: (points - 50) / 100 * 100 }
    return { level: 1, next: 50, progress: points / 50 * 100 }
  }

  const currentPoints = calculateCurrentPoints()
  const levelInfo = user ? getUserLevelInfo(user.points) : null

  return (
    <div className="user-panel">
      {/* 用户信息卡片 */}
      <div className="user-card">
        {isAuthenticated && user ? (
          <div className="user-info">
            {/* 用户头像和基本信息 */}
            <div className="user-header">
              <div className="avatar">
                {user.avatar ? (
                  <img src={user.avatar} alt={user.name} />
                ) : (
                  <span className="avatar-placeholder">
                    {user.name.charAt(0).toUpperCase()}
                  </span>
                )}
              </div>
              
              <div className="user-details">
                <h3 className="user-name">{user.name}</h3>
                <div className="user-id">ID: {user.id}</div>
              </div>
            </div>

            {/* 积分信息 */}
            <div className="points-section">
              <div className="total-points">
                <span className="points-label">总积分</span>
                <span className="points-value">{user.points}</span>
              </div>
              
              {currentPoints > 0 && (
                <div className="current-points">
                  <span className="earning-label">本次可获得</span>
                  <span className="earning-value">+{currentPoints}</span>
                </div>
              )}
            </div>

            {/* 等级进度 */}
            {levelInfo && (
              <div className="level-section">
                <div className="level-info">
                  <span className="level-label">等级 {levelInfo.level}</span>
                  {levelInfo.next > 0 && (
                    <span className="next-level">
                      下一级: {levelInfo.next}
                    </span>
                  )}
                </div>
                
                {levelInfo.next > 0 && (
                  <div className="progress-bar">
                    <div 
                      className="progress-fill"
                      style={{ width: `${levelInfo.progress}%` }}
                    />
                  </div>
                )}
              </div>
            )}

            {/* 操作按钮 */}
            <div className="action-buttons">
              <button 
                className="btn btn-secondary"
                onClick={onLogout}
                disabled={isLoggingIn}
              >
                退出登录
              </button>
              
              <button 
                className="btn btn-primary"
                onClick={() => window.location.reload()}
              >
                刷新页面
              </button>
            </div>
          </div>
        ) : (
          <div className="login-prompt">
            <div className="prompt-header">
              <div className="prompt-icon">👤</div>
              <h3>欢迎使用智能垃圾桶</h3>
            </div>
            
            <p className="prompt-text">
              请登录以开始垃圾分类并获得积分奖励
            </p>

            {!hasCameraAccess && (
              <div className="camera-warning">
                <span className="warning-icon">⚠️</span>
                <span>需要摄像头权限才能使用识别功能</span>
              </div>
            )}

            <button 
              className="btn btn-primary login-btn"
              onClick={() => setIsLoginOpen(true)}
              disabled={isLoggingIn}
            >
              {isLoggingIn ? '登录中...' : '立即登录'}
            </button>
          </div>
        )}
      </div>

      {/* 登录模态框 */}
      {isLoginOpen && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">
              <h3>用户登录</h3>
              <button 
                className="close-btn"
                onClick={() => {
                  setIsLoginOpen(false)
                  setLoginError('')
                }}
              >
                ×
              </button>
            </div>

            <form onSubmit={handleLogin} className="login-form">
              <div className="form-group">
                <label htmlFor="userId">用户ID</label>
                <input
                  id="userId"
                  type="text"
                  value={userIdInput}
                  onChange={(e) => setUserIdInput(e.target.value)}
                  placeholder="请输入用户ID"
                  disabled={isLoggingIn}
                  autoFocus
                />
              </div>

              {loginError && (
                <div className="error-message">
                  {loginError}
                </div>
              )}

              <div className="form-actions">
                <button 
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => {
                    setIsLoginOpen(false)
                    setLoginError('')
                  }}
                  disabled={isLoggingIn}
                >
                  取消
                </button>
                
                <button 
                  type="submit"
                  className="btn btn-primary"
                  disabled={!userIdInput.trim() || isLoggingIn}
                >
                  {isLoggingIn ? '登录中...' : '登录'}
                </button>
              </div>
            </form>

            {/* 测试用户提示 */}
            <div className="test-users">
              <p>测试用户ID: user001, user002, user003</p>
            </div>
          </div>
        </div>
      )}

      {/* 检测统计信息 */}
      {detections.length > 0 && (
        <div className="detection-stats">
          <h4>📊 实时检测</h4>
          <div className="stats-grid">
            <div className="stat-item">
              <span className="stat-label">识别数量</span>
              <span className="stat-value">{detections.length}</span>
            </div>
            <div className="stat-item">
              <span className="stat-label">预计积分</span>
              <span className="stat-value">+{currentPoints}</span>
            </div>
          </div>
        </div>
      )}

      <style jsx>{`
        .user-panel {
          position: absolute;
          top: 20px;
          right: 20px;
          z-index: 20;
          max-width: 320px;
        }

        .user-card {
          background: rgba(255, 255, 255, 0.95);
          backdrop-filter: blur(20px);
          border-radius: 16px;
          padding: 20px;
          box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
          border: 1px solid rgba(255, 255, 255, 0.2);
        }

        .user-header {
          display: flex;
          align-items: center;
          gap: 12px;
          margin-bottom: 16px;
        }

        .avatar {
          width: 48px;
          height: 48px;
          border-radius: 50%;
          background: linear-gradient(135deg, #22c55e, #3b82f6);
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
          font-weight: bold;
          font-size: 18px;
        }

        .avatar img {
          width: 100%;
          height: 100%;
          border-radius: 50%;
          object-fit: cover;
        }

        .user-details {
          flex: 1;
        }

        .user-name {
          margin: 0;
          font-size: 16px;
          font-weight: 600;
          color: #1f2937;
        }

        .user-id {
          font-size: 12px;
          color: #6b7280;
          margin-top: 2px;
        }

        .points-section {
          margin-bottom: 16px;
          padding: 12px;
          background: rgba(34, 197, 94, 0.1);
          border-radius: 12px;
        }

        .total-points {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 8px;
        }

        .points-label {
          font-size: 14px;
          color: #374151;
        }

        .points-value {
          font-size: 24px;
          font-weight: bold;
          color: #22c55e;
        }

        .current-points {
          display: flex;
          justify-content: space-between;
          align-items: center;
          font-size: 13px;
        }

        .earning-value {
          color: #22c55e;
          font-weight: 600;
        }

        .level-section {
          margin-bottom: 20px;
        }

        .level-info {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 8px;
          font-size: 13px;
        }

        .level-label {
          color: #374151;
          font-weight: 600;
        }

        .next-level {
          color: #6b7280;
        }

        .progress-bar {
          width: 100%;
          height: 6px;
          background: #e5e7eb;
          border-radius: 3px;
          overflow: hidden;
        }

        .progress-fill {
          height: 100%;
          background: linear-gradient(90deg, #22c55e, #3b82f6);
          border-radius: 3px;
          transition: width 0.3s ease;
        }

        .action-buttons {
          display: flex;
          gap: 8px;
        }

        .btn {
          flex: 1;
          padding: 10px 16px;
          border: none;
          border-radius: 8px;
          font-size: 14px;
          font-weight: 500;
          cursor: pointer;
          transition: all 0.2s ease;
        }

        .btn:disabled {
          opacity: 0.6;
          cursor: not-allowed;
        }

        .btn-primary {
          background: #22c55e;
          color: white;
        }

        .btn-primary:hover:not(:disabled) {
          background: #16a34a;
        }

        .btn-secondary {
          background: #f3f4f6;
          color: #374151;
        }

        .btn-secondary:hover:not(:disabled) {
          background: #e5e7eb;
        }

        .login-prompt {
          text-align: center;
        }

        .prompt-header {
          margin-bottom: 16px;
        }

        .prompt-icon {
          font-size: 40px;
          margin-bottom: 8px;
        }

        .prompt-header h3 {
          margin: 0;
          font-size: 18px;
          color: #1f2937;
        }

        .prompt-text {
          color: #6b7280;
          margin-bottom: 16px;
          line-height: 1.5;
        }

        .camera-warning {
          display: flex;
          align-items: center;
          gap: 6px;
          background: rgba(239, 68, 68, 0.1);
          color: #dc2626;
          padding: 8px 12px;
          border-radius: 6px;
          font-size: 12px;
          margin-bottom: 16px;
        }

        .warning-icon {
          font-size: 14px;
        }

        .login-btn {
          width: 100%;
        }

        .modal-overlay {
          position: fixed;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: rgba(0, 0, 0, 0.5);
          display: flex;
          align-items: center;
          justify-content: center;
          z-index: 1000;
        }

        .modal-content {
          background: white;
          border-radius: 16px;
          padding: 24px;
          max-width: 400px;
          width: 90%;
          max-height: 90vh;
          overflow-y: auto;
        }

        .modal-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;
        }

        .modal-header h3 {
          margin: 0;
          color: #1f2937;
        }

        .close-btn {
          background: none;
          border: none;
          font-size: 24px;
          cursor: pointer;
          color: #6b7280;
          padding: 0;
          width: 30px;
          height: 30px;
          display: flex;
          align-items: center;
          justify-content: center;
          border-radius: 50%;
        }

        .close-btn:hover {
          background: #f3f4f6;
        }

        .login-form {
          display: flex;
          flex-direction: column;
          gap: 16px;
        }

        .form-group {
          display: flex;
          flex-direction: column;
          gap: 6px;
        }

        .form-group label {
          font-size: 14px;
          font-weight: 500;
          color: #374151;
        }

        .form-group input {
          padding: 12px;
          border: 2px solid #e5e7eb;
          border-radius: 8px;
          font-size: 16px;
          transition: border-color 0.2s ease;
        }

        .form-group input:focus {
          outline: none;
          border-color: #22c55e;
        }

        .form-group input:disabled {
          background: #f3f4f6;
          cursor: not-allowed;
        }

        .error-message {
          background: rgba(239, 68, 68, 0.1);
          color: #dc2626;
          padding: 12px;
          border-radius: 8px;
          font-size: 14px;
          border: 1px solid rgba(239, 68, 68, 0.2);
        }

        .form-actions {
          display: flex;
          gap: 12px;
          margin-top: 8px;
        }

        .test-users {
          margin-top: 16px;
          padding-top: 16px;
          border-top: 1px solid #e5e7eb;
          font-size: 12px;
          color: #6b7280;
          text-align: center;
        }

        .detection-stats {
          margin-top: 16px;
          padding: 16px;
          background: rgba(59, 130, 246, 0.1);
          border-radius: 12px;
          border: 1px solid rgba(59, 130, 246, 0.2);
        }

        .detection-stats h4 {
          margin: 0 0 12px 0;
          font-size: 14px;
          color: #1f2937;
        }

        .stats-grid {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 12px;
        }

        .stat-item {
          text-align: center;
          padding: 8px;
          background: white;
          border-radius: 8px;
          border: 1px solid #e5e7eb;
        }

        .stat-label {
          display: block;
          font-size: 12px;
          color: #6b7280;
          margin-bottom: 4px;
        }

        .stat-value {
          display: block;
          font-size: 16px;
          font-weight: bold;
          color: #3b82f6;
        }

        /* 响应式设计 */
        @media (max-width: 768px) {
          .user-panel {
            top: 10px;
            right: 10px;
            left: 10px;
            max-width: none;
          }

          .user-card {
            padding: 16px;
          }

          .action-buttons {
            flex-direction: column;
          }

          .modal-content {
            margin: 20px;
            width: calc(100% - 40px);
          }
        }

        /* 暗色模式支持 */
        @media (prefers-color-scheme: dark) {
          .user-card {
            background: rgba(31, 41, 55, 0.95);
            border-color: rgba(255, 255, 255, 0.1);
          }

          .user-name,
          .modal-header h3,
          .detection-stats h4 {
            color: #f9fafb;
          }

          .user-id,
          .prompt-text,
          .next-level {
            color: #9ca3af;
          }

          .points-section {
            background: rgba(34, 197, 94, 0.15);
          }

          .btn-secondary {
            background: #374151;
            color: #f9fafb;
          }

          .btn-secondary:hover:not(:disabled) {
            background: #4b5563;
          }

          .progress-bar {
            background: #4b5563;
          }

          .modal-content {
            background: #1f2937;
            color: #f9fafb;
          }

          .form-group input {
            background: #374151;
            border-color: #4b5563;
            color: #f9fafb;
          }

          .form-group input:focus {
            border-color: #22c55e;
          }

          .test-users {
            border-color: #374151;
          }

          .stat-item {
            background: #374151;
            border-color: #4b5563;
          }
        }

        /* 高对比度模式 */
        @media (prefers-contrast: high) {
          .user-card {
            border: 2px solid currentColor;
          }

          .btn {
            border: 2px solid currentColor;
          }

          .progress-bar {
            border: 1px solid currentColor;
          }
        }

        /* 减少动画偏好 */
        @media (prefers-reduced-motion: reduce) {
          .btn,
          .progress-fill,
          .form-group input {
            transition: none;
          }
        }
      `}</style>
    </div>
  )
}

export default UserPanel