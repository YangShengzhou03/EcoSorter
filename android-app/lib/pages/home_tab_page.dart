import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:ecosorter/theme/app_theme.dart';
import 'package:ecosorter/providers/notice_provider.dart';
import 'package:ecosorter/providers/banner_provider.dart';
import 'package:ecosorter/models/notice.dart';
import 'package:ecosorter/models/banner.dart' as app_banner;

class HomeTabPage extends StatefulWidget {
  final VoidCallback? onScanTap;

  const HomeTabPage({super.key, this.onScanTap});

  @override
  State<HomeTabPage> createState() => _HomeTabPageState();
}

class _HomeTabPageState extends State<HomeTabPage> {
  final TextEditingController _searchController = TextEditingController();

  final List<Map<String, dynamic>> _quickActions = [
    {'icon': Icons.location_on_rounded, 'label': '附近垃圾桶', 'route': '/map', 'color': const Color(0xFF10B981)},
    {'icon': Icons.history_rounded, 'label': '投放记录', 'route': '/records', 'color': const Color(0xFF3B82F6)},
    {'icon': Icons.shopping_bag_rounded, 'label': '积分商城', 'route': '/points', 'color': const Color(0xFFF59E0B)},
    {'icon': Icons.receipt_long_rounded, 'label': '我的订单', 'route': '/orders', 'color': const Color(0xFF8B5CF6)},
  ];

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    final noticeProvider = Provider.of<NoticeProvider>(context, listen: false);
    final bannerProvider = Provider.of<BannerProvider>(context, listen: false);
    
