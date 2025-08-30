import React from 'react'
import { useAuthStore } from '@/store/auth'
import { Button } from '@/components/ui/Button'
import { StatsCard } from '@/components/ui/Card'
import { 
  TrashIcon, 
  ChartBarIcon, 
  GiftIcon, 
  ClockIcon 
} from '@heroicons/react/24/outline'

const ResidentDashboard: React.FC = () => {
  const { currentUser } = useAuthStore()

  // 居民用户统计数据
  const residentStats = [
    {
      title: '今日分类次数',
      value: '8',
      icon: <TrashIcon className="w-8 h-8 text-green-500" />,
      subtitle: '较昨日 +2次'
    },
    {
      title: '当前积分',
      value: '1,245',
      icon: <GiftIcon className="w-8 h-8 text-blue-500" />,
      subtitle: '可兑换奖励'
    },
    {
      title: '分类准确率',
      value: '94.2%',
      icon: <ChartBarIcon className="w-8 h-8 text-purple-500" />,
      subtitle: '个人最佳记录'
    },
    {
      title: '环保贡献',
      value: '28.5kg',
      icon: <ClockIcon className="w-8 h-8 text-orange-500" />,
      subtitle: '累计减少碳排放'
    }
  ]

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">居民控制面板</h1>
              <p className="text-sm text-gray-600">
                欢迎回来，{currentUser?.username}！继续为环保贡献力量
              </p>
            </div>
          </div>
        </div>
      </header>

      {/* Main content */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          {/* Stats grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            {residentStats.map((stat, index) => (
              <StatsCard
                key={index}
                title={stat.title}
                value={stat.value}
                icon={stat.icon}
                subtitle={stat.subtitle}
              />
            ))}
          </div>

          {/* Quick actions */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
              <h3 className="text-lg font-medium text-gray-900 mb-4">
                🗑️ 垃圾分类
              </h3>
              <p className="text-sm text-gray-600 mb-4">
                上传垃圾图片，智能识别分类
              </p>
              <Button className="w-full" variant="primary">
                开始分类
              </Button>
            </div>

            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
              <h3 className="text-lg font-medium text-gray-900 mb-4">
                💰 积分兑换
              </h3>
              <p className="text-sm text-gray-600 mb-4">
                使用积分兑换环保奖励
              </p>
              <Button className="w-full" variant="outline">
                查看奖励
              </Button>
            </div>

            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
              <h3 className="text-lg font-medium text-gray-900 mb-4">
                📊 我的记录
              </h3>
              <p className="text-sm text-gray-600 mb-4">
                查看分类历史和积分明细
              </p>
              <Button className="w-full" variant="outline">
                查看记录
              </Button>
            </div>
          </div>

          {/* Recent activity */}
          <div className="bg-white shadow-sm rounded-lg overflow-hidden">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900">最近分类记录</h2>
              <p className="mt-1 text-sm text-gray-600">
                您最近的垃圾分类活动
              </p>
            </div>

            <div className="divide-y divide-gray-200">
              {[
                { id: 1, item: '塑料瓶', category: '可回收', points: 5, time: '10分钟前' },
                { id: 2, item: '电池', category: '有害垃圾', points: 8, time: '2小时前' },
                { id: 3, item: '菜叶', category: '厨余垃圾', points: 3, time: '5小时前' },
                { id: 4, item: '纸巾', category: '其他垃圾', points: 1, time: '昨天' }
              ].map((activity) => (
                <div key={activity.id} className="px-6 py-4">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-3">
                      <div className="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center">
                        <span className="text-sm font-medium text-green-800">
                          +{activity.points}
                        </span>
                      </div>
                      <div>
                        <p className="text-sm font-medium text-gray-900">
                          {activity.item}
                        </p>
                        <p className="text-sm text-gray-600">
                          {activity.category}
                        </p>
                      </div>
                    </div>
                    <p className="text-sm text-gray-500">
                      {activity.time}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}

export default ResidentDashboard