import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:ecosorter/theme/app_theme.dart';
import 'package:ecosorter/providers/auth_provider.dart';
import 'package:ecosorter/providers/user_provider.dart';
import 'package:ecosorter/providers/product_provider.dart';
import 'package:ecosorter/providers/classification_provider.dart';

class ProfileTabPage extends StatefulWidget {
  const ProfileTabPage({super.key});

  @override
  State<ProfileTabPage> createState() => _ProfileTabPageState();
}

class _ProfileTabPageState extends State<ProfileTabPage> {
  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    final userProvider = Provider.of<UserProvider>(context, listen: false);
    final productProvider = Provider.of<ProductProvider>(context, listen: false);
    final classificationProvider = Provider.of<ClassificationProvider>(context, listen: false);
    
    await Future.wait([
      userProvider.loadStatistics(),
      productProvider.loadOrders(),
      classificationProvider.loadHistory(),
    ]);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.backgroundColor,
      body: SafeArea(
        child: Consumer3<AuthProvider, UserProvider, ProductProvider>(
          builder: (context, authProvider, userProvider, productProvider, child) {
            return ListView(
              padding: const EdgeInsets.all(16),
              children: [
                _buildUserInfo(authProvider),
                const SizedBox(height: 16),
                _buildStatistics(userProvider, productProvider),
                const SizedBox(height: 16),
                _buildMenuList(),
                const SizedBox(height: 80),
              ],
            );
          },
        ),
      ),
    );
  }

  Widget _buildUserInfo(AuthProvider authProvider) {
    final user = authProvider.user;
    final username = user?.username ?? '居民用户';
    final avatarUrl = user?.avatarUrl;
    final initial = username.isNotEmpty ? username[0].toUpperCase() : 'U';

    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [AppTheme.primaryColor, AppTheme.primaryColor.withOpacity(0.85)],
        ),
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: AppTheme.primaryColor.withOpacity(0.3),
            blurRadius: 12,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Row(
        children: [
          GestureDetector(
            onTap: () => Navigator.pushNamed(context, '/profile-edit'),
            child: Container(
              width: 64,
              height: 64,
              decoration: BoxDecoration(
                color: Colors.white.withOpacity(0.2),
                borderRadius: BorderRadius.circular(16),
                border: Border.all(color: Colors.white.withOpacity(0.3), width: 2),
              ),
              child: avatarUrl != null && avatarUrl.isNotEmpty
                  ? ClipRRect(
                      borderRadius: BorderRadius.circular(14),
                      child: Image.network(
                        avatarUrl,
                        fit: BoxFit.cover,
                        errorBuilder: (context, error, stackTrace) {
                          return Center(
                            child: Text(
                              initial,
                              style: const TextStyle(
                                fontSize: 28,
                                color: Colors.white,
                                fontWeight: FontWeight.w600,
                              ),
                            ),
                          );
                        },
                      ),
                    )
                  : Center(
                      child: Text(
                        initial,
                        style: const TextStyle(
                          fontSize: 28,
                          color: Colors.white,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                    ),
            ),
          ),
          const SizedBox(width: 16),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  username,
                  style: const TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.w600,
                    color: Colors.white,
                  ),
                ),
                const SizedBox(height: 6),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                  decoration: BoxDecoration(
                    color: Colors.white.withOpacity(0.2),
                    borderRadius: BorderRadius.circular(6),
                  ),
                  child: const Text(
                    '居民用户',
                    style: TextStyle(
                      fontSize: 12,
                      color: Colors.white,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ),
              ],
            ),
          ),
          GestureDetector(
            onTap: () => Navigator.pushNamed(context, '/settings'),
            child: Container(
              padding: const EdgeInsets.all(10),
              decoration: BoxDecoration(
                color: Colors.white.withOpacity(0.2),
                borderRadius: BorderRadius.circular(12),
              ),
              child: const Icon(Icons.settings_rounded, size: 22, color: Colors.white),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildStatistics(UserProvider userProvider, ProductProvider productProvider) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 20),
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
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: [
          _buildStatItem(
            '${userProvider.totalPoints}',
            '积分',
            () => Navigator.pushNamed(context, '/points'),
          ),
          _buildStatDivider(),
          _buildStatItem(
            '${productProvider.orders.length}',
            '订单',
            () => Navigator.pushNamed(context, '/orders'),
          ),
          _buildStatDivider(),
          _buildStatItem(
            '${userProvider.totalCount}',
            '记录',
            () => Navigator.pushNamed(context, '/records'),
          ),
          _buildStatDivider(),
          _buildStatItem(
            '${userProvider.streakDays}',
            '连续',
            null,
          ),
        ],
      ),
    );
  }

  Widget _buildStatDivider() {
    return Container(
      height: 40,
      width: 1,
      color: Colors.grey[100],
    );
  }

  Widget _buildStatItem(String count, String label, VoidCallback? onTap) {
    return GestureDetector(
      onTap: onTap ?? () {},
      behavior: HitTestBehavior.opaque,
      child: Column(
        children: [
          Text(
            count,
            style: const TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.w700,
              color: AppTheme.textPrimary,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            label,
            style: const TextStyle(
              fontSize: 13,
              color: AppTheme.textSecondary,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildMenuList() {
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
          _buildMenuItem(
            Icons.location_on_rounded,
            '附近垃圾桶',
            '查找附近垃圾分类点',
            const Color(0xFF10B981),
            () => Navigator.pushNamed(context, '/map'),
          ),
          _buildDivider(),
          _buildMenuItem(
            Icons.qr_code_scanner_rounded,
            '扫码投放',
            '扫描垃圾桶二维码',
            AppTheme.primaryColor,
            () {
              Navigator.pop(context);
            },
          ),
          _buildDivider(),
          _buildMenuItem(
            Icons.shopping_bag_rounded,
            '积分商城',
            '使用积分兑换商品',
            const Color(0xFFF59E0B),
            () => Navigator.pushNamed(context, '/points'),
          ),
          _buildDivider(),
          _buildMenuItem(
            Icons.receipt_long_rounded,
            '我的订单',
            '查看兑换记录',
            const Color(0xFF8B5CF6),
            () => Navigator.pushNamed(context, '/orders'),
          ),
          _buildDivider(),
          _buildMenuItem(
            Icons.history_rounded,
            '投放记录',
            '查看历史投放',
            const Color(0xFF3B82F6),
            () => Navigator.pushNamed(context, '/records'),
          ),
        ],
      ),
    );
  }

  Widget _buildMenuItem(IconData icon, String title, String subtitle, Color color, VoidCallback onTap) {
    return GestureDetector(
      onTap: onTap,
      behavior: HitTestBehavior.opaque,
      child: Padding(
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
                    style: const TextStyle(
                      fontSize: 12,
                      color: AppTheme.textSecondary,
                    ),
                  ),
                ],
              ),
            ),
            const Icon(Icons.chevron_right_rounded, size: 20, color: AppTheme.textTertiary),
          ],
        ),
      ),
    );
  }

  Widget _buildDivider() {
    return Padding(
      padding: const EdgeInsets.only(left: 74),
      child: Divider(height: 1, color: Colors.grey[100]),
    );
  }
}
