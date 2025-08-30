import { useState, useCallback, useEffect } from 'react'

interface DetectionResult {
  class: string
  score: number
  bbox: [number, number, number, number] // [x, y, width, height]
}

interface UseObjectDetectionReturn {
  isModelLoaded: boolean
  loading: boolean
  error: string | null
  detectObjects: (imageData: ImageData) => Promise<DetectionResult[]>
  loadModel: () => Promise<void>
  unloadModel: () => void
}

// COCO-SSD模型类别映射（专注于垃圾相关物体）
const TRASH_RELATED_CLASSES = [
  'bottle', 'cup', 'fork', 'knife', 'spoon', 'bowl',
  'banana', 'apple', 'orange', 'broccoli', 'carrot', 'hot dog',
  'pizza', 'donut', 'cake', 'chair', 'couch', 'potted plant',
  'bed', 'dining table', 'toilet', 'tv', 'laptop', 'mouse',
  'remote', 'keyboard', 'cell phone', 'microwave', 'oven',
  'toaster', 'sink', 'refrigerator', 'book', 'clock', 'vase',
  'scissors', 'teddy bear', 'hair drier', 'toothbrush'
]

export const useObjectDetection = (): UseObjectDetectionReturn => {
  const [isModelLoaded, setIsModelLoaded] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [model, setModel] = any = useState(null)

  // 加载TensorFlow.js模型
  const loadModel = useCallback(async (): Promise<void> => {
    try {
      setLoading(true)
      setError(null)

      // 检查TensorFlow.js是否可用
      if (typeof window === 'undefined' || typeof window.tf === 'undefined') {
        throw new Error('TensorFlow.js未加载，请检查网络连接')
      }

      const tf = (window as any).tf

      // 检查WebGL支持（GPU加速）
      const backend = tf.getBackend()
      if (backend !== 'webgl') {
        console.warn('WebGL不可用，使用CPU模式，性能可能较差')
      }

      // 加载COCO-SSD模型
      console.log('开始加载COCO-SSD模型...')
      
      // 动态导入模型（避免阻塞主线程）
      const cocoSsd = await import('@tensorflow-models/coco-ssd')
      const loadedModel = await cocoSsd.load({
        base: 'mobilenet_v2', // 使用轻量级版本
      })

      setModel(loadedModel)
      setIsModelLoaded(true)
      console.log('COCO-SSD模型加载成功')

      // 预热模型（第一次推理较慢）
      await warmUpModel(loadedModel)

    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : '模型加载失败'
      setError(errorMessage)
      console.error('模型加载错误:', err)
      setIsModelLoaded(false)
    } finally {
      setLoading(false)
    }
  }, [])

  // 模型预热
  const warmUpModel = async (model: any): Promise<void> => {
    try {
      const tf = (window as any).tf
      // 创建空的张量进行预热
      const dummyInput = tf.zeros([1, 224, 224, 3])
      await model.detect(dummyInput)
      dummyInput.dispose()
      console.log('模型预热完成')
    } catch (warmUpError) {
      console.warn('模型预热失败:', warmUpError)
    }
  }

  // 卸载模型释放内存
  const unloadModel = useCallback((): void => {
    if (model && model.dispose) {
      model.dispose()
    }
    setModel(null)
    setIsModelLoaded(false)
    setError(null)
  }, [model])

  // 执行物体检测
  const detectObjects = useCallback(async (
    imageData: ImageData
  ): Promise<DetectionResult[]> => {
    if (!model || !isModelLoaded) {
      throw new Error('模型未加载')
    }

    try {
      const tf = (window as any).tf

      // 使用tensorflow的tidy来自动管理内存
      return await tf.tidy(() => {
        // 将ImageData转换为Tensor
        const tensor = tf.browser.fromPixels(imageData)

        // 执行检测
        const predictions = await model.detect(tensor)

        // 过滤和格式化结果
        const filteredPredictions = predictions
          .filter((pred: any) => 
            pred.score > 0.5 && // 置信度阈值
            TRASH_RELATED_CLASSES.includes(pred.class.toLowerCase())
          )
          .map((pred: any) => ({
            class: pred.class,
            score: pred.score,
            bbox: pred.bbox
          }))

        // 释放张量内存
        tensor.dispose()

        return filteredPredictions
      })

    } catch (err) {
      console.error('物体检测错误:', err)
      throw new Error('检测失败: ' + (err instanceof Error ? err.message : '未知错误'))
    }
  }, [model, isModelLoaded])

  // 自动加载模型
  useEffect(() => {
    let isMounted = true

    const initializeModel = async () => {
      if (!isModelLoaded && !loading) {
        await loadModel()
      }
    }

    if (isMounted) {
      initializeModel()
    }

    return () => {
      isMounted = false
      // 组件卸载时清理模型
      if (model) {
        unloadModel()
      }
    }
  }, [])

  // 内存管理：在标签页隐藏时卸载模型
  useEffect(() => {
    const handleVisibilityChange = () => {
      if (document.hidden && isModelLoaded) {
        // 页面隐藏时卸载模型节省内存
        unloadModel()
      } else if (!document.hidden && !isModelLoaded && !loading) {
        // 页面重新显示时加载模型
        loadModel()
      }
    }

    document.addEventListener('visibilitychange', handleVisibilityChange)

    return () => {
      document.removeEventListener('visibilitychange', handleVisibilityChange)
    }
  }, [isModelLoaded, loading, loadModel, unloadModel])

  return {
    isModelLoaded,
    loading,
    error,
    detectObjects,
    loadModel,
    unloadModel
  }
}