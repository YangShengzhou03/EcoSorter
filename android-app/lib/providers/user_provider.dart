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

  List<Map<String, dynamic>> _pointRecords = [];
  int _pointRecordsPage = 1;
  bool _hasMorePointRecords = true;
  bool _isLoadingPointRecords = false;

  int get totalPoints => _totalPoints;
  int get totalCount => _totalCount;
  int get correctClassifications => _correctClassifications;
  double get totalWeight => _totalWeight;
  double get carbonSaved => _carbonSaved;
  int get accuracyRate => _accuracyRate;
  int get streakDays => _streakDays;
  bool get isLoading => _isLoading;
  List<Map<String, dynamic>> get pointRecords => _pointRecords;
  bool get hasMorePointRecords => _hasMorePointRecords;
  bool get isLoadingPointRecords => _isLoadingPointRecords;

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
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> loadPointRecords() async {
    _isLoadingPointRecords = true;
    _pointRecordsPage = 1;
    _hasMorePointRecords = true;
    notifyListeners();

    try {
      final data = await ApiService.get('/api/points/records/page?page=$_pointRecordsPage&pageSize=20');
      if (data['records'] != null && data['records'] is List) {
        _pointRecords = List<Map<String, dynamic>>.from(data['records']);
        _hasMorePointRecords = _pointRecords.length >= 20;
      } else {
        _pointRecords = [];
        _hasMorePointRecords = false;
      }
    } catch (e) {
      _pointRecords = [];
      _hasMorePointRecords = false;
    } finally {
      _isLoadingPointRecords = false;
      notifyListeners();
    }
  }

  Future<void> loadMorePointRecords() async {
    if (_isLoadingPointRecords || !_hasMorePointRecords) return;

    _isLoadingPointRecords = true;
    _pointRecordsPage++;
    notifyListeners();

    try {
      final data = await ApiService.get('/api/points/records/page?page=$_pointRecordsPage&pageSize=20');
      if (data['records'] != null && data['records'] is List) {
        final newRecords = List<Map<String, dynamic>>.from(data['records']);
        _pointRecords.addAll(newRecords);
        _hasMorePointRecords = newRecords.length >= 20;
      } else {
        _hasMorePointRecords = false;
      }
    } catch (e) {
      _hasMorePointRecords = false;
    } finally {
      _isLoadingPointRecords = false;
      notifyListeners();
    }
  }

  Future<void> refreshPointRecords() async {
    await loadPointRecords();
  }
}
