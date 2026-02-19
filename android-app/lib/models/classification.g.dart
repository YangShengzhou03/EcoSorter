// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'classification.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Classification _$ClassificationFromJson(Map<String, dynamic> json) =>
    Classification(
      id: json['id'] as String,
      userId: json['userId'] as String?,
      username: json['username'] as String?,
      categoryName: json['categoryName'] as String?,
      confidence: (json['confidence'] as num?)?.toDouble(),
      result: json['result'] as String?,
      status: json['status'] as String?,
      imageUrl: json['imageUrl'] as String?,
      processingTime: (json['processingTime'] as num?)?.toInt(),
      model: json['model'] as String?,
      createdAt: json['createdAt'] as String?,
      notes: json['notes'] as String?,
      disposalInstructions: json['disposalInstructions'] as String?,
      environmentalImpact: (json['environmentalImpact'] as num?)?.toInt(),
      alternatives: (json['alternatives'] as List<dynamic>?)
          ?.map((e) => e as String)
          .toList(),
      userFeedback: json['userFeedback'] as String?,
      points: (json['points'] as num?)?.toInt(),
    );

Map<String, dynamic> _$ClassificationToJson(Classification instance) =>
    <String, dynamic>{
      'id': instance.id,
      'userId': instance.userId,
      'username': instance.username,
      'categoryName': instance.categoryName,
      'confidence': instance.confidence,
      'result': instance.result,
      'status': instance.status,
      'imageUrl': instance.imageUrl,
      'processingTime': instance.processingTime,
      'model': instance.model,
      'createdAt': instance.createdAt,
      'notes': instance.notes,
      'disposalInstructions': instance.disposalInstructions,
      'environmentalImpact': instance.environmentalImpact,
      'alternatives': instance.alternatives,
      'userFeedback': instance.userFeedback,
      'points': instance.points,
    };

ClassificationResponse _$ClassificationResponseFromJson(
        Map<String, dynamic> json) =>
    ClassificationResponse(
      records: (json['records'] as List<dynamic>)
          .map((e) => Classification.fromJson(e as Map<String, dynamic>))
          .toList(),
      total: (json['total'] as num).toInt(),
      current: (json['current'] as num).toInt(),
      size: (json['size'] as num).toInt(),
    );

Map<String, dynamic> _$ClassificationResponseToJson(
        ClassificationResponse instance) =>
    <String, dynamic>{
      'records': instance.records,
      'total': instance.total,
      'current': instance.current,
      'size': instance.size,
    };

WasteCategory _$WasteCategoryFromJson(Map<String, dynamic> json) =>
    WasteCategory(
      id: json['id'] as String,
      name: json['name'] as String,
      description: json['description'] as String?,
      disposalMethod: json['disposalMethod'] as String?,
      color: json['color'] as String?,
      icon: json['icon'] as String?,
      environmentalImpact: (json['environmentalImpact'] as num?)?.toInt(),
      recyclingRate: (json['recyclingRate'] as num?)?.toDouble(),
      commonItems: (json['commonItems'] as List<dynamic>?)
          ?.map((e) => e as String)
          .toList(),
      disposalInstructions: json['disposalInstructions'] as String?,
      specialHandling: json['specialHandling'] as bool?,
      hazardous: json['hazardous'] as bool?,
      active: json['active'] as bool?,
    );

Map<String, dynamic> _$WasteCategoryToJson(WasteCategory instance) =>
    <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'description': instance.description,
      'disposalMethod': instance.disposalMethod,
      'color': instance.color,
      'icon': instance.icon,
      'environmentalImpact': instance.environmentalImpact,
      'recyclingRate': instance.recyclingRate,
      'commonItems': instance.commonItems,
      'disposalInstructions': instance.disposalInstructions,
      'specialHandling': instance.specialHandling,
      'hazardous': instance.hazardous,
      'active': instance.active,
    };
