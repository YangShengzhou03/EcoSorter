import 'package:json_annotation/json_annotation.dart';

part 'banner.g.dart';

@JsonSerializable()
class Banner {
  final int id;
  final String title;
  final String? description;
  final String? background;
  final int? sortOrder;
  final String? target;

  Banner({
    required this.id,
    required this.title,
    this.description,
    this.background,
    this.sortOrder,
    this.target,
  });

  factory Banner.fromJson(Map<String, dynamic> json) => _$BannerFromJson(json);
  Map<String, dynamic> toJson() => _$BannerToJson(this);
}
