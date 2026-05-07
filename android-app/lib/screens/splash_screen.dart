import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:ecosorter/providers/auth_provider.dart';
import 'package:ecosorter/theme/app_theme.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  void initState() {
    super.initState();
    _initApp();
  }

  Future<void> _initApp() async {
    final authProvider = Provider.of<AuthProvider>(context, listen: false);
    await authProvider.checkAuth();

    await Future.delayed(const Duration(seconds: 1));

    if (!mounted) return;

    if (authProvider.isAuthenticated) {
      Navigator.pushReplacementNamed(context, '/main-tab');
    } else {
      Navigator.pushReplacementNamed(context, '/login');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        width: double.infinity,
        color: Colors.white,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              width: 88,
              height: 88,
              decoration: BoxDecoration(
                color: AppTheme.primaryColor.withOpacity(0.1),
                borderRadius: BorderRadius.circular(22),
              ),
              child: Icon(Icons.recycling_rounded, size: 52, color: AppTheme.primaryColor),
            ),
            const SizedBox(height: 20),
            Text(
              '垃圾分类',
              style: TextStyle(fontSize: 28, fontWeight: FontWeight.w700, color: Colors.black87, letterSpacing: 2),
            ),
          ],
        ),
      ),
    );
  }
}
