import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:ecosorter/theme/app_theme.dart';
import 'package:ecosorter/providers/user_provider.dart';
import 'package:intl/intl.dart';

class PointRecordsScreen extends StatefulWidget {
  const PointRecordsScreen({super.key});

  @override
  State<PointRecordsScreen> createState() => _PointRecordsScreenState();
}

class _PointRecordsScreenState extends State<PointRecordsScreen> {
  final ScrollController _scrollController = ScrollController();
  String? _selectedType;

  final List<Map<String, String>> _types = [
    {'value': '', 'label': '全部'},
    {'value': 'classification', 'label': '分类获得'},
    {'value': 'order', 'label': '订单消耗'},
    {'value': 'admin', 'label': '管理员调整'},
  ];

  @override
  void initState() {
    super.initState();
    _loadData();
    _scrollController.addListener(_onScroll);
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  void _onScroll() {
    if (_scrollController.position.pixels >=
        _scrollController.position.maxScrollExtent - 200) {
      final provider = Provider.of<UserProvider>(context, listen: false);
      provider.loadMorePointRecords();
    }
  }

  Future<void> _loadData() async {
    final provider = Provider.of<UserProvider>(context, listen: false);
    await provider.loadPointRecords();
  }

  Future<void> _refresh() async {
    final provider = Provider.of<UserProvider>(context, listen: false);
    await provider.refreshPointRecords();
  }

  String _formatDate(String dateString) {
    try {
      final date = DateTime.parse(dateString);
      return DateFormat('yyyy-MM-dd HH:mm').format(date);
    } catch (e) {
      return dateString;
    }
  }

  String _getTypeText(String? type) {
    switch (type) {
      case 'classification':
        return '分类获得';
      case 'order':
        return '订单消耗';
      case 'admin':
        return '管理员调整';
      default:
        return type ?? '未知';
    }
  }

  Color _getTypeColor(String? type) {
    switch (type) {
      case 'classification':
        return const Color(0xFF10B981);
      case 'order':
        return const Color(0xFFF59E0B);
      case 'admin':
        return const Color(0xFF6B7280);
      default:
        return AppTheme.primaryColor;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.backgroundColor,
      appBar: AppBar(
        title: const Text('积分明细'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh_rounded),
            onPressed: _refresh,
          ),
        ],
      ),
      body: Column(
        children: [
          _buildFilterBar(),
          Expanded(
            child: Consumer<UserProvider>(
              builder: (context, provider, child) {
                List<Map<String, dynamic>> filteredRecords = provider.pointRecords;
                
                if (_selectedType != null && _selectedType!.isNotEmpty) {
                  filteredRecords = provider.pointRecords
                      .where((r) => r['type'] == _selectedType)
                      .toList();
                }

                if (provider.isLoading && provider.pointRecords.isEmpty) {
                  return const Center(child: CircularProgressIndicator());
                }

                if (filteredRecords.isEmpty) {
                  return _buildEmptyState();
                }

                return RefreshIndicator(
                  onRefresh: _refresh,
                  child: ListView.builder(
                    controller: _scrollController,
                    padding: const EdgeInsets.all(16),
                    itemCount: filteredRecords.length + (provider.hasMorePointRecords ? 1 : 0),
                    itemBuilder: (context, index) {
                      if (index >= filteredRecords.length) {
                        return const Center(
                          child: Padding(
                            padding: EdgeInsets.all(16),
                            child: CircularProgressIndicator(),
                          ),
                        );
                      }

                      final record = filteredRecords[index];
                      return _buildRecordCard(record);
                    },
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildFilterBar() {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.04),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: _buildFilterDropdown(
        hint: '积分类型',
        value: _selectedType,
        items: _types,
        onChanged: (value) {
          setState(() {
            _selectedType = value?.isEmpty == true ? null : value;
          });
        },
      ),
    );
  }

  Widget _buildFilterDropdown({
    required String hint,
    required String? value,
    required List<Map<String, String>> items,
    required ValueChanged<String?> onChanged,
  }) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12),
      decoration: BoxDecoration(
        color: AppTheme.backgroundColor,
        borderRadius: BorderRadius.circular(10),
      ),
      child: DropdownButtonHideUnderline(
        child: DropdownButton<String>(
          hint: Text(
            hint,
            style: const TextStyle(
              fontSize: 14,
              color: AppTheme.textSecondary,
            ),
          ),
          value: value,
          isExpanded: true,
          icon: const Icon(Icons.keyboard_arrow_down_rounded, size: 20),
          items: items.map((item) {
            return DropdownMenuItem<String>(
              value: item['value'],
              child: Text(
                item['label']!,
                style: const TextStyle(fontSize: 14),
              ),
            );
          }).toList(),
          onChanged: onChanged,
        ),
      ),
    );
  }

  Widget _buildEmptyState() {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Container(
            width: 100,
            height: 100,
            decoration: BoxDecoration(
              color: Colors.grey[100],
              borderRadius: BorderRadius.circular(24),
            ),
            child: Icon(
              Icons.monetization_on_outlined,
              size: 48,
              color: Colors.grey[400],
            ),
          ),
          const SizedBox(height: 20),
          Text(
            '暂无积分记录',
            style: TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.w500,
              color: Colors.grey[600],
            ),
          ),
          const SizedBox(height: 8),
          Text(
            '开始垃圾分类，赚取积分兑换好礼',
            style: TextStyle(
              fontSize: 14,
              color: Colors.grey[400],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildRecordCard(Map<String, dynamic> record) {
    final points = record['points'] as int? ?? 0;
    final isPositive = points >= 0;

    return Container(
      margin: const EdgeInsets.only(bottom: 12),
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
      child: Row(
        children: [
          Container(
            width: 52,
            height: 52,
            decoration: BoxDecoration(
              color: _getTypeColor(record['type']).withOpacity(0.1),
              borderRadius: BorderRadius.circular(14),
            ),
            child: Icon(
              isPositive ? Icons.add_circle_rounded : Icons.remove_circle_rounded,
              color: _getTypeColor(record['type']),
              size: 26,
            ),
          ),
          const SizedBox(width: 14),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                      decoration: BoxDecoration(
                        color: _getTypeColor(record['type']).withOpacity(0.1),
                        borderRadius: BorderRadius.circular(6),
                      ),
                      child: Text(
                        _getTypeText(record['type']),
                        style: TextStyle(
                          fontSize: 11,
                          fontWeight: FontWeight.w600,
                          color: _getTypeColor(record['type']),
                        ),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 6),
                Text(
                  record['description'] ?? '无描述',
                  style: const TextStyle(
                    fontSize: 14,
                    color: AppTheme.textPrimary,
                  ),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
                const SizedBox(height: 4),
                Row(
                  children: [
                    Icon(
                      Icons.access_time_rounded,
                      size: 14,
                      color: Colors.grey[400],
                    ),
                    const SizedBox(width: 4),
                    Text(
                      record['createdAt'] != null ? _formatDate(record['createdAt']) : '-',
                      style: TextStyle(
                        fontSize: 12,
                        color: Colors.grey[500],
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
            decoration: BoxDecoration(
              color: isPositive ? const Color(0xFFD1FAE5) : const Color(0xFFFEE2E2),
              borderRadius: BorderRadius.circular(8),
            ),
            child: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                Icon(
                  isPositive ? Icons.add_rounded : Icons.remove_rounded,
                  size: 14,
                  color: isPositive ? const Color(0xFF059669) : const Color(0xFFDC2626),
                ),
                const SizedBox(width: 4),
                Text(
                  '${points.abs()}',
                  style: TextStyle(
                    color: isPositive ? const Color(0xFF059669) : const Color(0xFFDC2626),
                    fontWeight: FontWeight.w700,
                    fontSize: 16,
                  ),
                ),
                const Text(
                  ' 积分',
                  style: TextStyle(
                    color: AppTheme.textSecondary,
                    fontSize: 12,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
