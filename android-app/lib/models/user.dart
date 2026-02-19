import 'package:json_annotation/json_annotation.dart';

part 'user.g.dart';

@JsonSerializable()
class User {
  final String id;
  final String username;
  final String email;
  final String? role;
  final bool? isActive;
  final String? avatarUrl;
  final String? phone;
  final String? lastLogin;
  final String? createdAt;
  final String? updatedAt;

  User({
    required this.id,
    required this.username,
    required this.email,
    this.role,
    this.isActive,
    this.avatarUrl,
    this.phone,
    this.lastLogin,
    this.createdAt,
    this.updatedAt,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    final profile = json['profile'] as Map<String, dynamic>?;
    return User(
      id: json['id']?.toString() ?? '',
      username: json['username'] as String? ?? '',
      email: json['email'] as String? ?? '',
      role: json['role'] as String?,
      isActive: json['isActive'] as bool?,
      avatarUrl: profile?['avatar'] as String? ?? json['avatar'] as String? ?? json['avatarUrl'] as String?,
      phone: profile?['phone'] as String? ?? json['phone'] as String?,
      lastLogin: json['lastLogin'] as String?,
      createdAt: json['createdAt']?.toString(),
      updatedAt: json['updatedAt']?.toString(),
    );
  }
  
  Map<String, dynamic> toJson() => _$UserToJson(this);
}

@JsonSerializable()
class LoginRequest {
  final String email;
  final String password;

  LoginRequest({
    required this.email,
    required this.password,
  });

  Map<String, dynamic> toJson() => _$LoginRequestToJson(this);
}

@JsonSerializable()
class RegisterRequest {
  final String username;
  final String email;
  final String password;
  final String? role;

  RegisterRequest({
    required this.username,
    required this.email,
    required this.password,
    this.role,
  });

  Map<String, dynamic> toJson() => _$RegisterRequestToJson(this);
}

@JsonSerializable()
class AuthResponse {
  final String accessToken;
  final String? refreshToken;
  final User user;

  AuthResponse({
    required this.accessToken,
    this.refreshToken,
    required this.user,
  });

  factory AuthResponse.fromJson(Map<String, dynamic> json) => _$AuthResponseFromJson(json);
}
