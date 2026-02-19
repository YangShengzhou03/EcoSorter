import 'package:flutter/material.dart';
import 'package:ecosorter/theme/app_theme.dart';
import 'user_agreement_screen.dart';
import 'privacy_policy_screen.dart';

class AboutScreen extends StatelessWidget {
  const AboutScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.backgroundColor,
      appBar: AppBar(
        title: const Text('关于我们'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            _buildDescription(),
            const SizedBox(height: 16),
            _buildFeatures(),
            const SizedBox(height: 16),
            _buildLinks(context),
            const SizedBox(height: 24),
            _buildCopyright(),
            const SizedBox(height: 32),
          ],
        ),
      ),
    );
  }

  Widget _buildDescription() {
    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
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
          const Text(
            '关于应用',
            style: TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.w600,
              color: AppTheme.textPrimary,
            ),
          ),
          const SizedBox(height: 12),
          Text(
            'EcoSorter 是一款智能垃圾分类应用，致力于帮助用户正确分类垃圾，提高环保意识。通过扫码识别垃圾桶，记录投放行为，获取积分奖励，让垃圾分类变得更加简单有趣。',
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

  Widget _buildFeatures() {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
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
          _buildFeatureItem(
            icon: Icons.qr_code_scanner_rounded,
            title: '智能扫码',
            subtitle: '扫描垃圾桶二维码，快速识别投放',
            color: AppTheme.primaryColor,
          ),
          _buildDivider(),
          _buildFeatureItem(
            icon: Icons.location_on_rounded,
            title: '附近垃圾桶',
            subtitle: '地图查看周边垃圾桶位置',
            color: const Color(0xFF10B981),
          ),
          _buildDivider(),
          _buildFeatureItem(
            icon: Icons.monetization_on_rounded,
            title: '积分奖励',
            subtitle: '正确投放获取积分，兑换精美礼品',
            color: const Color(0xFFF59E0B),
          ),
          _buildDivider(),
          _buildFeatureItem(
            icon: Icons.history_rounded,
            title: '投放记录',
            subtitle: '记录每一次环保行为',
            color: const Color(0xFF3B82F6),
          ),
        ],
      ),
    );
  }

  Widget _buildFeatureItem({
    required IconData icon,
    required String title,
    required String subtitle,
    required Color color,
  }) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
      child: Row(
        children: [
          Container(
            width: 44,
            height: 44,
            decoration: BoxDecoration(
              color: color.withOpacity(0.1),
              borderRadius: BorderRadius.circular(12),
            ),
            child: Icon(icon, color: color, size: 22),
          ),
          const SizedBox(width: 14),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  title,
                  style: const TextStyle(
                    fontSize: 15,
                    fontWeight: FontWeight.w500,
                    color: AppTheme.textPrimary,
                  ),
                ),
                const SizedBox(height: 2),
                Text(
                  subtitle,
                  style: TextStyle(
                    fontSize: 13,
                    color: Colors.grey[500],
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildDivider() {
    return Padding(
      padding: const EdgeInsets.only(left: 74),
      child: Divider(height: 1, color: Colors.grey[100]),
    );
  }

  Widget _buildLinks(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
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
          _buildLinkItem(
            icon: Icons.description_outlined,
            title: '用户协议',
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const UserAgreementScreen()),
              );
            },
          ),
          _buildDivider(),
          _buildLinkItem(
            icon: Icons.privacy_tip_outlined,
            title: '隐私政策',
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const PrivacyPolicyScreen()),
              );
            },
          ),
          _buildDivider(),
          _buildLinkItem(
            icon: Icons.update_rounded,
            title: '检查更新',
            trailing: 'v1.0.0',
            onTap: () {
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('当前已是最新版本')),
              );
            },
          ),
        ],
      ),
    );
  }

  Widget _buildLinkItem({
    required IconData icon,
    required String title,
    String? trailing,
    required VoidCallback onTap,
  }) {
    return GestureDetector(
      onTap: onTap,
      behavior: HitTestBehavior.opaque,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
        child: Row(
          children: [
            Icon(icon, size: 20, color: Colors.grey[500]),
            const SizedBox(width: 14),
            Expanded(
              child: Text(
                title,
                style: const TextStyle(
                  fontSize: 15,
                  color: AppTheme.textPrimary,
                ),
              ),
            ),
            if (trailing != null)
              Text(
                trailing,
                style: TextStyle(
                  fontSize: 13,
                  color: Colors.grey[400],
                ),
              )
            else
              Icon(
                Icons.chevron_right_rounded,
                size: 20,
                color: Colors.grey[400],
              ),
          ],
        ),
      ),
    );
  }

  Widget _buildCopyright() {
    return Column(
      children: [
        Text(
          '© 2026 EcoSorter',
          style: TextStyle(
            fontSize: 13,
            color: Colors.grey[400],
          ),
        ),
        const SizedBox(height: 4),
        Text(
          '让垃圾分类成为一种习惯',
          style: TextStyle(
            fontSize: 12,
            color: Colors.grey[400],
          ),
        ),
      ],
    );
  }
}
