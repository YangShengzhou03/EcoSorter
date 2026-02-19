import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:image_picker/image_picker.dart';

class ApiService {
  static const String baseUrl = 'http://10.0.2.2:8081';
  
  static const _storage = FlutterSecureStorage();

  static Map<String, String> get _headers => {
    'Content-Type': 'application/json; charset=UTF-8',
    'Accept': 'application/json; charset=UTF-8',
  };

  static Future<Map<String, String>> _getAuthHeaders() async {
    final token = await _storage.read(key: 'token');
    if (token != null) {
      return {
        ..._headers,
        'Authorization': 'Bearer $token',
      };
    }
    return _headers;
  }

  static dynamic _parseResponse(http.Response response) {
    return jsonDecode(utf8.decode(response.bodyBytes));
  }

  static Future<dynamic> get(String endpoint) async {
    final headers = await _getAuthHeaders();
    final response = await http.get(
      Uri.parse('$baseUrl$endpoint'),
      headers: headers,
    ).timeout(const Duration(seconds: 30));
    return _parseResponse(response);
  }

  static Future<Map<String, dynamic>> getAsMap(String endpoint) async {
    final result = await get(endpoint);
    if (result is Map<String, dynamic>) {
      return result;
    }
    if (result is Map) {
      return Map<String, dynamic>.from(result);
    }
    throw Exception('Expected Map but got ${result.runtimeType}');
  }

  static Future<List<dynamic>> getAsList(String endpoint) async {
    final result = await get(endpoint);
    if (result is List) {
      return result;
    }
    throw Exception('Expected List but got ${result.runtimeType}');
  }

  static Future<Map<String, dynamic>> post(String endpoint, {Map<String, dynamic>? body}) async {
    final headers = await _getAuthHeaders();
    final response = await http.post(
      Uri.parse('$baseUrl$endpoint'),
      headers: headers,
      body: body != null ? jsonEncode(body) : null,
    ).timeout(const Duration(seconds: 30));
    final result = _parseResponse(response);
    if (result is Map<String, dynamic>) {
      return result;
    }
    if (result is Map) {
      return Map<String, dynamic>.from(result);
    }
    throw Exception('Expected Map but got ${result.runtimeType}');
  }

  static Future<Map<String, dynamic>> put(String endpoint, {Map<String, dynamic>? body}) async {
    final headers = await _getAuthHeaders();
    final response = await http.put(
      Uri.parse('$baseUrl$endpoint'),
      headers: headers,
      body: body != null ? jsonEncode(body) : null,
    ).timeout(const Duration(seconds: 30));
    final result = _parseResponse(response);
    if (result is Map<String, dynamic>) {
      return result;
    }
    if (result is Map) {
      return Map<String, dynamic>.from(result);
    }
    throw Exception('Expected Map but got ${result.runtimeType}');
  }

  static Future<Map<String, dynamic>> delete(String endpoint) async {
    final headers = await _getAuthHeaders();
    final response = await http.delete(
      Uri.parse('$baseUrl$endpoint'),
      headers: headers,
    ).timeout(const Duration(seconds: 30));
    final result = _parseResponse(response);
    if (result is Map<String, dynamic>) {
      return result;
    }
    if (result is Map) {
      return Map<String, dynamic>.from(result);
    }
    throw Exception('Expected Map but got ${result.runtimeType}');
  }

  static Future<http.Response> postMultipart(String endpoint, String filePath, String fieldName) async {
    final token = await _storage.read(key: 'token');
    final request = http.MultipartRequest(
      'POST',
      Uri.parse('$baseUrl$endpoint'),
    );
    request.headers['Authorization'] = 'Bearer $token';
    request.files.add(await http.MultipartFile.fromPath(fieldName, filePath));
    
    final streamedResponse = await request.send().timeout(const Duration(seconds: 60));
    return await http.Response.fromStream(streamedResponse);
  }

  static Future<String?> uploadImage(String endpoint, {bool fromCamera = false}) async {
    final ImagePicker picker = ImagePicker();
    final XFile? image = await picker.pickImage(
      source: fromCamera ? ImageSource.camera : ImageSource.gallery,
      maxWidth: 1024,
      maxHeight: 1024,
      imageQuality: 85,
    );
    
    if (image == null) return null;
    
    final token = await _storage.read(key: 'token');
    final request = http.MultipartRequest(
      'POST',
      Uri.parse('$baseUrl$endpoint'),
    );
    request.headers['Authorization'] = 'Bearer $token';
    request.files.add(await http.MultipartFile.fromPath('file', image.path));
    
    final streamedResponse = await request.send().timeout(const Duration(seconds: 60));
    final response = await http.Response.fromStream(streamedResponse);
    
    if (response.statusCode == 200) {
      final result = jsonDecode(utf8.decode(response.bodyBytes));
      if (result is Map) {
        return result['url'] as String? ?? result['avatarUrl'] as String?;
      }
    }
    throw Exception('上传失败: ${response.statusCode}');
  }

  static Future<void> saveToken(String token) async {
    await _storage.write(key: 'token', value: token);
  }

  static Future<String?> getToken() async {
    return await _storage.read(key: 'token');
  }

  static Future<void> clearToken() async {
    await _storage.delete(key: 'token');
  }
}
