import 'package:flutter/material.dart';
import 'package:ecosorter/theme/app_theme.dart';

class PrivacyPolicyScreen extends StatelessWidget {
  const PrivacyPolicyScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.backgroundColor,
      appBar: AppBar(
        title: const Text('隐私政策'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildSection(
              '一、引言',
              'EcoSorter智能垃圾分类系统（以下简称"我们"）非常重视用户的隐私和个人信息保护。本隐私政策将向您说明我们如何收集、使用、存储和保护您的个人信息，以及您享有的相关权利。使用本系统即表示您同意本隐私政策的条款。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '二、信息收集',
              '2.1 我们收集的信息\n\n• 账户信息：包括用户名、密码、手机号、邮箱等注册信息\n• 个人信息：包括姓名、头像、地址等\n• 使用信息：包括登录时间、操作记录、投放记录等\n• 设备信息：包括设备型号、操作系统、IP地址等\n• 位置信息：在您授权的情况下，获取您的地理位置信息\n\n2.2 信息收集方式\n\n• 您主动提供的信息\n• 我们自动收集的信息\n• 通过第三方服务获取的信息',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '三、信息使用',
              '我们使用收集到的信息用于以下目的：\n\n• 提供和改进我们的服务\n• 验证您的身份和账户安全\n• 发送重要通知和服务消息\n• 分析使用情况，优化用户体验\n• 防止欺诈和滥用行为\n• 遵守法律法规要求',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '四、信息共享',
              '4.1 我们不会出售您的个人信息\n\n4.2 以下情况下，我们可能会共享您的信息：\n\n• 获得您的明确同意\n• 为提供服务所必需\n• 履行法律义务\n• 保护我们的权利和财产\n• 与可信的第三方服务提供商合作\n\n4.3 第三方服务\n\n本系统可能使用第三方服务（如地图、支付等），这些服务可能收集您的信息。我们建议您阅读这些第三方的隐私政策。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '五、信息存储',
              '5.1 存储地点\n\n您的个人信息将存储在中国境内的服务器上。\n\n5.2 存储期限\n\n我们将在实现本政策所述目的所需的期限内保留您的个人信息。账户注销后，我们将删除或匿名化处理您的个人信息。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '六、信息保护',
              '我们采取以下措施保护您的个人信息：\n\n• 使用加密技术保护数据传输\n• 限制员工访问个人信息\n• 定期进行安全审计\n• 建立应急响应机制\n• 遵循行业标准的安全实践',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '七、您的权利',
              '您享有以下权利：\n\n• 访问权：查看我们收集的您的个人信息\n• 更正权：要求更正不准确的个人信息\n• 删除权：要求删除您的个人信息\n• 撤回同意：撤回对信息处理的同意\n• 数据携带：要求以结构化格式获取您的信息\n• 投诉权：向监管机构投诉\n\n如需行使上述权利，请通过本政策提供的联系方式与我们联系。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '八、隐私政策的更新',
              '我们可能适时修订本隐私政策的条款，该等修订构成本隐私政策的一部分。如该等修订造成您在本隐私政策下权利的实质减少，我们将在修订生效前通过在主页上显著位置提示或向您发送电子邮件或以其他方式通知您。在该种情况下，若您继续使用我们的服务，即表示同意受经修订的本隐私政策的约束。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '九、未成年人保护',
              '本系统主要面向成年人用户。如果您是未满18周岁的未成年人，请在监护人指导下使用本系统。如发现我们收集了未成年人的个人信息，我们将及时删除。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '十、联系我们',
              '如果您对本隐私政策有任何疑问、意见或建议，通过以下方式与我们联系：\n\n• 邮箱：privacy@ecosorter.com\n• 电话：400-XXX-XXXX\n• 地址：江西省南昌市高新区XX路XX号\n\n我们将在收到您的反馈后尽快回复。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '十一、生效日期',
              '本隐私政策自发布之日起生效。最后更新日期：2026年。',
            ),
            const SizedBox(height: 32),
          ],
        ),
      ),
    );
  }

  Widget _buildSection(String title, String content) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.04),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            title,
            style: const TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.w600,
              color: AppTheme.textPrimary,
            ),
          ),
          const SizedBox(height: 12),
          Text(
            content,
            style: TextStyle(
              fontSize: 14,
              color: Colors.grey[600],
              height: 1.7,
            ),
          ),
        ],
      ),
    );
  }
}
