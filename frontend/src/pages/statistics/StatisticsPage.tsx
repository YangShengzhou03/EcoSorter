import React from 'react';

const StatisticsPage: React.FC = () => {
  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">数据统计</h1>
        
        <div className="bg-white rounded-lg shadow-sm p-6">
          <h2 className="text-xl font-semibold text-gray-800 mb-4">
            统计功能开发中
          </h2>
          <p className="text-gray-600">
            此功能正在开发中，将提供详细的垃圾分类数据统计和分析报告。
          </p>
        </div>
      </div>
    </div>
  );
};

export default StatisticsPage;