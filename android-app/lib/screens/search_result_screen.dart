import 'package:flutter/material.dart';
import 'package:ecosorter/theme/app_theme.dart';
import 'package:ecosorter/services/api_service.dart';
import 'package:ecosorter/models/classification.dart';

class SearchResultScreen extends StatefulWidget {
  final String keyword;

  const SearchResultScreen({super.key, required this.keyword});

  @override
  State<SearchResultScreen> createState() => _SearchResultScreenState();
}

class _SearchResultScreenState extends State<SearchResultScreen> {
  final TextEditingController _searchController = TextEditingController();
  List<WasteCategory> _results = [];
  bool _isLoading = false;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    _searchController.text = widget.keyword;
    _search(widget.keyword);
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  Future<void> _search(String keyword) async {
    if (keyword.trim().isEmpty) {
      setState(() {
        _results = [];
        _isLoading = false;
      });
      return;
    }

    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      final data = await ApiService.get('/api/classification/search?keyword=${Uri.encodeComponent(keyword.trim())}');
      final List<dynamic> list = data is List ? data : (data['data'] ?? data['records'] ?? []);
      setState(() {
        _results = list.map((e) => WasteCategory.fromJson(e)).toList();
      });
    } catch (e) {
      setState(() {
        _errorMessage = '搜索失败: $e';
        _results = [];
      });
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  Color _getCategoryColor(String? name) {
    switch (name) {
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

  IconData _getCategoryIcon(String? name) {
    switch (name) {
      case '可回收物':
        return Icons.recycling;
      case '厨余垃圾':
        return Icons.restaurant_rounded;
      case '有害垃圾':
        return Icons.warning_rounded;
      case '其他垃圾':
        return Icons.delete_rounded;
      default:
        return Icons.help_outline_rounded;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: _buildSearchField(),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('取消'),
          ),
        ],
      ),
      body: _buildBody(),
    );
  }

  Widget _buildBody() {
    if (_isLoading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (_errorMessage != null) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.error_outline, size: 64, color: Colors.grey[300]),
            const SizedBox(height: 16),
            Text(_errorMessage!, style: TextStyle(color: Colors.grey[500])),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: () => _search(_searchController.text),
              child: const Text('重试'),
            ),
          ],
        ),
      );
    }

    if (_results.isEmpty) {
      return _buildEmptyState();
    }

    return ListView.builder(
      padding: const EdgeInsets.all(16),
      itemCount: _results.length,
      itemBuilder: (context, index) {
        return _buildResultItem(_results[index]);
      },
    );
  }

  Widget _buildSearchField() {
    return Container(
      height: 36,
      decoration: BoxDecoration(
        color: Colors.grey[100],
        borderRadius: BorderRadius.circular(8),
      ),
      child: TextField(
        controller: _searchController,
        autofocus: false,
        onSubmitted: _search,
        decoration: InputDecoration(
          hintText: '搜索垃圾名称',
          hintStyle: TextStyle(color: Colors.grey[400], fontSize: 14),
          border: InputBorder.none,
          focusedBorder: InputBorder.none,
          enabledBorder: InputBorder.none,
          prefixIcon: Icon(Icons.search, color: Colors.grey[400], size: 18),
          contentPadding: const EdgeInsets.symmetric(vertical: 8),
        ),
        style: const TextStyle(fontSize: 14),
      ),
    );
  }

  Widget _buildEmptyState() {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(
            Icons.search_off_rounded,
            size: 80,
            color: Colors.grey[300],
          ),
          const SizedBox(height: 16),
          Text(
            '未找到"${_searchController.text}"相关结果',
            style: TextStyle(
              fontSize: 15,
              color: Colors.grey[500],
            ),
          ),
          const SizedBox(height: 8),
          Text(
            '请尝试其他关键词',
            style: TextStyle(
              fontSize: 13,
              color: Colors.grey[400],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildResultItem(WasteCategory category) {
    final color = _getCategoryColor(category.name);
    final icon = _getCategoryIcon(category.name);

    return Container(
      margin: const EdgeInsets.only(bottom: 10),
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.03),
            blurRadius: 6,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Row(
        children: [
          Container(
            width: 48,
            height: 48,
            decoration: BoxDecoration(
              color: color.withOpacity(0.1),
              borderRadius: BorderRadius.circular(8),
            ),
            child: Icon(icon, color: color, size: 24),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    Text(
                      category.name,
                      style: const TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.w600,
                        color: AppTheme.textPrimary,
                      ),
                    ),
                    const SizedBox(width: 8),
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                      decoration: BoxDecoration(
                        color: color.withOpacity(0.1),
                        borderRadius: BorderRadius.circular(4),
                      ),
                      child: Text(
                        category.name,
                        style: TextStyle(
                          fontSize: 11,
                          color: color,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 4),
                Text(
                  category.description ?? category.disposalInstructions ?? '暂无描述',
                  style: TextStyle(
                    fontSize: 12,
                    color: Colors.grey[500],
                  ),
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                ),
              ],
            ),
          ),
          Icon(
            Icons.chevron_right_rounded,
            size: 18,
            color: Colors.grey[400],
          ),
        ],
      ),
    );
  }
}
