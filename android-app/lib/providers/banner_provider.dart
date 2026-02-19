import 'package:flutter/foundation.dart';
import 'package:ecosorter/services/api_service.dart';
import 'package:ecosorter/models/banner.dart';

class BannerProvider with ChangeNotifier {
  List<Banner> _banners = [];
  bool _isLoading = false;

  List<Banner> get banners => _banners;
  bool get isLoading => _isLoading;

  Future<void> loadBanners({String? target}) async {
    _isLoading = true;
    notifyListeners();

    try {
      String endpoint = '/api/banners';
      if (target != null) {
        endpoint += '?target=$target';
      }
      
      final data = await ApiService.get(endpoint);
      _banners = (data as List)
          .map((json) => Banner.fromJson(json as Map<String, dynamic>))
          .toList();
    } catch (e) {
      debugPrint('加载轮播图失败: $e');
      _banners = [];
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}
