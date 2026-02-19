import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:ecosorter/theme/app_theme.dart';
import 'package:ecosorter/providers/classification_provider.dart';
import 'package:ecosorter/models/classification.dart';
import 'package:intl/intl.dart';

class RecordsScreen extends StatefulWidget {
  const RecordsScreen({super.key});

  @override
  State<RecordsScreen> createState() => _RecordsScreenState();
}

class _RecordsScreenState extends State<RecordsScreen> {
  final ScrollController _scrollController = ScrollController();
  String? _selectedCategory;
  String? _selectedStatus;

  final List<Map<String, String>> _categories = [
    {'value': '', 'label': '全部'},
    {'value': '可回收物', 'label': '可回收物'},
    {'value': '厨余垃圾', 'label': '厨余垃圾'},
    {'value': '有害垃圾', 'label': '有害垃圾'},
    {'value': '其他垃圾', 'label': '其他垃圾'},
  ];

  final List<Map<String, String>> _statuses = [
    {'value': '', 'label': '全部'},
    {'value': 'completed', 'label': '完成'},
    {'value': 'pending', 'label': '待审核'},
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
      final provider = Provider.of<ClassificationProvider>(context, listen: false);
      provider.loadMore();
    }
  }

  Future<void> _loadData() async {
    final provider = Provider.of<ClassificationProvider>(context, listen: false);
    await provider.loadHistory();
    await provider.loadCategories();
  }

  Future<void> _refresh() async {
    final provider = Provider.of<ClassificationProvider>(context, listen: false);
    await provider.refresh();
  }

  String _formatDate(String dateString) {
    try {
      final date = DateTime.parse(dateString);
      return DateFormat('yyyy-MM-dd HH:mm').format(date);
    } catch (e) {
      return dateString;
    }
  }

  Color _getCategoryColor(String? category) {
    switch (category) {
      case '可回收物':
        return const Color(0xFF10B981);
      case '厨余垃圾':
        return const Color(0xFFF59E0B);
      case '有害垃圾':
        return const Color(0xFFEF4444);
      case '其他垃圾':
        return const Color(0xFF6B7280);
      default:
        return AppTheme.primaryColor;
    }
  }

  Color _getCategoryBgColor(String? category) {
    switch (category) {
      case '可回收物':
        return const Color(0xFFD1FAE5);
      case '厨余垃圾':
        return const Color(0xFFFEF3C7);
      case '有害垃圾':
        return const Color(0xFFFEE2E2);
      case '其他垃圾':
        return const Color(0xFFE5E7EB);
      default:
        return AppTheme.primaryColor.withOpacity(0.1);
    }
  }

  String _getStatusText(String? status) {
    switch (status) {
      case 'completed':
        return '完成';
      case 'pending':
        return '待审核';
      default:
        return status ?? '未知';
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.backgroundColor,
      appBar: AppBar(
        title: const Text('投放记录'),
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
            child: Consumer<ClassificationProvider>(
              builder: (context, provider, child) {
                List<Classification> filteredRecords = provider.records;
                
                if (_selectedCategory != null && _selectedCategory!.isNotEmpty) {
                  filteredRecords = provider.records
                      .where((r) => r.categoryName == _selectedCategory)
                      .toList();
                }
                
                if (_selectedStatus != null && _selectedStatus!.isNotEmpty) {
                  filteredRecords = filteredRecords
                      .where((r) => r.status == _selectedStatus)
                      .toList();
                }

                if (provider.isLoading && provider.records.isEmpty) {
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
                    itemCount: filteredRecords.length + (provider.hasMore ? 1 : 0),
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
      child: Row(
        children: [
          Expanded(
            child: _buildFilterDropdown(
              hint: '垃圾分类',
              value: _selectedCategory,
              items: _categories,
              onChanged: (value) {
                setState(() {
                  _selectedCategory = value?.isEmpty == true ? null : value;
                });
              },
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: _buildFilterDropdown(
              hint: '状态',
              value: _selectedStatus,
              items: _statuses,
              onChanged: (value) {
                setState(() {
                  _selectedStatus = value?.isEmpty == true ? null : value;
                });
              },
            ),
          ),
        ],
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
              Icons.inbox_outlined,
              size: 48,
              color: Colors.grey[400],
            ),
          ),
          const SizedBox(height: 20),
          Text(
            '暂无投放记录',
            style: TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.w500,
              color: Colors.grey[600],
            ),
          ),
          const SizedBox(height: 8),
          Text(
            '开始垃圾分类，记录您的环保足迹',
            style: TextStyle(
              fontSize: 14,
              color: Colors.grey[400],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildRecordCard(Classification record) {
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
              color: _getCategoryBgColor(record.categoryName),
              borderRadius: BorderRadius.circular(14),
            ),
            child: Icon(
              Icons.recycling_rounded,
              color: _getCategoryColor(record.categoryName),
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
                    Text(
                      record.categoryName ?? '未知分类',
                      style: const TextStyle(
                        fontWeight: FontWeight.w600,
                        fontSize: 15,
                        color: AppTheme.textPrimary,
                      ),
                    ),
                    const SizedBox(width: 8),
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                      decoration: BoxDecoration(
                        color: _getStatusText(record.status) == '完成'
                            ? const Color(0xFFD1FAE5)
                            : const Color(0xFFFEF3C7),
                        borderRadius: BorderRadius.circular(6),
                      ),
                      child: Text(
                        _getStatusText(record.status),
                        style: TextStyle(
                          fontSize: 11,
                          fontWeight: FontWeight.w600,
                          color: _getStatusText(record.status) == '完成'
                              ? const Color(0xFF059669)
                              : const Color(0xFFD97706),
                        ),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 6),
                Row(
                  children: [
                    Icon(
                      Icons.access_time_rounded,
                      size: 14,
                      color: Colors.grey[400],
                    ),
                    const SizedBox(width: 4),
                    Text(
                      record.createdAt != null ? _formatDate(record.createdAt!) : '-',
                      style: TextStyle(
                        fontSize: 12,
                        color: Colors.grey[500],
                      ),
                    ),
                    if (record.confidence != null) ...[
                      const SizedBox(width: 12),
                      Icon(
                        Icons.verified_rounded,
                        size: 14,
                        color: Colors.grey[400],
                      ),
                      const SizedBox(width: 4),
                      Text(
                        '置信度 ${(record.confidence! * 100).toStringAsFixed(0)}%',
                        style: TextStyle(
                          fontSize: 12,
                          color: Colors.grey[500],
                        ),
                      ),
                    ],
                  ],
                ),
              ],
            ),
          ),
          if (record.points != null && record.points! > 0)
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
              decoration: BoxDecoration(
                color: const Color(0xFFD1FAE5),
                borderRadius: BorderRadius.circular(8),
              ),
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  const Icon(
                    Icons.add_rounded,
                    size: 14,
                    color: Color(0xFF059669),
                  ),
                  Text(
                    '${record.points}',
                    style: const TextStyle(
                      color: Color(0xFF059669),
                      fontWeight: FontWeight.w700,
                      fontSize: 14,
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
