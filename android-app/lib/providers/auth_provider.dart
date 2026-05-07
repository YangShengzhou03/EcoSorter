import 'package:flutter/foundation.dart';
import 'package:ecosorter/models/user.dart';
import 'package:ecosorter/services/api_service.dart';

class AuthProvider with ChangeNotifier {
  User? _user;
  bool _isLoading = false;
  String? _errorMessage;

  User? get user => _user;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;
  bool get isAuthenticated => _user != null;

  Future<bool> login(String email, String password) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      final data = await ApiService.post(
        '/api/auth/login',
        body: LoginRequest(email: email, password: password).toJson(),
      );

      final authResponse = AuthResponse.fromJson(data);
      await ApiService.saveToken(authResponse.accessToken);
      _user = authResponse.user;
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _errorMessage = '网络错误: $e';
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  Future<bool> scanQRCode(String qrCode) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      final data = await ApiService.post(
        '/api/qr-login/scan',
        body: {
          'qrCode': qrCode,
          'userId': _user?.id,
        },
      );

      if (data['status'] == 'scanned') {
        _isLoading = false;
        notifyListeners();
        return true;
      } else if (data['status'] == 'expired') {
        _errorMessage = data['message'] ?? '二维码已过期';
        _isLoading = false;
        notifyListeners();
        return false;
      } else {
        _errorMessage = data['message'] ?? '二维码无效';
        _isLoading = false;
        notifyListeners();
        return false;
      }
    } catch (e) {
      _errorMessage = '扫描失败: $e';
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  Future<bool> confirmQRLogin(String qrCode) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      final data = await ApiService.post(
        '/api/qr-login/confirm',
        body: {
          'qrCode': qrCode,
          'userId': _user?.id,
        },
      );

      if (data['status'] == 'confirmed') {
        _isLoading = false;
        notifyListeners();
        return true;
      } else {
        _errorMessage = data['message'] ?? '确认失败';
        _isLoading = false;
        notifyListeners();
        return false;
      }
    } catch (e) {
      _errorMessage = '确认失败: $e';
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  Future<bool> register(String username, String email, String password) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      final data = await ApiService.post(
        '/api/auth/register',
        body: RegisterRequest(
          username: username,
          email: email,
          password: password,
          role: 'RESIDENT',
        ).toJson(),
      );

      final authResponse = AuthResponse.fromJson(data);
      await ApiService.saveToken(authResponse.accessToken);
      _user = authResponse.user;
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _errorMessage = '网络错误: $e';
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  Future<void> logout() async {
    try {
      await ApiService.post('/api/auth/logout');
    } catch (_) {}
    await ApiService.clearToken();
    _user = null;
    notifyListeners();
  }

  Future<void> checkAuth() async {
    final token = await ApiService.getToken();
    if (token != null) {
      try {
        final userData = await ApiService.get('/api/auth/me');
        _user = User.fromJson(userData);
        notifyListeners();
      } catch (e) {
        await logout();
      }
    }
  }

  Future<bool> updateProfile({String? username, String? phone}) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      final data = await ApiService.put(
        '/api/profile',
        body: {
          if (username != null) 'username': username,
          if (phone != null) 'phone': phone,
        },
      );

      _user = User.fromJson(data);
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _errorMessage = '更新失败: $e';
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  Future<bool> updateAvatar(String avatarUrl) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      final data = await ApiService.put(
        '/api/profile/avatar',
        body: {'avatar': avatarUrl},
      );

      _user = User.fromJson(data);
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _errorMessage = '头像更新失败: $e';
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }
}
