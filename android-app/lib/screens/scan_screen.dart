import 'package:flutter/material.dart';
import 'package:mobile_scanner/mobile_scanner.dart';
import 'package:provider/provider.dart';
import 'package:ecosorter/providers/classification_provider.dart';
import 'package:ecosorter/models/trashcan.dart';
import 'package:ecosorter/services/api_service.dart';

class ScanScreen extends StatefulWidget {
  const ScanScreen({super.key});

  @override
  State<ScanScreen> createState() => _ScanScreenState();
}

class _ScanScreenState extends State<ScanScreen> {
  bool _isScanning = true;
  bool _isLoading = false;
  MobileScannerController? _controller;

  @override
  void dispose() {
    _controller?.dispose();
    super.dispose();
  }

  void _onDetect(BarcodeCapture capture) {
    final barcode = capture.barcodes.first;
    if (barcode.rawValue == null) return;

    if (_isScanning) {
      setState(() {
        _isScanning = false;
      });

      _handleScannedCode(barcode.rawValue!);
    }
  }

  Future<void> _handleScannedCode(String code) async {
    if (!mounted) return;

    setState(() {
      _isLoading = true;
    });

    try {
      final data = await ApiService.get('/api/trashcans/device/$code');
      final trashcan = Trashcan.fromJson(data);
      
      if (mounted) {
        _showTrashcanDialog(trashcan);
      }
    } catch (e) {
      if (mounted) {
        _showErrorDialog('未找到该垃圾桶设备');
      }
    } finally {
      if (mounted) {
        setState(() {
          _isLoading = false;
        });
      }
    }
  }

