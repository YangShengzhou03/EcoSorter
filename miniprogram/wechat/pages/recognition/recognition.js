// pages/recognition/recognition.js
Page({
  data: {
    cameraContext: null,
    flashStatus: 'off',
    devicePosition: 'back',
    showResult: false,
    isLoading: false,
    photoPath: '',
    result: {
      type: '',
      confidence: 0,
      tips: ''
    }
  },

  onLoad() {
    this.initCamera()
  },

  onUnload() {
    // 页面卸载时释放摄像头资源
    if (this.data.cameraContext) {
      this.data.cameraContext.stopRecord()
    }
  },

  initCamera() {
    const cameraContext = wx.createCameraContext()
    this.setData({ cameraContext })
  },

  takePhoto() {
    if (this.data.isLoading) return

    this.setData({ isLoading: true })

    this.data.cameraContext.takePhoto({
      quality: 'high',
      success: (res) => {
        this.setData({
          photoPath: res.tempImagePath,
          isLoading: false
        })
        this.recognizeGarbage(res.tempImagePath)
      },
      fail: (err) => {
        console.error('拍照失败:', err)
        wx.showToast({
          title: '拍照失败',
          icon: 'error'
        })
        this.setData({ isLoading: false })
      }
    })
  },

  switchCamera() {
    this.setData({
      devicePosition: this.data.devicePosition === 'back' ? 'front' : 'back'
    })
  },

  toggleFlash() {
    const newFlashStatus = this.data.flashStatus === 'off' ? 'on' : 'off'
    this.setData({ flashStatus: newFlashStatus })
    
    wx.showToast({
      title: newFlashStatus === 'on' ? '闪光灯已开启' : '闪光灯已关闭',
      icon: 'none'
    })
  },

  cameraError(e) {
    console.error('摄像头错误:', e.detail)
    wx.showModal({
      title: '摄像头错误',
      content: '请检查摄像头权限或重启小程序',
      showCancel: false
    })
  },

  recognizeGarbage(imagePath) {
    this.setData({ isLoading: true })

    // 模拟AI识别过程
    setTimeout(() => {
      // 模拟识别结果
      const garbageTypes = [
        {
          type: '可回收垃圾',
          confidence: 92,
          tips: '请投放到蓝色垃圾桶，包括纸张、塑料、玻璃、金属等可回收利用的物品。'
        },
        {
          type: '厨余垃圾',
          confidence: 88,
          tips: '请投放到绿色垃圾桶，包括剩菜剩饭、瓜皮果核、花卉绿植等易腐垃圾。'
        },
        {
          type: '有害垃圾',
          confidence: 95,
          tips: '请投放到红色垃圾桶，包括电池、荧光灯管、过期药品等对环境和健康有害的物品。'
        },
        {
          type: '其他垃圾',
          confidence: 85,
          tips: '请投放到灰色垃圾桶，包括卫生纸、烟头、陶瓷碎片等难以回收的废弃物。'
        }
      ]

      const randomResult = garbageTypes[Math.floor(Math.random() * garbageTypes.length)]
      
      this.setData({
        result: randomResult,
        showResult: true,
        isLoading: false
      })

      // 记录识别历史
      this.recordRecognitionHistory(randomResult)

    }, 2000) // 模拟2秒识别时间
  },

  recordRecognitionHistory(result) {
    const history = wx.getStorageSync('recognitionHistory') || []
    history.unshift({
      type: result.type,
      confidence: result.confidence,
      timestamp: new Date().getTime(),
      imagePath: this.data.photoPath
    })
    
    // 只保留最近20条记录
    if (history.length > 20) {
      history.pop()
    }
    
    wx.setStorageSync('recognitionHistory', history)
  },

  closeModal() {
    this.setData({ showResult: false })
  },

  retakePhoto() {
    this.setData({ showResult: false })
  },

  confirmResult() {
    this.setData({ showResult: false })
    
    // 更新用户积分
    this.updateUserPoints()
    
    wx.showToast({
      title: '识别完成，+10积分',
      icon: 'success'
    })

    // 返回首页
    setTimeout(() => {
      wx.navigateBack()
    }, 1500)
  },

  updateUserPoints() {
    let userStats = wx.getStorageSync('userStats') || {
      todayCount: 0,
      totalCount: 0,
      points: 0
    }
    
    userStats.todayCount += 1
    userStats.totalCount += 1
    userStats.points += 10
    
    wx.setStorageSync('userStats', userStats)
  },

  onShareAppMessage() {
    return {
      title: '智能垃圾分类识别',
      path: '/pages/recognition/recognition',
      imageUrl: this.data.photoPath || '/images/share-recognition.jpg'
    }
  }
})