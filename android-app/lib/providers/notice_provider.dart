import 'package:flutter/foundation.dart';
import 'package:ecosorter/models/notice.dart';
import 'package:ecosorter/services/api_service.dart';

class NoticeProvider with ChangeNotifier {
  List<Notice> _notices = [];
  bool _isLoading = false;
  String? _errorMessage;

  List<Notice> get notices => _notices;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  Future<void> loadPublishedNotices() async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      final data = await ApiService.get('/api/notices/published');
      final List<dynamic> list = data is List ? data : (data['data'] ?? []);
      _notices = list.map((e) => Notice.fromJson(e)).toList();
    } catch (e) {
      _errorMessage = '加载通知失败: $e';
      debugPrint(_errorMessage);
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> loadAllNotices({int page = 1, int pageSize = 10}) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      final data = await ApiService.get('/api/notices?page=$page&pageSize=$pageSize');
      final List<dynamic> list = data['records'] ?? [];
      _notices = list.map((e) => Notice.fromJson(e)).toList();
    } catch (e) {
      _errorMessage = '加载通知失败: $e';
      debugPrint(_errorMessage);
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}
