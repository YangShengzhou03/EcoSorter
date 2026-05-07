import 'package:flutter/foundation.dart';
import 'package:ecosorter/models/trashcan.dart';
import 'package:ecosorter/services/api_service.dart';

class TrashcanProvider with ChangeNotifier {
  List<Trashcan> _trashcans = [];
  bool _isLoading = false;
  String? _errorMessage;

  List<Trashcan> get trashcans => _trashcans;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  Future<void> loadTrashcans() async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      final data = await ApiService.get('/api/trashcans/nearby');
      final List<dynamic> list = data is List ? data : (data['data'] ?? data['records'] ?? []);
      _trashcans = list.map((e) => Trashcan.fromJson(e)).toList();
    } catch (e) {
      _errorMessage = '加载垃圾桶失败: $e';
      _trashcans = [];
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<Trashcan?> getTrashcanByDeviceId(String deviceId) async {
    try {
      final data = await ApiService.get('/api/trashcans/device/$deviceId');
      return Trashcan.fromJson(data);
    } catch (e) {
      return null;
    }
  }

  Future<Trashcan?> getTrashcanById(int id) async {
    try {
      final data = await ApiService.get('/api/trashcans/$id');
      return Trashcan.fromJson(data);
    } catch (e) {
      return null;
    }
  }
}
