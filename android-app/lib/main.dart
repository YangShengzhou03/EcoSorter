import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:ecosorter/providers/auth_provider.dart';
import 'package:ecosorter/providers/user_provider.dart';
import 'package:ecosorter/providers/product_provider.dart';
import 'package:ecosorter/providers/classification_provider.dart';
import 'package:ecosorter/providers/notice_provider.dart';
import 'package:ecosorter/providers/banner_provider.dart';
import 'package:ecosorter/screens/splash_screen.dart';
import 'package:ecosorter/screens/records_screen.dart';
import 'package:ecosorter/screens/points_screen.dart';
import 'package:ecosorter/screens/product_detail_screen.dart';
import 'package:ecosorter/screens/orders_screen.dart';
import 'package:ecosorter/screens/notification_screen.dart';
import 'package:ecosorter/screens/profile_edit_screen.dart';
import 'package:ecosorter/screens/help_screen.dart';
import 'package:ecosorter/screens/about_screen.dart';
import 'package:ecosorter/screens/settings_screen.dart';
import 'package:ecosorter/screens/search_result_screen.dart';
import 'package:ecosorter/pages/auth/login_page.dart';
import 'package:ecosorter/pages/auth/register_page.dart';
import 'package:ecosorter/pages/auth/forgot_password_page.dart';
import 'package:ecosorter/pages/main_tab_page.dart';
import 'package:ecosorter/theme/app_theme.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => AuthProvider()),
        ChangeNotifierProvider(create: (_) => UserProvider()),
        ChangeNotifierProvider(create: (_) => ProductProvider()),
        ChangeNotifierProvider(create: (_) => ClassificationProvider()),
        ChangeNotifierProvider(create: (_) => NoticeProvider()),
        ChangeNotifierProvider(create: (_) => BannerProvider()),
      ],
      child: MaterialApp(
        title: '垃圾分类',
        debugShowCheckedModeBanner: false,
        theme: AppTheme.lightTheme,
        initialRoute: '/',
        onGenerateRoute: (settings) {
          switch (settings.name) {
            case '/':
              return MaterialPageRoute(builder: (context) => const SplashScreen());
            case '/login':
              return MaterialPageRoute(builder: (context) => const LoginPage());
            case '/register':
              return MaterialPageRoute(builder: (context) => const RegisterPage());
            case '/forgot-password':
              return MaterialPageRoute(builder: (context) => const ForgotPasswordPage());
            case '/main-tab':
              return MaterialPageRoute(builder: (context) => const MainTabPage());
            case '/records':
              return MaterialPageRoute(builder: (context) => const RecordsScreen());
            case '/points':
              return MaterialPageRoute(builder: (context) => const PointsScreen());
            case '/orders':
              return MaterialPageRoute(builder: (context) => const OrdersScreen());
            case '/notification':
              return MaterialPageRoute(builder: (context) => const NotificationScreen());
            case '/profile-edit':
              return MaterialPageRoute(builder: (context) => const ProfileEditScreen());
            case '/help':
              return MaterialPageRoute(builder: (context) => const HelpScreen());
            case '/about':
              return MaterialPageRoute(builder: (context) => const AboutScreen());
            case '/settings':
              return MaterialPageRoute(builder: (context) => const SettingsScreen());
            case '/search':
              final keyword = settings.arguments as String? ?? '';
              return MaterialPageRoute(
                builder: (context) => SearchResultScreen(keyword: keyword),
              );
            case '/product-detail':
              final productId = settings.arguments as int?;
              return MaterialPageRoute(
                builder: (context) => ProductDetailScreen(productId: productId ?? 0),
              );
          }
          return null;
        },
      ),
    );
  }
}
