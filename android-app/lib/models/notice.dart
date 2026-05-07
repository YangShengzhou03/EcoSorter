import 'package:json_annotation/json_annotation.dart';

part 'notice.g.dart';

@JsonSerializable()
class Notice {
  final int id;
  final String title;
  final String content;
  final String? status;
  final String? targetAudience;
  final int? authorId;
  final String? createdAt;
  final String? updatedAt;

  Notice({
    required this.id,
    required this.title,
    required this.content,
    this.status,
    this.targetAudience,
    this.authorId,
    this.createdAt,
    this.updatedAt,
  });

  factory Notice.fromJson(Map<String, dynamic> json) => _$NoticeFromJson(json);
  Map<String, dynamic> toJson() => _$NoticeToJson(this);
}
