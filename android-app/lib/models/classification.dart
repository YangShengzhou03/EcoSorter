import 'package:json_annotation/json_annotation.dart';

part 'classification.g.dart';

@JsonSerializable()
class Classification {
  final String id;
  final String? userId;
  final String? username;
  final String? categoryName;
  final double? confidence;
  final String? result;
  final String? status;
  final String? imageUrl;
  final int? processingTime;
  final String? model;
  final String? createdAt;
  final String? notes;
  final String? disposalInstructions;
  final int? environmentalImpact;
  final List<String>? alternatives;
  final String? userFeedback;
  final int? points;

  Classification({
    required this.id,
    this.userId,
    this.username,
    this.categoryName,
    this.confidence,
    this.result,
    this.status,
    this.imageUrl,
    this.processingTime,
    this.model,
    this.createdAt,
    this.notes,
    this.disposalInstructions,
    this.environmentalImpact,
    this.alternatives,
    this.userFeedback,
    this.points,
  });

  factory Classification.fromJson(Map<String, dynamic> json) => _$ClassificationFromJson(json);
  Map<String, dynamic> toJson() => _$ClassificationToJson(this);
}

@JsonSerializable()
class ClassificationResponse {
  final List<Classification> records;
  final int total;
  final int current;
  final int size;

  ClassificationResponse({
    required this.records,
    required this.total,
    required this.current,
    required this.size,
  });

  factory ClassificationResponse.fromJson(Map<String, dynamic> json) => _$ClassificationResponseFromJson(json);
}

@JsonSerializable()
class WasteCategory {
  final String id;
  final String name;
  final String? description;
  final String? disposalMethod;
  final String? color;
  final String? icon;
  final int? environmentalImpact;
  final double? recyclingRate;
  final List<String>? commonItems;
  final String? disposalInstructions;
  final bool? specialHandling;
  final bool? hazardous;
  final bool? active;

  WasteCategory({
    required this.id,
    required this.name,
    this.description,
    this.disposalMethod,
    this.color,
    this.icon,
    this.environmentalImpact,
    this.recyclingRate,
    this.commonItems,
    this.disposalInstructions,
    this.specialHandling,
    this.hazardous,
    this.active,
  });

  factory WasteCategory.fromJson(Map<String, dynamic> json) => _$WasteCategoryFromJson(json);
}
