import 'package:flutter/material.dart';
import 'package:ecosorter/theme/app_theme.dart';

class HelpScreen extends StatefulWidget {
  const HelpScreen({super.key});

  @override
  State<HelpScreen> createState() => _HelpScreenState();
}

class _HelpScreenState extends State<HelpScreen> {
  final List<Map<String, dynamic>> _faqList = [
    {
      'question': '如何使用扫码功能？',
      'answer': '点击首页右上角的扫码图标或底部导航栏中间的扫码按钮，对准垃圾桶上的二维码即可识别垃圾桶信息并进行投放。',
    },
    {
      'question': '积分如何获取？',
      'answer': '每次正确投放垃圾可获得相应积分奖励。可回收物投放积分较高，厨余垃圾和其他垃圾投放也可获得基础积分。',
    },
    {
      'question': '积分可以兑换什么？',
      'answer': '积分可在积分商城兑换各类生活用品、环保周边、优惠券等礼品。进入积分商城即可查看可兑换商品。',
    },
    {
      'question': '如何查看投放记录？',
      'answer': '点击首页的"投放记录"按钮，或在"我的"页面点击"记录"即可查看您的所有投放历史记录。',
    },
    {
      'question': '垃圾桶满了怎么办？',
      'answer': '扫码时会显示垃圾桶当前容量，如果容量超过90%将无法投放。建议选择附近其他垃圾桶进行投放。',
    },
    {
      'question': '如何修改个人信息？',
      'answer': '进入"我的"页面，点击"个人信息"即可修改用户名、手机号等基本信息。',
    },
  ];

  int? _expandedIndex;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('帮助中心'),
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          _buildHeader(),
          const SizedBox(height: 20),
          _buildQuickHelp(),
          const SizedBox(height: 20),
          _buildFaqSection(),
        ],
      ),
    );
  }

  Widget _buildHeader() {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          colors: [AppTheme.primaryColor, AppTheme.primaryLight],
        ),
        borderRadius: BorderRadius.circular(AppTheme.borderRadius8),
      ),
      child: Row(
        children: [
          Container(
            width: 48,
            height: 48,
            decoration: BoxDecoration(
              color: Colors.white.withOpacity(0.2),
              borderRadius: BorderRadius.circular(AppTheme.borderRadius8),
            ),
            child: const Icon(
              Icons.help_outline_rounded,
              color: Colors.white,
              size: 24,
            ),
          ),
          const SizedBox(width: 14),
          const Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  '需要帮助？',
                  style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w600,
                    color: Colors.white,
                  ),
                ),
                SizedBox(height: 4),
                Text(
                  '查看常见问题或联系客服',
                  style: TextStyle(
                    fontSize: 13,
                    color: Colors.white70,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildQuickHelp() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          '快速入口',
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.w600,
            color: AppTheme.textPrimary,
          ),
        ),
        const SizedBox(height: 12),
        Row(
          children: [
            Expanded(
              child: _buildQuickItem(
                icon: Icons.location_on_rounded,
                label: '附近垃圾桶',
                color: AppTheme.successColor,
                onTap: () => Navigator.pushNamed(context, '/map'),
              ),
            ),
            const SizedBox(width: 12),
            Expanded(
              child: _buildQuickItem(
                icon: Icons.history_rounded,
                label: '投放记录',
                color: AppTheme.infoColor,
                onTap: () => Navigator.pushNamed(context, '/records'),
              ),
            ),
            const SizedBox(width: 12),
            Expanded(
              child: _buildQuickItem(
                icon: Icons.support_agent_rounded,
                label: '联系客服',
                color: AppTheme.warningColor,
                onTap: () {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('客服功能开发中')),
                  );
                },
              ),
            ),
          ],
        ),
      ],
    );
  }

  Widget _buildQuickItem({
    required IconData icon,
    required String label,
    required Color color,
    required VoidCallback onTap,
  }) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 16),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(8),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.04),
              blurRadius: 8,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Column(
          children: [
            Container(
              width: 40,
              height: 40,
              decoration: BoxDecoration(
                color: color.withOpacity(0.1),
                borderRadius: BorderRadius.circular(8),
              ),
              child: Icon(icon, color: color, size: 20),
            ),
            const SizedBox(height: 8),
            Text(
              label,
              style: const TextStyle(
                fontSize: 12,
                color: AppTheme.textPrimary,
                fontWeight: FontWeight.w500,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildFaqSection() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          '常见问题',
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.w600,
            color: AppTheme.textPrimary,
          ),
        ),
        const SizedBox(height: 12),
        ...List.generate(_faqList.length, (index) {
          return _buildFaqItem(index);
        }),
      ],
    );
  }

  Widget _buildFaqItem(int index) {
    final faq = _faqList[index];
    final isExpanded = _expandedIndex == index;

    return Container(
      margin: const EdgeInsets.only(bottom: 8),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8),
        border: Border.all(
          color: isExpanded ? AppTheme.primaryColor.withOpacity(0.3) : Colors.grey[100]!,
        ),
      ),
      child: Column(
        children: [
          GestureDetector(
            onTap: () {
              setState(() {
                _expandedIndex = isExpanded ? null : index;
              });
            },
            behavior: HitTestBehavior.opaque,
            child: Padding(
              padding: const EdgeInsets.all(14),
              child: Row(
                children: [
                  Container(
                    width: 28,
                    height: 28,
                    decoration: BoxDecoration(
                      color: AppTheme.primaryColor.withOpacity(0.1),
                      borderRadius: BorderRadius.circular(AppTheme.borderRadius6),
                    ),
                    child: const Center(
                      child: Text(
                        'Q',
                        style: TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.w600,
                          color: AppTheme.primaryColor,
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(width: 10),
                  Expanded(
                    child: Text(
                      faq['question'],
                      style: const TextStyle(
                        fontSize: 14,
                        fontWeight: FontWeight.w500,
                        color: AppTheme.textPrimary,
                      ),
                    ),
                  ),
                  Icon(
                    isExpanded
                        ? Icons.keyboard_arrow_up_rounded
                        : Icons.keyboard_arrow_down_rounded,
                    color: AppTheme.textTertiary,
                    size: 20,
                  ),
                ],
              ),
            ),
          ),
          if (isExpanded)
            Container(
              width: double.infinity,
              padding: const EdgeInsets.fromLTRB(52, 0, 14, 14),
              child: Text(
                faq['answer'],
                style: TextStyle(
                  fontSize: 13,
                  color: Colors.grey[600],
                  height: 1.5,
                ),
              ),
            ),
        ],
      ),
    );
  }
}
