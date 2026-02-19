// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'product.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Product _$ProductFromJson(Map<String, dynamic> json) => Product(
      id: (json['id'] as num).toInt(),
      name: json['name'] as String,
      description: json['description'] as String?,
      imageUrl: json['imageUrl'] as String?,
      points: (json['points'] as num).toInt(),
      stock: (json['stock'] as num).toInt(),
      maxPurchase: (json['maxPurchase'] as num?)?.toInt(),
      status: json['status'] as String?,
      category: json['category'] as String?,
      totalPurchased: (json['totalPurchased'] as num?)?.toInt(),
      userPurchased: (json['userPurchased'] as num?)?.toInt(),
      createdAt: json['createdAt'] as String?,
      updatedAt: json['updatedAt'] as String?,
    );

Map<String, dynamic> _$ProductToJson(Product instance) => <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'description': instance.description,
      'imageUrl': instance.imageUrl,
      'points': instance.points,
      'stock': instance.stock,
      'maxPurchase': instance.maxPurchase,
      'status': instance.status,
      'category': instance.category,
      'totalPurchased': instance.totalPurchased,
      'userPurchased': instance.userPurchased,
      'createdAt': instance.createdAt,
      'updatedAt': instance.updatedAt,
    };

ProductListResponse _$ProductListResponseFromJson(Map<String, dynamic> json) =>
    ProductListResponse(
      records: (json['records'] as List<dynamic>)
          .map((e) => Product.fromJson(e as Map<String, dynamic>))
          .toList(),
      total: (json['total'] as num).toInt(),
      current: (json['current'] as num).toInt(),
      size: (json['size'] as num).toInt(),
    );

Map<String, dynamic> _$ProductListResponseToJson(
        ProductListResponse instance) =>
    <String, dynamic>{
      'records': instance.records,
      'total': instance.total,
      'current': instance.current,
      'size': instance.size,
    };

OrderRequest _$OrderRequestFromJson(Map<String, dynamic> json) => OrderRequest(
      productId: (json['productId'] as num).toInt(),
      quantity: (json['quantity'] as num).toInt(),
      contactName: json['contactName'] as String,
      contactPhone: json['contactPhone'] as String,
      shippingAddress: json['shippingAddress'] as String,
    );

Map<String, dynamic> _$OrderRequestToJson(OrderRequest instance) =>
    <String, dynamic>{
      'productId': instance.productId,
      'quantity': instance.quantity,
      'contactName': instance.contactName,
      'contactPhone': instance.contactPhone,
      'shippingAddress': instance.shippingAddress,
    };

Order _$OrderFromJson(Map<String, dynamic> json) => Order(
      id: (json['id'] as num).toInt(),
      productId: (json['productId'] as num).toInt(),
      productName: json['productName'] as String,
      productImageUrl: json['productImageUrl'] as String?,
      quantity: (json['quantity'] as num).toInt(),
      totalPoints: (json['totalPoints'] as num).toInt(),
      contactName: json['contactName'] as String?,
      contactPhone: json['contactPhone'] as String?,
      shippingAddress: json['shippingAddress'] as String?,
      trackingNumber: json['trackingNumber'] as String?,
      status: json['status'] as String,
      remark: json['remark'] as String?,
      createdAt: json['createdAt'] as String?,
      updatedAt: json['updatedAt'] as String?,
    );

Map<String, dynamic> _$OrderToJson(Order instance) => <String, dynamic>{
      'id': instance.id,
      'productId': instance.productId,
      'productName': instance.productName,
      'productImageUrl': instance.productImageUrl,
      'quantity': instance.quantity,
      'totalPoints': instance.totalPoints,
      'contactName': instance.contactName,
      'contactPhone': instance.contactPhone,
      'shippingAddress': instance.shippingAddress,
      'trackingNumber': instance.trackingNumber,
      'status': instance.status,
      'remark': instance.remark,
      'createdAt': instance.createdAt,
      'updatedAt': instance.updatedAt,
    };

OrderListResponse _$OrderListResponseFromJson(Map<String, dynamic> json) =>
    OrderListResponse(
      records: (json['records'] as List<dynamic>)
          .map((e) => Order.fromJson(e as Map<String, dynamic>))
          .toList(),
      total: (json['total'] as num).toInt(),
      current: (json['current'] as num).toInt(),
      size: (json['size'] as num).toInt(),
    );

Map<String, dynamic> _$OrderListResponseToJson(OrderListResponse instance) =>
    <String, dynamic>{
      'records': instance.records,
      'total': instance.total,
      'current': instance.current,
      'size': instance.size,
    };