    await Future.wait([
      noticeProvider.loadPublishedNotices(),
      bannerProvider.loadBanners(target: 'user'),
    ]);
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.backgroundColor,
      body: SafeArea(
        child: RefreshIndicator(
          onRefresh: _loadData,
          child: SingleChildScrollView(
            physics: const AlwaysScrollableScrollPhysics(),
            child: Column(
              children: [
                const SizedBox(height: 16),
                _buildSearchBar(),
                const SizedBox(height: 12),
                _buildBannerCarousel(),
                const SizedBox(height: 16),
                _buildQuickActions(),
                const SizedBox(height: 16),
                _buildNotificationCard(),
                const SizedBox(height: 80),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildSearchBar() {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 16),
      height: 48,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.05),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Row(
        children: [
          const SizedBox(width: 16),
          Icon(Icons.search, color: Colors.grey[400], size: 22),
          const SizedBox(width: 12),
          Expanded(
            child: TextField(
              controller: _searchController,
              cursorColor: AppTheme.primaryColor,
              onSubmitted: (value) {
                if (value.trim().isNotEmpty) {
                  Navigator.pushNamed(context, '/search', arguments: value.trim());
                }
              },
              decoration: InputDecoration(
                hintText: '搜索垃圾名称，如"塑料瓶"',
                hintStyle: TextStyle(color: Colors.grey[400], fontSize: 14),
                border: InputBorder.none,
                focusedBorder: InputBorder.none,
                enabledBorder: InputBorder.none,
                contentPadding: const EdgeInsets.symmetric(vertical: 14),
                isDense: true,
              ),
              style: const TextStyle(fontSize: 14),
            ),
          ),
          GestureDetector(
            onTap: widget.onScanTap,
            child: Container(
              padding: const EdgeInsets.all(8),
              child: Icon(Icons.qr_code_scanner_rounded, color: Colors.grey[500], size: 22),
            ),
          ),
          const SizedBox(width: 8),
        ],
      ),
    );
  }

  Widget _buildBannerCarousel() {
    return Consumer<BannerProvider>(
      builder: (context, bannerProvider, child) {
        final banners = bannerProvider.banners;
        
        if (banners.isEmpty) {
          return Container(
            margin: const EdgeInsets.symmetric(horizontal: 16),
            height: 160,
            decoration: BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
                colors: [AppTheme.primaryColor, AppTheme.primaryColor.withOpacity(0.8)],
              ),
              borderRadius: BorderRadius.circular(16),
            ),
            child: const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(Icons.recycling_rounded, color: Colors.white, size: 48),
                  SizedBox(height: 12),
                  Text(
                    '欢迎使用智能垃圾分类系统',
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 18,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ],
              ),
            ),
          );
        }

        return Container(
          margin: const EdgeInsets.symmetric(horizontal: 16),
          height: 160,
          child: ClipRRect(
            borderRadius: BorderRadius.circular(16),
            child: PageView.builder(
              itemCount: banners.length,
              itemBuilder: (context, index) {
                final banner = banners[index];
                return _buildBannerItem(banner);
              },
            ),
          ),
        );
      },
    );
  }

  Widget _buildBannerItem(app_banner.Banner banner) {
    Color bgColor = AppTheme.primaryColor;
    if (banner.background != null && banner.background!.startsWith('#')) {
      try {
        bgColor = Color(int.parse(banner.background!.replaceFirst('#', '0xFF')));
      } catch (_) {}
    }

    return Container(
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [bgColor, bgColor.withOpacity(0.8)],
        ),
      ),
      padding: const EdgeInsets.all(20),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            banner.title,
            style: const TextStyle(
              color: Colors.white,
              fontSize: 22,
              fontWeight: FontWeight.w700,
              shadows: [
                Shadow(
                  color: Colors.black26,
                  blurRadius: 4,
                  offset: Offset(0, 2),
                ),
              ],
            ),
          ),
          if (banner.description != null && banner.description!.isNotEmpty) ...[
            const SizedBox(height: 8),
            Text(
              banner.description!,
              style: TextStyle(
                color: Colors.white.withOpacity(0.9),
                fontSize: 14,
              ),
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
            ),
          ],
        ],
      ),
    );
  }

  Widget _buildQuickActions() {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Container(
        padding: const EdgeInsets.all(16),
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
              '常用功能',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.w600,
                color: AppTheme.textPrimary,
              ),
            ),
            const SizedBox(height: 16),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: _quickActions.map((action) => _buildQuickActionItem(action)).toList(),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildQuickActionItem(Map<String, dynamic> action) {
    return GestureDetector(
      onTap: () => Navigator.pushNamed(context, action['route']),
      child: Column(
        children: [
          Container(
            width: 52,
            height: 52,
            decoration: BoxDecoration(
              color: (action['color'] as Color).withOpacity(0.1),
              borderRadius: BorderRadius.circular(14),
            ),
            child: Icon(action['icon'], color: action['color'], size: 24),
          ),
          const SizedBox(height: 8),
          Text(
            action['label'],
            style: const TextStyle(
              fontSize: 12,
              color: AppTheme.textPrimary,
              fontWeight: FontWeight.w500,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildNotificationCard() {
    return Consumer<NoticeProvider>(
      builder: (context, noticeProvider, child) {
        final notices = noticeProvider.notices.take(3).toList();
        
        return Container(
          margin: const EdgeInsets.symmetric(horizontal: 16),
          padding: const EdgeInsets.all(16),
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
              Row(
                children: [
                  Container(
                    width: 36,
                    height: 36,
                    decoration: BoxDecoration(
                      color: AppTheme.primaryColor.withOpacity(0.1),
                      borderRadius: BorderRadius.circular(10),
                    ),
                    child: const Icon(Icons.notifications_outlined, color: AppTheme.primaryColor, size: 18),
                  ),
                  const SizedBox(width: 12),
                  const Text(
                    '通知公告',
                    style: TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.w600,
                      color: AppTheme.textPrimary,
                    ),
                  ),
                  const Spacer(),
                  GestureDetector(
                    onTap: () => Navigator.pushNamed(context, '/notification'),
                    child: Row(
                      children: [
                        Text(
                          '查看全部',
                          style: TextStyle(fontSize: 13, color: Colors.grey[500]),
                        ),
                        Icon(Icons.chevron_right, color: Colors.grey[400], size: 18),
                      ],
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 12),
              if (notices.isEmpty)
                Padding(
                  padding: const EdgeInsets.symmetric(vertical: 20),
                  child: Center(
                    child: Text('暂无通知', style: TextStyle(fontSize: 14, color: Colors.grey[400])),
                  ),
                )
              else
                ...notices.map((notice) => _buildNotificationItem(notice)),
            ],
          ),
        );
      },
    );
  }

  Widget _buildNotificationItem(Notice notice) {
    return Container(
      margin: const EdgeInsets.only(bottom: 8),
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: AppTheme.backgroundColor,
        borderRadius: BorderRadius.circular(10),
      ),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: 6,
            height: 6,
            margin: const EdgeInsets.only(top: 6),
            decoration: BoxDecoration(
              color: AppTheme.primaryColor,
              borderRadius: BorderRadius.circular(3),
            ),
          ),
          const SizedBox(width: 10),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  notice.title,
                  style: const TextStyle(
                    fontSize: 14,
                    color: AppTheme.textPrimary,
                    fontWeight: FontWeight.w500,
                  ),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
                const SizedBox(height: 4),
                Text(
                  notice.content,
                  style: const TextStyle(
                    fontSize: 12,
                    color: AppTheme.textSecondary,
                    height: 1.4,
                  ),
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
