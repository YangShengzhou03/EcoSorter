import React, { ErrorInfo, ReactNode } from 'react'

interface ErrorBoundaryProps {
  children: ReactNode
  fallback?: ReactNode
  onError?: (error: Error, errorInfo: ErrorInfo) => void
}

interface ErrorBoundaryState {
  hasError: boolean
  error?: Error
  errorInfo?: ErrorInfo
}

class ErrorBoundary extends React.Component<ErrorBoundaryProps, ErrorBoundaryState> {
  constructor(props: ErrorBoundaryProps) {
    super(props)
    this.state = {
      hasError: false
    }
  }

  static getDerivedStateFromError(error: Error): Partial<ErrorBoundaryState> {
    return {
      hasError: true,
      error
    }
  }

  componentDidCatch(error: Error, errorInfo: ErrorInfo): void {
    this.setState({
      errorInfo
    })

    // 调用自定义错误处理回调
    if (this.props.onError) {
      this.props.onError(error, errorInfo)
    }

    // 记录错误到控制台
    console.error('ErrorBoundary caught an error:', error, errorInfo)

    // 可以在这里发送错误报告到服务器
    this.sendErrorReport(error, errorInfo)
  }

  // 发送错误报告到服务器
  private sendErrorReport(error: Error, errorInfo: ErrorInfo): void {
    try {
      const errorReport = {
        timestamp: new Date().toISOString(),
        message: error.message,
        stack: error.stack,
        componentStack: errorInfo.componentStack,
        userAgent: navigator.userAgent,
        url: window.location.href,
        // 可以添加更多上下文信息
        deviceInfo: JSON.parse(localStorage.getItem('deviceInfo') || '{}'),
        authState: !!localStorage.getItem('auth_userId')
      }

      // 在实际项目中，这里应该发送到错误收集服务
      console.log('Error report:', errorReport)

      // 示例：发送到后端API
      // fetch('/api/error-report', {
      //   method: 'POST',
      //   headers: { 'Content-Type': 'application/json' },
      //   body: JSON.stringify(errorReport)
      // })
    } catch (reportError) {
      console.error('Failed to send error report:', reportError)
    }
  }

  // 重置错误状态
  private resetError = (): void => {
    this.setState({
      hasError: false,
      error: undefined,
      errorInfo: undefined
    })
  }

  // 重新加载页面
  private reloadPage = (): void => {
    window.location.reload()
  }