  void _showTrashcanDialog(Trashcan trashcan) {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => AlertDialog(
        title: const Text('垃圾桶识别'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(
              _getBinTypeIcon(trashcan.binType),
              size: 80,
              color: _getBinTypeColor(trashcan.binType),
            ),
            const SizedBox(height: 16),
            Text(
              trashcan.deviceName ?? '垃圾桶',
              style: const TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 8),
            Text(
              trashcan.location ?? '未知位置',
              style: TextStyle(
                color: Colors.grey[600],
              ),
            ),
            const SizedBox(height: 8),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
              decoration: BoxDecoration(
                color: _getStatusColor(trashcan.status).withOpacity(0.1),
                borderRadius: BorderRadius.circular(16),
              ),
              child: Text(
                trashcan.statusText ?? '未知状态',
                style: TextStyle(
                  color: _getStatusColor(trashcan.status),
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            const SizedBox(height: 16),
            LinearProgressIndicator(
              value: trashcan.capacityPercent / 100,
              backgroundColor: Colors.grey[200],
              valueColor: AlwaysStoppedAnimation<Color>(
                trashcan.capacityPercent > 80 ? Colors.red : Colors.green,
              ),
            ),
            const SizedBox(height: 8),
            Text(
              '容量: ${trashcan.capacityPercent}%',
              style: TextStyle(
                fontSize: 12,
                color: Colors.grey[600],
              ),
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context);
              _resetScanner();
            },
            child: const Text('重新扫描'),
          ),
          ElevatedButton(
            onPressed: trashcan.capacityPercent < 90
                ? () {
                    Navigator.pop(context);
                    _showClassificationSelection(trashcan);
                  }
                : null,
            child: const Text('开始投放'),
          ),
        ],
      ),
    );
  }

  void _showClassificationSelection(Trashcan trashcan) {
    final provider = Provider.of<ClassificationProvider>(context, listen: false);
    
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('选择垃圾类型'),
        content: Consumer<ClassificationProvider>(
          builder: (context, provider, child) {
            if (provider.categories.isEmpty) {
              return const Center(child: CircularProgressIndicator());
            }
            
            return SizedBox(
              width: double.maxFinite,
              height: 300,
              child: ListView.builder(
                itemCount: provider.categories.length,
                itemBuilder: (context, index) {
                  final category = provider.categories[index];
                  return ListTile(
                    leading: CircleAvatar(
                      backgroundColor: _getCategoryColor(category.name),
                      child: Icon(
                        _getCategoryIcon(category.name),
                        color: Colors.white,
                        size: 20,
                      ),
                    ),
                    title: Text(category.name),
                    subtitle: category.description != null
                        ? Text(
                            category.description!,
                            style: const TextStyle(fontSize: 12),
                          )
                        : null,
                    onTap: () {
                      Navigator.pop(context);
                      _showSuccessDialog(trashcan, category.name);
                    },
                  );
                },
              ),
            );
          },
        ),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context);
              _resetScanner();
            },
            child: const Text('取消'),
          ),
        ],
      ),
    );
    
    provider.loadCategories();
  }

  void _showSuccessDialog(Trashcan trashcan, String categoryName) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('投放成功'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            const Icon(
              Icons.check_circle,
              size: 80,
              color: Colors.green,
            ),
            const SizedBox(height: 16),
            const Text(
              '感谢您的环保行为！',
              style: TextStyle(fontSize: 16),
            ),
            const SizedBox(height: 8),
            Text(
              '垃圾类型: $categoryName',
              style: TextStyle(
                color: Colors.grey[600],
              ),
            ),
            const SizedBox(height: 8),
            Container(
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: Colors.orange[50],
                borderRadius: BorderRadius.circular(8),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    Icons.monetization_on,
                    color: Colors.orange[700],
                  ),
                  const SizedBox(width: 8),
                  Text(
                    '+10 积分',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                      color: Colors.orange[700],
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
        actions: [
          ElevatedButton(
            onPressed: () {
              Navigator.pop(context);
              _resetScanner();
            },
            child: const Text('完成'),
          ),
        ],
      ),
    );
  }

  void _showErrorDialog(String message) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('错误'),
        content: Text(message),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context);
              _resetScanner();
            },
            child: const Text('重试'),
          ),
        ],
      ),
    );
  }

  void _resetScanner() {
    setState(() {
      _isScanning = true;
      _isLoading = false;
    });
  }

  IconData _getBinTypeIcon(String? binType) {
    switch (binType?.toLowerCase()) {
      case 'recyclable':
      case '可回收物':
        return Icons.recycling;
      case 'kitchen':
      case '厨余垃圾':
        return Icons.compost;
      case 'hazardous':
      case '有害垃圾':
        return Icons.warning;
      case 'other':
      case '其他垃圾':
        return Icons.delete;
      default:
        return Icons.delete_outline;
    }
  }

  Color _getBinTypeColor(String? binType) {
    switch (binType?.toLowerCase()) {
      case 'recyclable':
      case '可回收物':
        return Colors.green;
      case 'kitchen':
      case '厨余垃圾':
        return Colors.orange;
      case 'hazardous':
      case '有害垃圾':
        return Colors.red;
      case 'other':
      case '其他垃圾':
        return Colors.grey;
      default:
        return Colors.blue;
    }
  }

  Color _getStatusColor(String? status) {
    switch (status) {
      case 'online':
        return Colors.green;
      case 'offline':
        return Colors.grey;
      case 'maintenance':
        return Colors.orange;
      case 'error':
        return Colors.red;
      default:
        return Colors.grey;
    }
  }

  Color _getCategoryColor(String? name) {
    switch (name) {
      case '可回收物':
        return Colors.green;
      case '厨余垃圾':
        return Colors.orange;
      case '有害垃圾':
        return Colors.red;
      case '其他垃圾':
        return Colors.grey;
      default:
        return Colors.blue;
    }
  }

  IconData _getCategoryIcon(String? name) {
    switch (name) {
      case '可回收物':
        return Icons.recycling;
      case '厨余垃圾':
        return Icons.compost;
      case '有害垃圾':
        return Icons.warning;
      case '其他垃圾':
        return Icons.delete;
      default:
        return Icons.help_outline;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('扫码投放'),
        actions: [
          IconButton(
            icon: Icon(_isScanning ? Icons.flash_off : Icons.flash_on),
            onPressed: () {
              _controller?.toggleTorch();
            },
          ),
        ],
      ),
      body: Stack(
        children: [
          MobileScanner(
            controller: _controller,
            onDetect: _onDetect,
            errorBuilder: (context, error, child) {
              return Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Icon(
                      Icons.error_outline,
                      size: 80,
                      color: Colors.red,
                    ),
                    const SizedBox(height: 16),
                    Text(
                      '相机错误: ${error.errorCode}',
                      textAlign: TextAlign.center,
                      style: const TextStyle(color: Colors.red),
                    ),
                  ],
                ),
              );
            },
          ),
          _buildScanOverlay(),
          if (_isLoading)
            Container(
              color: Colors.black.withOpacity(0.5),
              child: const Center(
                child: CircularProgressIndicator(),
              ),
            ),
        ],
      ),
    );
  }

  Widget _buildScanOverlay() {
    return Container(
      color: Colors.black.withOpacity(0.3),
      child: Center(
        child: Container(
          width: 280,
          height: 280,
          decoration: BoxDecoration(
            border: Border.all(
              color: Colors.green,
              width: 3,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          child: Stack(
            children: [
              _buildCorner(),
              _buildCorner(alignment: Alignment.topRight),
              _buildCorner(alignment: Alignment.bottomLeft),
              _buildCorner(alignment: Alignment.bottomRight),
              if (!_isScanning)
                Container(
                  color: Colors.white,
                  child: const Center(
                    child: Icon(
                      Icons.check_circle,
                      size: 80,
                      color: Colors.green,
                    ),
                  ),
                ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildCorner({Alignment alignment = Alignment.topLeft}) {
    return Align(
      alignment: alignment,
      child: Container(
        width: 30,
        height: 30,
        decoration: BoxDecoration(
          border: Border.all(
            color: Colors.green,
            width: 4,
          ),
          borderRadius: BorderRadius.circular(4),
        ),
      ),
    );
  }
}
