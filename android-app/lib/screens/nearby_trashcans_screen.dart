import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:ecosorter/theme/app_theme.dart';
import 'package:ecosorter/providers/trashcan_provider.dart';
import 'package:ecosorter/models/trashcan.dart';

class NearbyTrashcansScreen extends StatefulWidget {
  const NearbyTrashcansScreen({super.key});

  @override
  State<NearbyTrashcansScreen> createState() => _NearbyTrashcansScreenState();
}

class _NearbyTrashcansScreenState extends State<NearbyTrashcansScreen> {
  String? _selectedType;

  final List<Map<String, String>> _types = [
    {'value': '', 'label': '全部'},
    {'value': 'recyclable', 'label': '可回收物'},
    {'value': 'kitchen', 'label': '厨余垃圾'},
    {'value': 'hazardous', 'label': '有害垃圾'},
    {'value': 'other', 'label': '其他垃圾'},
  ];

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    final provider = Provider.of<TrashcanProvider>(context, listen: false);
    await provider.loadTrashcans();
  }

  Future<void> _refresh() async {
    await _loadData();
  }

  Color _getStatusColor(String? status) {
    switch (status) {
      case 'online':
        return const Color(0xFF10B981);
      case 'offline':
        return const Color(0xFF6B7280);
      case 'maintenance':
        return const Color(0xFFF59E0B);
      case 'error':
        return const Color(0xFFEF4444);
      default:
        return const Color(0xFF6B7280);
    }
  }

  String _getStatusText(String? status) {
    switch (status) {
      case 'online':
        return '在线';
      case 'offline':
        return '离线';
      case 'maintenance':
        return '维护中';
      case 'error':
        return '故障';
      default:
        return '未知';
    }
  }

  IconData _getBinTypeIcon(String? binType) {
    switch (binType?.toLowerCase()) {
      case 'recyclable':
        return Icons.recycling;
      case 'kitchen':
        return Icons.restaurant_rounded;
      case 'hazardous':
        return Icons.warning_rounded;
      case 'other':
        return Icons.delete_rounded;
      default:
        return Icons.delete_outline_rounded;
    }
  }

  Color _getBinTypeColor(String? binType) {
    switch (binType?.toLowerCase()) {
      case 'recyclable':
        return const Color(0xFF10B981);
      case 'kitchen':
        return const Color(0xFFF59E0B);
      case 'hazardous':
        return const Color(0xFFEF4444);
      case 'other':
        return const Color(0xFF6B7280);
      default:
        return const Color(0xFF3B82F6);
    }
  }

  String _getBinTypeName(String? binType) {
    switch (binType?.toLowerCase()) {
      case 'recyclable':
        return '可回收物';
      case 'kitchen':
        return '厨余垃圾';
      case 'hazardous':
        return '有害垃圾';
      case 'other':
        return '其他垃圾';
      default:
        return binType ?? '未知';
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.backgroundColor,
      appBar: AppBar(
        title: const Text('附近垃圾桶'),
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
            child: Consumer<TrashcanProvider>(
              builder: (context, provider, child) {
                List<Trashcan> filteredTrashcans = provider.trashcans;

                if (_selectedType != null && _selectedType!.isNotEmpty) {
                  filteredTrashcans = provider.trashcans
                      .where((t) => t.binType?.toLowerCase() == _selectedType)
                      .toList();
                }

                if (provider.isLoading && provider.trashcans.isEmpty) {
                  return const Center(child: CircularProgressIndicator());
                }

                if (filteredTrashcans.isEmpty) {
                  return _buildEmptyState();
                }

                return RefreshIndicator(
                  onRefresh: _refresh,
                  child: ListView.builder(
                    padding: const EdgeInsets.all(16),
                    itemCount: filteredTrashcans.length,
                    itemBuilder: (context, index) {
                      final trashcan = filteredTrashcans[index];
                      return _buildTrashcanCard(trashcan);
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
        hint: '垃圾桶类型',
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
              Icons.location_off_rounded,
              size: 48,
              color: Colors.grey[400],
            ),
          ),
          const SizedBox(height: 20),
          Text(
            '暂无垃圾桶',
            style: TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.w500,
              color: Colors.grey[600],
            ),
          ),
          const SizedBox(height: 8),
          Text(
            '附近暂无垃圾桶信息',
            style: TextStyle(
              fontSize: 14,
              color: Colors.grey[400],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildTrashcanCard(Trashcan trashcan) {
    final statusColor = _getStatusColor(trashcan.status);
    final binTypeColor = _getBinTypeColor(trashcan.binType);

    return Container(
      margin: const EdgeInsets.only(bottom: 12),
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
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
            decoration: BoxDecoration(
              color: AppTheme.backgroundColor,
              borderRadius: const BorderRadius.vertical(top: Radius.circular(16)),
            ),
            child: Row(
              children: [
                Container(
                  width: 40,
                  height: 40,
                  decoration: BoxDecoration(
                    color: binTypeColor.withOpacity(0.1),
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: Icon(
                    _getBinTypeIcon(trashcan.binType),
                    color: binTypeColor,
                    size: 20,
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        trashcan.deviceName ?? '垃圾桶 #${trashcan.id}',
                        style: const TextStyle(
                          fontSize: 15,
                          fontWeight: FontWeight.w600,
                          color: AppTheme.textPrimary,
                        ),
                      ),
                      if (trashcan.location != null)
                        Text(
                          trashcan.location!,
                          style: TextStyle(
                            fontSize: 12,
                            color: Colors.grey[500],
                          ),
                        ),
                    ],
                  ),
                ),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                  decoration: BoxDecoration(
                    color: statusColor.withOpacity(0.1),
                    borderRadius: BorderRadius.circular(6),
                  ),
                  child: Row(
                    children: [
                      Container(
                        width: 6,
                        height: 6,
                        decoration: BoxDecoration(
                          color: statusColor,
                          shape: BoxShape.circle,
                        ),
                      ),
                      const SizedBox(width: 6),
                      Text(
                        _getStatusText(trashcan.status),
                        style: TextStyle(
                          fontSize: 12,
                          color: statusColor,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(16),
            child: Column(
              children: [
                Row(
                  children: [
                    _buildInfoItem(
                      Icons.category_rounded,
                      '类型',
                      _getBinTypeName(trashcan.binType),
                      binTypeColor,
                    ),
                    const SizedBox(width: 16),
                    _buildInfoItem(
                      Icons.memory_rounded,
                      '设备ID',
                      trashcan.deviceId ?? 'N/A',
                      Colors.grey,
                    ),
                  ],
                ),
                const SizedBox(height: 12),
                Row(
                  children: [
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Row(
                            children: [
                              Icon(
                                Icons.storage_rounded,
                                size: 16,
                                color: Colors.grey[500],
                              ),
                              const SizedBox(width: 6),
                              Text(
                                '容量',
                                style: TextStyle(
                                  fontSize: 13,
                                  color: Colors.grey[600],
                                ),
                              ),
                              const Spacer(),
                              Text(
                                '${trashcan.capacityPercent}%',
                                style: TextStyle(
                                  fontSize: 14,
                                  fontWeight: FontWeight.w600,
                                  color: trashcan.capacityPercent > 80
                                      ? const Color(0xFFEF4444)
                                      : const Color(0xFF10B981),
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(height: 6),
                          ClipRRect(
                            borderRadius: BorderRadius.circular(4),
                            child: LinearProgressIndicator(
                              value: trashcan.capacityPercent / 100,
                              backgroundColor: Colors.grey[200],
                              valueColor: AlwaysStoppedAnimation<Color>(
                                trashcan.capacityPercent > 80
                                    ? const Color(0xFFEF4444)
                                    : const Color(0xFF10B981),
                              ),
                              minHeight: 6,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
                if (trashcan.lastUpdate != null) ...[
                  const SizedBox(height: 12),
                  Row(
                    children: [
                      Icon(
                        Icons.access_time_rounded,
                        size: 14,
                        color: Colors.grey[400],
                      ),
                      const SizedBox(width: 6),
                      Text(
                        '最后更新: ${trashcan.lastUpdate}',
                        style: TextStyle(
                          fontSize: 12,
                          color: Colors.grey[500],
                        ),
                      ),
                    ],
                  ),
                ],
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildInfoItem(IconData icon, String label, String value, Color color) {
    return Expanded(
      child: Row(
        children: [
          Icon(icon, size: 16, color: color),
          const SizedBox(width: 6),
          Text(
            '$label: ',
            style: TextStyle(
              fontSize: 12,
              color: Colors.grey[600],
            ),
          ),
          Expanded(
            child: Text(
              value,
              style: TextStyle(
                fontSize: 12,
                color: color,
                fontWeight: FontWeight.w500,
              ),
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ],
      ),
    );
  }
}