  render(): ReactNode {
    if (this.state.hasError) {
      // 显示自定义fallback UI
      if (this.props.fallback) {
        return this.props.fallback
      }

      // 默认错误页面
      return (
        <div className="error-boundary">
          <div className="error-content">
            <div className="error-icon">💥</div>
            
            <h1 className="error-title">
              哎呀，出错了！
            </h1>
            
            <p className="error-message">
              应用程序遇到了意外错误
            </p>

            {/* 开发环境显示详细错误信息 */}
            {import.meta.env.DEV && this.state.error && (
              <div className="error-details">
                <h3>错误详情（仅开发环境显示）:</h3>
                <pre className="error-stack">
                  {this.state.error.stack}
                </pre>
                {this.state.errorInfo && (
                  <pre className="component-stack">
                    {this.state.errorInfo.componentStack}
                  </pre>
                )}
              </div>
            )}

            <div className="error-actions">
              <button 
                className="btn btn-primary"
                onClick={this.resetError}
              >
                重试
              </button>
              
              <button 
                className="btn btn-secondary"
                onClick={this.reloadPage}
              >
                重新加载页面
              </button>
            </div>

            <div className="error-help">
              <p>如果问题持续存在，请联系技术支持</p>
              <div className="contact-info">
                <span>📧 support@ecosorter.com</span>
                <span>📞 400-123-4567</span>
              </div>
            </div>
          </div>

          <style jsx>{`
            .error-boundary {
              min-height: 100vh;
              display: flex;
              align-items: center;
              justify-content: center;
              background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
              padding: 20px;
              font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
            }

            .error-content {
              max-width: 600px;
              text-align: center;
              background: white;
              padding: 40px;
              border-radius: 20px;
              box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
              border: 1px solid #e2e8f0;
            }

            .error-icon {
              font-size: 64px;
              margin-bottom: 24px;
            }

            .error-title {
              margin: 0 0 16px 0;
              font-size: 28px;
              font-weight: 700;
              color: #1f2937;
            }

            .error-message {
              margin: 0 0 32px 0;
              font-size: 16px;
              color: #6b7280;
              line-height: 1.5;
            }

            .error-details {
              margin: 32px 0;
              padding: 20px;
              background: #f8fafc;
              border-radius: 12px;
              border: 1px solid #e2e8f0;
              text-align: left;
            }

            .error-details h3 {
              margin: 0 0 12px 0;
              font-size: 16px;
              color: #374151;
            }

            .error-stack,
            .component-stack {
              font-size: 12px;
              color: #6b7280;
              white-space: pre-wrap;
              word-break: break-all;
              margin: 0;
              font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
            }

            .component-stack {
              margin-top: 12px;
              color: #9ca3af;
            }

            .error-actions {
              display: flex;
              gap: 12px;
              justify-content: center;
              margin-bottom: 32px;
            }

            .btn {
              padding: 12px 24px;
              border: none;
              border-radius: 8px;
              font-size: 14px;
              font-weight: 500;
              cursor: pointer;
              transition: all 0.2s ease;
            }

            .btn-primary {
              background: #22c55e;
              color: white;
            }

            .btn-primary:hover {
              background: #16a34a;
            }

            .btn-secondary {
              background: #f3f4f6;
              color: #374151;
            }

            .btn-secondary:hover {
              background: #e5e7eb;
            }

            .error-help {
              padding-top: 24px;
              border-top: 1px solid #f1f5f9;
            }

            .error-help p {
              margin: 0 0 16px 0;
              color: #6b7280;
              font-size: 14px;
            }

            .contact-info {
              display: flex;
              flex-direction: column;
              gap: 8px;
              font-size: 14px;
              color: #9ca3af;
            }

            /* 响应式设计 */
            @media (max-width: 768px) {
              .error-content {
                padding: 24px;
                margin: 20px;
              }

              .error-title {
                font-size: 24px;
              }

              .error-actions {
                flex-direction: column;
              }

              .btn {
                width: 100%;
              }

              .contact-info {
                flex-direction: column;
                text-align: center;
              }
            }

            /* 暗色模式支持 */
            @media (prefers-color-scheme: dark) {
              .error-boundary {
                background: linear-gradient(135deg, #111827 0%, #1f2937 100%);
              }

              .error-content {
                background: #1f2937;
                border-color: #374151;
                color: #f9fafb;
              }

              .error-title {
                color: #f9fafb;
              }

              .error-message {
                color: #9ca3af;
              }

              .error-details {
                background: #111827;
                border-color: #374151;
              }

              .error-details h3 {
                color: #f9fafb;
              }

              .btn-secondary {
                background: #374151;
                color: #f9fafb;
              }

              .btn-secondary:hover {
                background: #4b5563;
              }

              .error-help {
                border-color: #374151;
              }

              .error-help p {
                color: #9ca3af;
              }
            }

            /* 高对比度模式 */
            @media (prefers-contrast: high) {
              .error-content {
                border: 2px solid currentColor;
              }

              .error-details {
                border: 2px solid currentColor;
              }

              .btn {
                border: 2px solid currentColor;
              }

              .error-help {
                border-top: 2px solid currentColor;
              }
            }

            /* 减少动画偏好 */
            @media (prefers-reduced-motion: reduce) {
              .btn {
                transition: none;
              }
            }
          `}</style>
        </div>
      )
    }

    return this.props.children
  }
}

export default ErrorBoundary