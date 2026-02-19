import 'package:flutter/foundation.dart';
import 'package:ecosorter/models/product.dart';
import 'package:ecosorter/services/api_service.dart';

class ProductProvider with ChangeNotifier {
  List<Product> _products = [];
  Product? _selectedProduct;
  List<Order> _orders = [];
  bool _isLoading = false;
  int _currentPage = 1;
  int _pageSize = 8;
  int _total = 0;

  List<Product> get products => _products;
  Product? get selectedProduct => _selectedProduct;
  List<Order> get orders => _orders;
  bool get isLoading => _isLoading;
  int get total => _total;
  bool get hasMore => _products.length < _total;

  Future<void> loadProducts({int page = 1, int pageSize = 8}) async {
    _isLoading = true;
    _currentPage = page;
    _pageSize = pageSize;
    notifyListeners();

    try {
      final data = await ApiService.get(
        '/api/products?page=$page&pageSize=$pageSize',
      );
      final productListResponse = ProductListResponse.fromJson(data);
      
      if (page == 1) {
        _products = productListResponse.records;
      } else {
        _products.addAll(productListResponse.records);
      }
      _total = productListResponse.total;
    } catch (e) {
      debugPrint('加载商品失败: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> loadProductDetail(int productId) async {
    _isLoading = true;
    notifyListeners();

    try {
      final data = await ApiService.get('/api/products/$productId');
      _selectedProduct = Product.fromJson(data);
    } catch (e) {
      debugPrint('加载商品详情失败: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<bool> createOrder({
    required int productId,
    required int quantity,
    required String contactName,
    required String contactPhone,
    required String shippingAddress,
  }) async {
    _isLoading = true;
    notifyListeners();

    try {
      await ApiService.post(
        '/api/orders',
        body: OrderRequest(
          productId: productId,
          quantity: quantity,
          contactName: contactName,
          contactPhone: contactPhone,
          shippingAddress: shippingAddress,
        ).toJson(),
      );
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      debugPrint('创建订单失败: $e');
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  Future<void> loadOrders({int page = 1, int pageSize = 10}) async {
    _isLoading = true;
    notifyListeners();

    try {
      final data = await ApiService.get(
        '/api/orders?page=$page&pageSize=$pageSize',
      );
      final orderListResponse = OrderListResponse.fromJson(data);
      _orders = orderListResponse.records;
      _total = orderListResponse.total;
    } catch (e) {
      debugPrint('加载订单失败: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> loadMore() async {
    if (!hasMore || _isLoading) return;
    await loadProducts(page: _currentPage + 1, pageSize: _pageSize);
  }

  Future<void> refresh() async {
    await loadProducts(page: 1, pageSize: _pageSize);
  }
}
