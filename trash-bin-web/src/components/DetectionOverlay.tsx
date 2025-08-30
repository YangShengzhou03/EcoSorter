import React from 'react'

interface DetectionResult {
  class: string
  score: number
  bbox: [number, number, number, number] // [x, y, width, height]
  trashType?: 'recyclable' | 'hazardous' | 'household' | 'other'
}

interface DetectionOverlayProps {
  detections: DetectionResult[]
  videoWidth: number
  videoHeight: number
  containerWidth?: number
  containerHeight?: number
}

// 垃圾类型颜色映射
const TRASH_TYPE_COLORS = {
  recyclable: '#22c55e', // 绿色 - 可回收
  hazardous: '#ef4444', // 红色 - 有害
  household: '#f59e0b', // 橙色 - 厨余
  other: '#6b7280'     // 灰色 - 其他
}

// 垃圾类型标签文本
const TRASH_TYPE_LABELS = {
  recyclable: '可回收',
  hazardous: '有害垃圾',
  household: '厨余垃圾',
  other: '其他垃圾'
}

const DetectionOverlay: React.FC<DetectionOverlayProps> = ({
  detections,
  videoWidth,
  videoHeight,
  containerWidth = videoWidth,
  containerHeight = videoHeight
}) => {
  // 计算缩放比例
  const scaleX = containerWidth / videoWidth
  const scaleY = containerHeight / videoHeight

  // 格式化置信度显示
  const formatConfidence = (score: number): string => {
    return (score * 100).toFixed(1) + '%'
  }

  // 获取显示名称（中文本地化）
  const getDisplayName = (className: string, trashType?: string): string => {
    const classNames: Record<string, string> = {
      'bottle': '瓶子',
      'cup': '杯子',
      'fork': '叉子',
      'knife': '刀子',
      'spoon': '勺子',
      'bowl': '碗',
      'banana': '香蕉',
      'apple': '苹果',
      'orange': '橙子',
      'broccoli': '西兰花',
      'carrot': '胡萝卜',
      'pizza': '披萨',
      'donut': '甜甜圈',
      'cake': '蛋糕',
      'book': '书本',
      'cell phone': '手机',
      'laptop': '笔记本电脑',
      'remote': '遥控器',
      'keyboard': '键盘',
      'mouse': '鼠标'
    }

    return classNames[className.toLowerCase()] || className
  }

  if (!detections.length || !videoWidth || !videoHeight) {
    return null
  }

  return (
    <div 
      className="detection-overlay"
      style={{
        position: 'absolute',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        pointerEvents: 'none',
        zIndex: 10
      }}
    >
      {detections.map((detection, index) => {
        const [x, y, width, height] = detection.bbox
        const scaledX = x * scaleX
        const scaledY = y * scaleY
        const scaledWidth = width * scaleX
        const scaledHeight = height * scaleY

        const borderColor = detection.trashType 
          ? TRASH_TYPE_COLORS[detection.trashType] 
          : TRASH_TYPE_COLORS.other

        const typeLabel = detection.trashType 
          ? TRASH_TYPE_LABELS[detection.trashType]
          : '未知类型'

        return (
          <div
            key={index}
            className="detection-box"
            style={{
              position: 'absolute',
              left: scaledX,
              top: scaledY,
              width: scaledWidth,
              height: scaledHeight,
              border: `3px solid ${borderColor}`,
              borderRadius: '8px',
              backgroundColor: 'rgba(0, 0, 0, 0.2)',
              boxShadow: `0 0 0 2px ${borderColor}40`,
              pointerEvents: 'none'
            }}
          >
            {/* 标签背景 */}
            <div
              className="detection-label"
              style={{
                position: 'absolute',
                top: '-28px',
                left: '0',
                background: borderColor,
                color: 'white',
                padding: '4px 8px',
                borderRadius: '6px',
                fontSize: '12px',
                fontWeight: 'bold',
                whiteSpace: 'nowrap',
                maxWidth: '200px',
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                boxShadow: '0 2px 8px rgba(0, 0, 0, 0.3)'
              }}
            >
              <span className="label-text">
                {getDisplayName(detection.class, detection.trashType)}
              </span>
              
              <span className="confidence-badge" style={{ 
                marginLeft: '6px', 
                background: 'rgba(255, 255, 255, 0.2)', 
                padding: '2px 6px', 
                borderRadius: '4px',
                fontSize: '10px'
              }}>
                {formatConfidence(detection.score)}
              </span>
            </div>

            {/* 类型标签 */}
            <div
              className="type-label"
              style={{
                position: 'absolute',
                bottom: '-28px',
                left: '0',
                background: borderColor,
                color: 'white',
                padding: '4px 8px',
                borderRadius: '6px',
                fontSize: '12px',
                fontWeight: 'bold',
                whiteSpace: 'nowrap',
                boxShadow: '0 2px 8px rgba(0, 0, 0, 0.3)'
              }}
            >
              {typeLabel}
            </div>

            {/* 角标指示器 */}
            {['top-left', 'top-right', 'bottom-left', 'bottom-right'].map((corner) => (
              <div
                key={corner}
                className="corner-indicator"
                style={{
                  position: 'absolute',
                  width: '12px',
                  height: '12px',
                  background: borderColor,
                  [corner.split('-')[0]]: '-6px',
                  [corner.split('-')[1]]: '-6px',
                  borderRadius: '2px',
                  transform: 'rotate(45deg)'
                }}
              />
            ))}
          </div>
        )
      })}

      {/* 统计信息面板 */}
      {detections.length > 0 && (
        <div
          className="stats-panel"
          style={{
            position: 'absolute',
            top: '16px',
            right: '16px',
            background: 'rgba(0, 0, 0, 0.8)',
            color: 'white',
            padding: '12px 16px',
            borderRadius: '12px',
            fontSize: '14px',
            backdropFilter: 'blur(10px)',
            border: '1px solid rgba(255, 255, 255, 0.1)'
          }}
        >
          <div style={{ fontWeight: 'bold', marginBottom: '8px' }}>
            🎯 检测结果
          </div>
          
          <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
            <div>识别数量: {detections.length} 个</div>
            
            {/* 分类统计 */}
            {Object.entries(TRASH_TYPE_LABELS).map(([type, label]) => {
              const count = detections.filter(d => d.trashType === type).length
              if (count === 0) return null
              
              return (
                <div key={type} style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                  <div 
                    style={{ 
                      width: '12px', 
                      height: '12px', 
                      background: TRASH_TYPE_COLORS[type as keyof typeof TRASH_TYPE_COLORS],
                      borderRadius: '2px'
                    }}
                  />
                  <span>{label}: {count}</span>
                </div>
              )
            })}
          </div>
        </div>
      )}

      <style jsx>{`
        .detection-box {
          transition: all 0.3s ease;
          animation: pulse 2s infinite;
        }

        @keyframes pulse {
          0% { box-shadow: 0 0 0 2px rgba(34, 197, 94, 0.4); }
          50% { box-shadow: 0 0 0 6px rgba(34, 197, 94, 0.2); }
          100% { box-shadow: 0 0 0 2px rgba(34, 197, 94, 0.4); }
        }

        .detection-label {
          display: flex;
          align-items: center;
          font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
        }

        .confidence-badge {
          font-feature-settings: 'tnum';
          font-variant-numeric: tabular-nums;
        }

        /* 响应式调整 */
        @media (max-width: 768px) {
          .detection-label {
            font-size: 10px;
            padding: 2px 6px;
            top: -22px;
          }
          
          .type-label {
            font-size: 10px;
            padding: 2px 6px;
            bottom: -22px;
          }
          
          .confidence-badge {
            font-size: 8px;
            padding: 1px 4px;
          }
          
          .stats-panel {
            font-size: 12px;
            padding: 8px 12px;
            top: 8px;
            right: 8px;
          }
        }

        /* 高对比度模式支持 */
        @media (prefers-contrast: high) {
          .detection-box {
            border-width: 4px;
          }
          
          .detection-label,
          .type-label {
            border: 2px solid white;
          }
        }

        /* 减少动画偏好 */
        @media (prefers-reduced-motion: reduce) {
          .detection-box {
            animation: none;
            transition: none;
          }
        }
      `}</style>
    </div>
  )
}

export default DetectionOverlay