import 'package:flutter/foundation.dart';
import 'package:ecosorter/models/classification.dart';
import 'package:ecosorter/services/api_service.dart';

class ClassificationProvider with ChangeNotifier {
  List<Classification> _records = [];
  List<WasteCategory> _categories = [];
  bool _isLoading = false;
  int _currentPage = 1;
  int _pageSize = 10;
  int _total = 0;

  List<Classification> get records => _records;
  List<WasteCategory> get categories => _categories;
  bool get isLoading => _isLoading;
  int get currentPage => _currentPage;
  int get pageSize => _pageSize;
  int get total => _total;
  bool get hasMore => _records.length < _total;

  Future<void> loadCategories() async {
    try {
      final data = await ApiService.get('/api/classification/categories');
      final List<dynamic> list = data is List ? data : (data['data'] ?? data['categories'] ?? []);
      _categories = list.map((e) => WasteCategory.fromJson(e)).toList();
      notifyListeners();
    } catch (e) {
      debugPrint('加载分类失败: $e');
    }
  }

  Future<void> loadHistory({int page = 1, int pageSize = 10}) async {
    _isLoading = true;
    _currentPage = page;
    _pageSize = pageSize;
    notifyListeners();

    try {
      final data = await ApiService.get(
        '/api/classification/history?page=$page&pageSize=$pageSize',
      );
      final classificationResponse = ClassificationResponse.fromJson(data);
      
      if (page == 1) {
        _records = classificationResponse.records;
      } else {
        _records.addAll(classificationResponse.records);
      }
      _total = classificationResponse.total;
    } catch (e) {
      debugPrint('加载记录失败: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> loadMore() async {
    if (!hasMore || _isLoading) return;
    await loadHistory(page: _currentPage + 1, pageSize: _pageSize);
  }

  Future<void> refresh() async {
    await loadHistory(page: 1, pageSize: _pageSize);
  }
}
