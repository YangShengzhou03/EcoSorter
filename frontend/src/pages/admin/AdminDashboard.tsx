import React, { useState } from 'react'
import { useAuthStore } from '@/store/auth'
import { Button } from '@/components/ui/Button'
import { StatsCard } from '@/components/ui/Card'
import { 
  UsersIcon, 
  DeviceTabletIcon, 
  CogIcon,
  ChartBarIcon
} from '@heroicons/react/24/outline'

const AdminDashboard: React.FC = () => {
  const { currentUser } = useAuthStore()
  const [activeTab, setActiveTab] = useState<'overview' | 'users' | 'devices' | 'settings'>('overview')

  // 管理员统计数据
  const adminStats = [
    {
      title: '总用户数',
      value: '1,234',
      icon: <UsersIcon className="w-8 h-8 text-blue-500" />,
      subtitle: '今日新增 12人'
    },
    {
      title: '在线设备',
      value: '89/120',
      icon: <DeviceTabletIcon className="w-8 h-8 text-green-500" />,
      subtitle: '设备正常率 92%'
    },
    {
      title: '今日分类量',
      value: '5,678',
      icon: <ChartBarIcon className="w-8 h-8 text-purple-500" />,
      subtitle: '较昨日 +15%'
    },
    {
      title: '系统状态',
      value: '正常',
      icon: <CogIcon className="w-8 h-8 text-orange-500" />,
      subtitle: '运行时间 28天'
    }
  ]

  // 模拟用户数据
  const users = [
    { id: 1, username: '张三', role: '居民用户', status: '活跃', lastLogin: '2小时前' },
    { id: 2, username: '李四', role: '垃圾收集员', status: '工作中', lastLogin: '30分钟前' },
    { id: 3, username: '王五', role: '居民用户', status: '离线', lastLogin: '1天前' },
    { id: 4, username: '赵六', role: '管理员', status: '活跃', lastLogin: '当前' }
  ]

  // 模拟设备数据
  const devices = [
    { id: 1, name: '智能垃圾桶A', location: 'A小区', status: '正常', capacity: '75%' },
    { id: 2, name: '智能垃圾桶B', location: 'B商业区', status: '正常', capacity: '45%' },
    { id: 3, name: '智能垃圾桶C', location: 'C公园', status: '警告', capacity: '95%' },
    { id: 4, name: '压缩设备D', location: '中转站', status: '维护中', capacity: 'N/A' }
  ]

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">系统管理控制台</h1>
              <p className="text-sm text-gray-600">
                您好，{currentUser?.username}！系统运行状态良好
              </p>
            </div>
            
            <div className="flex space-x-2">
              <Button variant="outline" size="sm">
                系统日志
              </Button>
              <Button variant="primary" size="sm">
                刷新数据
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
            {adminStats.map((stat, index) => (
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
                { id: 'overview', name: '概览', icon: <ChartBarIcon className="w-4 h-4" /> },
                { id: 'users', name: '用户管理', icon: <UsersIcon className="w-4 h-4" /> },
                { id: 'devices', name: '设备监控', icon: <DeviceTabletIcon className="w-4 h-4" /> },
                { id: 'settings', name: '系统设置', icon: <CogIcon className="w-4 h-4" /> }
              ].map((tab) => (
                <button
                  key={tab.id}
                  onClick={() => setActiveTab(tab.id as any)}
                  className={`py-4 px-1 border-b-2 font-medium text-sm flex items-center space-x-1 ${
                    activeTab === tab.id
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700'
                  }`}
                >
                  {tab.icon}
                  <span>{tab.name}</span>
                </button>
              ))}
            </nav>

            {/* Tab content */}
            <div className="p-6">
              {activeTab === 'overview' && (
                <div>
                  <h3 className="text-lg font-medium text-gray-900 mb-4">系统概览</h3>
                  
                  {/* Quick stats */}
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
                    <div className="bg-blue-50 p-4 rounded-lg">
                      <div className="flex items-center">
                        <UsersIcon className="w-6 h-6 text-blue-600 mr-2" />
                        <h4 className="font-medium text-blue-900">用户活跃度</h4>
                      </div>
                      <p className="text-2xl font-bold text-blue-800 mt-2">78%</p>
                      <p className="text-sm text-blue-700">较上周 +5%</p>
                    </div>
                    
                    <div className="bg-green-50 p-4 rounded-lg">
                      <div className="flex items-center">
                        <DeviceTabletIcon className="w-6 h-6 text-green-600 mr-2" />
                        <h4 className="font-medium text-green-900">设备正常运行率</h4>
                      </div>
                      <p className="text-2xl font-bold text-green-800 mt-2">98.5%</p>
                      <p className="text-sm text-green-700">过去24小时</p>
                    </div>
                  </div>

                  {/* Recent activities */}
                  <div className="bg-gray-50 p-4 rounded-lg">
                    <h4 className="font-medium text-gray-900 mb-3">最近系统活动</h4>
                    <div className="space-y-2">
                      {[
                        '系统备份完成 - 2分钟前',
                        '用户「李四」登录系统 - 10分钟前',
                        '设备「智能垃圾桶C」发出容量警告 - 15分钟前',
                        '分类规则更新完成 - 1小时前'
                      ].map((activity, index) => (
                        <p key={index} className="text-sm text-gray-600 flex items-center">
                          <span className="w-2 h-2 bg-green-400 rounded-full mr-2"></span>
                          {activity}
                        </p>
                      ))}
                    </div>
                  </div>
                </div>
              )}

              {activeTab === 'users' && (
                <div>
                  <div className="flex justify-between items-center mb-4">
                    <h3 className="text-lg font-medium text-gray-900">用户管理</h3>
                    <Button size="sm">
                      添加用户
                    </Button>
                  </div>
                  
                  <div className="overflow-hidden shadow ring-1 ring-black ring-opacity-5 rounded-lg">
                    <table className="min-w-full divide-y divide-gray-300">
                      <thead className="bg-gray-50">
                        <tr>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">用户</th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">角色</th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">最后登录</th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                        </tr>
                      </thead>
                      <tbody className="bg-white divide-y divide-gray-200">
                        {users.map((user) => (
                          <tr key={user.id}>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="flex items-center">
                                <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                                  <span className="text-sm font-medium text-blue-800">
                                    {user.username.charAt(0)}
                                  </span>
                                </div>
                                <div className="ml-4">
                                  <div className="text-sm font-medium text-gray-900">{user.username}</div>
                                </div>
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                                {user.role}
                              </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                                user.status === '活跃' ? 'bg-green-100 text-green-800' :
                                user.status === '工作中' ? 'bg-yellow-100 text-yellow-800' :
                                'bg-gray-100 text-gray-800'
                              }`}>
                                {user.status}
                              </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                              {user.lastLogin}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                              <button className="text-blue-600 hover:text-blue-900 mr-3">
                                编辑
                              </button>
                              <button className="text-red-600 hover:text-red-900">
                                删除
                              </button>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              )}

              {activeTab === 'devices' && (
                <div>
                  <div className="flex justify-between items-center mb-4">
                    <h3 className="text-lg font-medium text-gray-900">设备监控</h3>
                    <Button size="sm" variant="outline">
                      刷新状态
                    </Button>
                  </div>
                  
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    {devices.map((device) => (
                      <div key={device.id} className="bg-white p-4 rounded-lg shadow-sm border">
                        <div className="flex items-center justify-between mb-2">
                          <h4 className="font-medium text-gray-900">{device.name}</h4>
                          <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                            device.status === '正常' ? 'bg-green-100 text-green-800' :
                            device.status === '警告' ? 'bg-yellow-100 text-yellow-800' :
                            'bg-red-100 text-red-800'
                          }`}>
                            {device.status}
                          </span>
                        </div>
                        
                        <p className="text-sm text-gray-600 mb-2">位置: {device.location}</p>
                        
                        {device.capacity !== 'N/A' && (
                          <div className="mt-3">
                            <div className="flex justify-between text-sm text-gray-600 mb-1">
                              <span>容量</span>
                              <span>{device.capacity}</span>
                            </div>
                            <div className="w-full bg-gray-200 rounded-full h-2">
                              <div 
                                className={`h-2 rounded-full ${
                                  parseInt(device.capacity) < 70 ? 'bg-green-500' :
                                  parseInt(device.capacity) < 90 ? 'bg-yellow-500' :
                                  'bg-red-500'
                                }`}
                                style={{ width: device.capacity }}
                              />
                            </div>
                          </div>
                        )}
                        
                        <div className="mt-3 flex space-x-2">
                          <Button size="sm" variant="outline">
                            详情
                          </Button>
                          <Button size="sm" variant="outline">
                            远程控制
                          </Button>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {activeTab === 'settings' && (
                <div>
                  <h3 className="text-lg font-medium text-gray-900 mb-4">系统设置</h3>
                  
                  <div className="space-y-4">
                    <div className="bg-white p-4 rounded-lg shadow-sm">
                      <h4 className="font-medium text-gray-900 mb-2">分类规则配置</h4>
                      <p className="text-sm text-gray-600 mb-3">管理垃圾分类规则和标准</p>
                      <Button size="sm">
                        编辑规则
                      </Button>
                    </div>
                    
                    <div className="bg-white p-4 rounded-lg shadow-sm">
                      <h4 className="font-medium text-gray-900 mb-2">积分系统设置</h4>
                      <p className="text-sm text-gray-600 mb-3">配置积分计算规则和奖励机制</p>
                      <Button size="sm">
                        积分设置
                      </Button>
                    </div>
                    
                    <div className="bg-white p-4 rounded-lg shadow-sm">
                      <h4 className="font-medium text-gray-900 mb-2">通知系统</h4>
                      <p className="text-sm text-gray-600 mb-3">配置系统通知和警报设置</p>
                      <Button size="sm">
                        通知设置
                      </Button>
                    </div>
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* System alerts */}
          <div className="bg-red-50 border border-red-200 rounded-lg p-4">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <span className="text-red-400 text-2xl">🚨</span>
              </div>
              <div className="ml-3">
                <h3 className="text-sm font-medium text-red-800">系统警报</h3>
                <p className="text-sm text-red-700">
                  智能垃圾桶C容量已达95%，请及时安排清运
                </p>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}

export default AdminDashboard