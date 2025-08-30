import React from 'react'
import { useAuthStore } from '@/store/auth'
import { StatsCard } from '@/components/ui/Card'
import { Button } from '@/components/ui/Button'
import { LoadingSpinner } from '@/components/ui/LoadingSpinner'
import {
  ChartBarIcon,
  CameraIcon,
  DocumentTextIcon,
  UsersIcon,
  ArrowUpIcon,
  ArrowDownIcon,
} from '@heroicons/react/24/outline'

const DashboardPage: React.FC = () => {
  const { currentUser, logout } = useAuthStore()

  // Mock data for demonstration
  const statsData = [
    {
      title: '总分类数量',
      value: '1,234',
      icon: <DocumentTextIcon className="w-8 h-8 text-blue-500" />,
      trend: { value: 12.5, isPositive: true },
      subtitle: '本月分类',
    },
    {
      title: '准确率',
      value: '92.3%',
      icon: <ChartBarIcon className="w-8 h-8 text-green-500" />,
      trend: { value: 3.2, isPositive: true },
      subtitle: '较上月提升',
    },
    {
      title: '活跃用户',
      value: '456',
      icon: <UsersIcon className="w-8 h-8 text-purple-500" />,
      trend: { value: 8.1, isPositive: true },
      subtitle: '今日活跃',
    },
    {
      title: '待处理',
      value: '23',
      icon: <CameraIcon className="w-8 h-8 text-orange-500" />,
      trend: { value: 15.4, isPositive: false },
      subtitle: '需要审核',
    },
  ]

  const recentActivities = [
    { id: 1, user: '张三', action: '分类了塑料瓶', time: '2分钟前', category: 'recyclable' },
    { id: 2, user: '李四', action: '上传了电池图片', time: '5分钟前', category: 'hazardous' },
    { id: 3, user: '王五', action: '完成了厨余垃圾识别', time: '10分钟前', category: 'kitchen' },
    { id: 4, user: '赵六', action: '报告了分类错误', time: '15分钟前', category: 'other' },
  ]

  const getCategoryColor = (category: string) => {
    const colors = {
      recyclable: 'text-blue-600 bg-blue-100',
      hazardous: 'text-red-600 bg-red-100',
      kitchen: 'text-green-600 bg-green-100',
      household: 'text-gray-600 bg-gray-100',
      other: 'text-purple-600 bg-purple-100',
    }
    return colors[category as keyof typeof colors] || colors.other
  }

  const getCategoryLabel = (category: string) => {
    const labels = {
      recyclable: '可回收',
      hazardous: '有害',
      kitchen: '厨余',
      household: '其他',
      other: '其他',
    }
    return labels[category as keyof typeof labels] || '其他'
  }

  if (!currentUser) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">控制面板</h1>
              <p className="text-sm text-gray-600">
                欢迎回来，{currentUser.username}！
              </p>
            </div>
            
            <div className="flex items-center space-x-4">
              <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800">
                {currentUser.role}
              </span>
              <Button
                variant="outline"
                size="sm"
                onClick={logout}
              >
                退出登录
              </Button>
            </div>
          </div>
        </div>
      </header>

      {/* Main content */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          {/* Stats grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            {statsData.map((stat, index) => (
              <StatsCard
                key={index}
                title={stat.title}
                value={stat.value}
                icon={stat.icon}
                trend={stat.trend}
                subtitle={stat.subtitle}
              />
            ))}
          </div>

          {/* Recent activity */}
          <div className="bg-white shadow-sm rounded-lg overflow-hidden">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900">最近活动</h2>
              <p className="mt-1 text-sm text-gray-600">
                系统最近的分类活动记录
              </p>
            </div>

            <div className="divide-y divide-gray-200">
              {recentActivities.map((activity) => (
                <div key={activity.id} className="px-6 py-4">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-3">
                      <div className="flex-shrink-0">
                        <div className={`w-8 h-8 rounded-full flex items-center justify-center ${getCategoryColor(activity.category)}`}>
                          <span className="text-xs font-medium">
                            {getCategoryLabel(activity.category).charAt(0)}
                          </span>
                        </div>
                      </div>
                      <div>
                        <p className="text-sm font-medium text-gray-900">
                          {activity.user}
                        </p>
                        <p className="text-sm text-gray-600">
                          {activity.action}
                        </p>
                      </div>
                    </div>
                    
                    <div className="text-right">
                      <p className="text-sm text-gray-500">
                        {activity.time}
                      </p>
                      <span className={`inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium ${getCategoryColor(activity.category)}`}>
                        {getCategoryLabel(activity.category)}
                      </span>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            <div className="px-6 py-4 bg-gray-50">
              <Button variant="outline" className="w-full">
                查看所有活动
              </Button>
            </div>
          </div>

          {/* Quick actions */}
          <div className="mt-8 grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
              <h3 className="text-lg font-medium text-gray-900 mb-4">
                快速分类
              </h3>
              <p className="text-sm text-gray-600 mb-4">
                上传图片开始垃圾分类识别
              </p>
              <Button className="w-full">
                开始分类
              </Button>
            </div>

            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
              <h3 className="text-lg font-medium text-gray-900 mb-4">
                数据统计
              </h3>
              <p className="text-sm text-gray-600 mb-4">
                查看详细的分类统计和分析报告
              </p>
              <Button variant="outline" className="w-full">
                查看统计
              </Button>
            </div>

            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
              <h3 className="text-lg font-medium text-gray-900 mb-4">
                系统设置
              </h3>
              <p className="text-sm text-gray-600 mb-4">
                管理账户设置和系统偏好
              </p>
              <Button variant="outline" className="w-full">
                前往设置
              </Button>
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}

export default DashboardPage