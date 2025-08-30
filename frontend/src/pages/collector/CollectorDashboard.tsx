import React, { useState } from 'react';
import { useAuthStore } from '@/store/auth';
import { Button } from '@/components/ui/Button';
import { StatsCard } from '@/components/ui/Card';
import { 
  TruckIcon, 
  MapPinIcon, 
  DevicePhoneMobileIcon,
  ClockIcon
} from '@heroicons/react/24/outline'

const CollectorDashboard: React.FC = () => {
  const { currentUser } = useAuthStore()
  const [activeTab, setActiveTab] = useState<'tasks' | 'routes' | 'devices'>('tasks')

  // 收集员统计数据
  const collectorStats = [
    {
      title: '今日任务数',
      value: '12',
      icon: <TruckIcon className="w-8 h-8 text-blue-500" />,
      subtitle: '已完成 8/12'
    },
    {
      title: '待清运点',
      value: '6',
      icon: <MapPinIcon className="w-8 h-8 text-green-500" />,
      subtitle: '最远距离 2.3km'
    },
    {
      title: '设备状态',
      value: '4/5',
      icon: <DevicePhoneMobileIcon className="w-8 h-8 text-orange-500" />,
      subtitle: '1台需要维护'
    },
    {
      title: '工作时长',
      value: '6.5h',
      icon: <ClockIcon className="w-8 h-8 text-purple-500" />,
      subtitle: '今日工作时长'
    }
  ]

  // 模拟任务数据
  const tasks = [
    { id: 1, location: 'A小区垃圾站', status: '进行中', priority: '高', estimatedTime: '30分钟' },
    { id: 2, location: 'B商业区收集点', status: '待处理', priority: '中', estimatedTime: '45分钟' },
    { id: 3, location: 'C公园分类箱', status: '待处理', priority: '低', estimatedTime: '20分钟' },
    { id: 4, location: 'D办公楼回收点', status: '已完成', priority: '高', estimatedTime: '已完成' }
  ]

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">收集员工作台</h1>
              <p className="text-sm text-gray-600">
                您好，{currentUser?.username}！今日有{collectorStats[0].value}个清运任务
              </p>
            </div>
            
            <div className="flex space-x-2">
              <Button variant="outline" size="sm">
                上报设备状态
              </Button>
              <Button variant="primary" size="sm">
                开始导航
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
            {collectorStats.map((stat, index) => (
              <StatsCard
                key={index}
                title={stat.title}
                value={stat.value}
                icon={stat.icon}
                subtitle={stat.subtitle}
              />
            ))}
          </div>

          {/* Tab navigation */}
          <div className="bg-white rounded-lg shadow-sm mb-6">
            <nav className="flex space-x-8 px-6 border-b border-gray-200">
              {[
                { id: 'tasks', name: '任务列表', count: collectorStats[0].value },
                { id: 'routes', name: '路线规划', count: collectorStats[1].value },
                { id: 'devices', name: '设备管理', count: collectorStats[2].value }
              ].map((tab) => (
                <button
                  key={tab.id}
                  onClick={() => setActiveTab(tab.id as any)}
                  className={`py-4 px-1 border-b-2 font-medium text-sm ${
                    activeTab === tab.id
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700'
                  }`}
                >
                  {tab.name}
                  {tab.count && (
                    <span className="ml-2 py-0.5 px-2 bg-gray-100 rounded-full text-xs">
                      {tab.count}
                    </span>
                  )}
                </button>
              ))}
            </nav>

            {/* Tab content */}
            <div className="p-6">
              {activeTab === 'tasks' && (
                <div>
                  <h3 className="text-lg font-medium text-gray-900 mb-4">今日清运任务</h3>
                  <div className="space-y-3">
                    {tasks.map((task) => (
                      <div key={task.id} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                        <div className="flex items-center space-x-3">
                          <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                            <TruckIcon className="w-4 h-4 text-blue-600" />
                          </div>
                          <div>
                            <p className="font-medium text-gray-900">{task.location}</p>
                            <p className="text-sm text-gray-600">优先级: {task.priority}</p>
                          </div>
                        </div>
                        
                        <div className="text-right">
                          <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                            task.status === '进行中' ? 'bg-yellow-100 text-yellow-800' :
                            task.status === '待处理' ? 'bg-blue-100 text-blue-800' :
                            'bg-green-100 text-green-800'
                          }`}>
                            {task.status}
                          </span>
                          <p className="text-sm text-gray-500 mt-1">{task.estimatedTime}</p>
                        </div>
                      </div>
                    ))}
                  </div>
                  
                  <div className="mt-6 flex space-x-3">
                    <Button variant="primary">
                      开始下一个任务
                    </Button>
                    <Button variant="outline">
                      查看详细路线
                    </Button>
                  </div>
                </div>
              )}

              {activeTab === 'routes' && (
                <div>
                  <h3 className="text-lg font-medium text-gray-900 mb-4">最优路线规划</h3>
                  <div className="bg-gray-100 rounded-lg p-4 text-center">
                    <MapPinIcon className="w-12 h-12 text-gray-400 mx-auto mb-2" />
                    <p className="text-gray-600">路线规划功能开发中</p>
                    <p className="text-sm text-gray-500 mt-1">将集成地图导航和最优路径计算</p>
                  </div>
                </div>
              )}

              {activeTab === 'devices' && (
                <div>
                  <h3 className="text-lg font-medium text-gray-900 mb-4">设备状态管理</h3>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    {['智能垃圾桶A', '压缩设备B', '运输车C', '手持终端D', '称重设备E'].map((device, index) => (
                      <div key={index} className="bg-gray-50 p-4 rounded-lg">
                        <div className="flex items-center justify-between mb-2">
                          <span className="font-medium">{device}</span>
                          <span className={`inline-flex items-center px-2 py-0.5 rounded-full text-xs ${
                            index === 2 ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'
                          }`}>
                            {index === 2 ? '需维护' : '正常'}
                          </span>
                        </div>
                        <p className="text-sm text-gray-600">最后上报: 2小时前</p>
                        <Button size="sm" variant="outline" className="mt-2">
                          上报状态
                        </Button>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* Emergency section */}
          <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <span className="text-yellow-400 text-2xl">⚠️</span>
              </div>
              <div className="ml-3">
                <h3 className="text-sm font-medium text-yellow-800">紧急通知</h3>
                <p className="text-sm text-yellow-700">
                  今天下午3点有临时清运任务，请合理安排时间
                </p>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}

export default CollectorDashboard