// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'trashcan.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Trashcan _$TrashcanFromJson(Map<String, dynamic> json) => Trashcan(
      id: (json['id'] as num).toInt(),
      deviceId: json['deviceId'] as String?,
      deviceName: json['deviceName'] as String?,
      location: json['location'] as String?,
      capacityLevel: (json['capacityLevel'] as num?)?.toInt(),
      maxCapacity: (json['maxCapacity'] as num?)?.toInt(),
      threshold: (json['threshold'] as num?)?.toInt(),
      status: json['status'] as String?,
      statusText: json['statusText'] as String?,
      binType: json['binType'] as String?,
      latitude: (json['latitude'] as num?)?.toDouble(),
      longitude: (json['longitude'] as num?)?.toDouble(),
      lastUpdate: json['lastUpdate'] as String?,
    );

Map<String, dynamic> _$TrashcanToJson(Trashcan instance) => <String, dynamic>{
      'id': instance.id,
      'deviceId': instance.deviceId,
      'deviceName': instance.deviceName,
      'location': instance.location,
      'capacityLevel': instance.capacityLevel,
      'maxCapacity': instance.maxCapacity,
      'threshold': instance.threshold,
      'status': instance.status,
      'statusText': instance.statusText,
      'binType': instance.binType,
      'latitude': instance.latitude,
      'longitude': instance.longitude,
      'lastUpdate': instance.lastUpdate,
    };
