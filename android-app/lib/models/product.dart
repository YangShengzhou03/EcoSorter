import 'package:json_annotation/json_annotation.dart';

part 'product.g.dart';

@JsonSerializable()
class Product {
  final int id;
  final String name;
  final String? description;
  final String? imageUrl;
  final int points;
  final int stock;
  final int? maxPurchase;
  final String? status;
  final String? category;
  final int? totalPurchased;
  final int? userPurchased;
  final String? createdAt;
  final String? updatedAt;

  Product({
    required this.id,
    required this.name,
    this.description,
    this.imageUrl,
    required this.points,
    required this.stock,
    this.maxPurchase,
    this.status,
    this.category,
    this.totalPurchased,
    this.userPurchased,
    this.createdAt,
    this.updatedAt,
  });

  factory Product.fromJson(Map<String, dynamic> json) => _$ProductFromJson(json);
  Map<String, dynamic> toJson() => _$ProductToJson(this);
}

@JsonSerializable()
class ProductListResponse {
  final List<Product> records;
  final int total;
  final int current;
  final int size;

  ProductListResponse({
    required this.records,
    required this.total,
    required this.current,
    required this.size,
  });

  factory ProductListResponse.fromJson(Map<String, dynamic> json) => _$ProductListResponseFromJson(json);
}

@JsonSerializable()
class OrderRequest {
  final int productId;
  final int quantity;
  final String contactName;
  final String contactPhone;
  final String shippingAddress;

  OrderRequest({
    required this.productId,
    required this.quantity,
    required this.contactName,
    required this.contactPhone,
    required this.shippingAddress,
  });

  Map<String, dynamic> toJson() => _$OrderRequestToJson(this);
}

@JsonSerializable()
class Order {
  final int id;
  final int productId;
  final String productName;
  final String? productImageUrl;
  final int quantity;
  final int totalPoints;
  final String? contactName;
  final String? contactPhone;
  final String? shippingAddress;
  final String? trackingNumber;
  final String status;
  final String? remark;
  final String? createdAt;
  final String? updatedAt;

  Order({
    required this.id,
    required this.productId,
    required this.productName,
    this.productImageUrl,
    required this.quantity,
    required this.totalPoints,
    this.contactName,
    this.contactPhone,
    this.shippingAddress,
    this.trackingNumber,
    required this.status,
    this.remark,
    this.createdAt,
    this.updatedAt,
  });

  factory Order.fromJson(Map<String, dynamic> json) => _$OrderFromJson(json);
}

@JsonSerializable()
class OrderListResponse {
  final List<Order> records;
  final int total;
  final int current;
  final int size;

  OrderListResponse({
    required this.records,
    required this.total,
    required this.current,
    required this.size,
  });

  factory OrderListResponse.fromJson(Map<String, dynamic> json) => _$OrderListResponseFromJson(json);
}
