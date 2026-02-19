import 'package:json_annotation/json_annotation.dart';

part 'trashcan.g.dart';

@JsonSerializable()
class Trashcan {
  final int id;
  final String? deviceId;
  final String? deviceName;
  final String? location;
  final int? capacityLevel;
  final int? maxCapacity;
  final int? threshold;
  final String? status;
  final String? statusText;
  final String? binType;
  final double? latitude;
  final double? longitude;
  final String? lastUpdate;

  Trashcan({
    required this.id,
    this.deviceId,
    this.deviceName,
    this.location,
    this.capacityLevel,
    this.maxCapacity,
    this.threshold,
    this.status,
    this.statusText,
    this.binType,
    this.latitude,
    this.longitude,
    this.lastUpdate,
  });

  factory Trashcan.fromJson(Map<String, dynamic> json) => _$TrashcanFromJson(json);

  int get capacityPercent {
    if (maxCapacity == null || maxCapacity == 0) return 0;
    return ((capacityLevel ?? 0) * 100 / maxCapacity!).round();
  }
}
