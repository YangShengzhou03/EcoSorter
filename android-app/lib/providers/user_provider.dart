import 'package:flutter/foundation.dart';
import 'package:ecosorter/services/api_service.dart';

class UserProvider with ChangeNotifier {
  int _totalPoints = 0;
  int _totalCount = 0;
  int _correctClassifications = 0;
  double _totalWeight = 0;
  double _carbonSaved = 0;
  int _accuracyRate = 0;
  int _streakDays = 0;
  bool _isLoading = false;

  int get totalPoints => _totalPoints;
  int get totalCount => _totalCount;
  int get correctClassifications => _correctClassifications;
  double get totalWeight => _totalWeight;
  double get carbonSaved => _carbonSaved;
  int get accuracyRate => _accuracyRate;
  int get streakDays => _streakDays;
  bool get isLoading => _isLoading;

  Future<void> loadStatistics() async {
    _isLoading = true;
    notifyListeners();

    try {
      final data = await ApiService.get('/api/user/statistics');
      _totalPoints = (data['totalPoints'] ?? 0).toInt();
      _totalCount = (data['totalCount'] ?? 0).toInt();
      _correctClassifications = data['correctClassifications'] ?? 0;
      _totalWeight = (data['totalWeight'] ?? 0).toDouble();
      _carbonSaved = (data['carbonSaved'] ?? 0).toDouble();
      _accuracyRate = data['accuracyRate'] ?? 0;
      _streakDays = data['streakDays'] ?? 0;
    } catch (e) {
      debugPrint('加载统计数据失败: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}
