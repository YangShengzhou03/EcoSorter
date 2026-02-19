import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:ecosorter/theme/app_theme.dart';
import 'package:ecosorter/providers/auth_provider.dart';

class RegisterPage extends StatefulWidget {
  const RegisterPage({super.key});

  @override
  State<RegisterPage> createState() => _RegisterPageState();
}

class _RegisterPageState extends State<RegisterPage> {
  final _formKey = GlobalKey<FormState>();
  final _usernameController = TextEditingController();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  final _confirmPasswordController = TextEditingController();
  bool _obscurePassword = true;
  bool _obscureConfirmPassword = true;
  bool _agreedToTerms = false;

  @override
  void dispose() {
    _usernameController.dispose();
    _emailController.dispose();
    _passwordController.dispose();
    _confirmPasswordController.dispose();
    super.dispose();
  }

  Future<void> _handleRegister() async {
    if (!(_formKey.currentState?.validate() ?? false)) return;

    if (!_agreedToTerms) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('请阅读并同意用户协议和隐私政策'),
          backgroundColor: AppTheme.errorColor,
        ),
      );
      return;
    }

    final authProvider = Provider.of<AuthProvider>(context, listen: false);

    final success = await authProvider.register(
      _usernameController.text.trim(),
      _emailController.text.trim(),
      _passwordController.text.trim(),
    );

    if (success && mounted) {
      Navigator.pushReplacementNamed(context, '/main-tab');
    }
  }

  void _showTermsDialog(String title, String content) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text(
          title,
          style: const TextStyle(
            fontSize: 18,
            fontWeight: FontWeight.w700,
            color: AppTheme.textPrimary,
          ),
        ),
        content: SingleChildScrollView(
          child: Text(
            content,
            style: const TextStyle(
              fontSize: 14,
              height: 1.6,
              color: AppTheme.textSecondary,
            ),
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('我已阅读'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.backgroundColor,
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back_ios, size: 20),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.symmetric(horizontal: 24),
          child: Form(
            key: _formKey,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const SizedBox(height: 20),
                const Text(
                  '创建账号',
                  style: TextStyle(
                    fontSize: 28,
                    fontWeight: FontWeight.w800,
                    color: AppTheme.textPrimary,
                    letterSpacing: -1,
                  ),
                ),
                const SizedBox(height: 8),
                const Text(
                  '注册账号，开启智能垃圾分类之旅',
                  style: TextStyle(
                    fontSize: 15,
                    color: AppTheme.textSecondary,
                  ),
                ),
                const SizedBox(height: 36),
                TextFormField(
                  controller: _usernameController,
                  decoration: const InputDecoration(
                    labelText: '用户名',
                    prefixIcon: Icon(Icons.person_outline_rounded),
                    helperText: '用户名将用于登录和展示',
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return '请输入用户名';
                    }
                    if (value.length < 2) {
                      return '用户名至少2个字符';
                    }
                    if (value.length > 20) {
                      return '用户名最多20个字符';
                    }
                    if (!RegExp(r'^[\u4e00-\u9fa5a-zA-Z0-9_]+$').hasMatch(value)) {
                      return '用户名只能包含中文、字母、数字和下划线';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 16),
                TextFormField(
                  controller: _emailController,
                  keyboardType: TextInputType.emailAddress,
                  decoration: const InputDecoration(
                    labelText: '邮箱',
                    prefixIcon: Icon(Icons.email_outlined),
                    helperText: '用于找回密码和接收通知',
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return '请输入邮箱';
                    }
                    if (!RegExp(r'^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$').hasMatch(value)) {
                      return '请输入正确的邮箱格式';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 16),
                TextFormField(
                  controller: _passwordController,
                  obscureText: _obscurePassword,
                  decoration: InputDecoration(
                    labelText: '密码',
                    prefixIcon: const Icon(Icons.lock_outline_rounded),
                    helperText: '至少6个字符',
                    suffixIcon: IconButton(
                      icon: Icon(
                        _obscurePassword ? Icons.visibility_off : Icons.visibility,
                      ),
                      onPressed: () {
                        setState(() {
                          _obscurePassword = !_obscurePassword;
                        });
                      },
                    ),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return '请输入密码';
                    }
                    if (value.length < 6) {
                      return '密码至少6个字符';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 16),
                TextFormField(
                  controller: _confirmPasswordController,
                  obscureText: _obscureConfirmPassword,
                  decoration: InputDecoration(
                    labelText: '确认密码',
                    prefixIcon: const Icon(Icons.lock_outline_rounded),
                    suffixIcon: IconButton(
                      icon: Icon(
                        _obscureConfirmPassword ? Icons.visibility_off : Icons.visibility,
                      ),
                      onPressed: () {
                        setState(() {
                          _obscureConfirmPassword = !_obscureConfirmPassword;
                        });
                      },
                    ),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return '请再次输入密码';
                    }
                    if (value != _passwordController.text) {
                      return '两次输入的密码不一致';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 24),
                Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    SizedBox(
                      width: 24,
                      height: 24,
                      child: Checkbox(
                        value: _agreedToTerms,
                        onChanged: (value) {
                          setState(() {
                            _agreedToTerms = value ?? false;
                          });
                        },
                        activeColor: AppTheme.primaryColor,
                        materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
                        visualDensity: VisualDensity.compact,
                      ),
                    ),
                    const SizedBox(width: 8),
                    Expanded(
                      child: RichText(
                        text: TextSpan(
                          style: const TextStyle(
                            fontSize: 13,
                            color: AppTheme.textSecondary,
                            height: 1.5,
                          ),
                          children: [
                            const TextSpan(text: '我已阅读并同意'),
                            TextSpan(
                              text: '《用户协议》',
                              style: const TextStyle(
                                color: AppTheme.primaryColor,
                                fontWeight: FontWeight.w600,
                              ),
                              recognizer: TapGestureRecognizer()
                                ..onTap = () {
                                  _showTermsDialog(
                                    '用户协议',
                                    _userAgreement,
                                  );
                                },
                            ),
                            const TextSpan(text: '和'),
                            TextSpan(
                              text: '《隐私政策》',
                              style: const TextStyle(
                                color: AppTheme.primaryColor,
                                fontWeight: FontWeight.w600,
                              ),
                              recognizer: TapGestureRecognizer()
                                ..onTap = () {
                                  _showTermsDialog(
                                    '隐私政策',
                                    _privacyPolicy,
                                  );
                                },
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 24),
                Consumer<AuthProvider>(
                  builder: (context, authProvider, child) {
                    return Column(
                      children: [
                        if (authProvider.errorMessage != null)
                          Container(
                            width: double.infinity,
                            margin: const EdgeInsets.only(bottom: 16),
                            padding: const EdgeInsets.all(12),
                            decoration: BoxDecoration(
                              color: AppTheme.errorColor.withOpacity(0.1),
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Row(
                              children: [
                                const Icon(
                                  Icons.error_outline,
                                  color: AppTheme.errorColor,
                                  size: 20,
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: Text(
                                    authProvider.errorMessage!,
                                    style: const TextStyle(
                                      color: AppTheme.errorColor,
                                      fontSize: 13,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        SizedBox(
                          width: double.infinity,
                          height: 52,
                          child: ElevatedButton(
                            onPressed: authProvider.isLoading ? null : _handleRegister,
                            style: ElevatedButton.styleFrom(
                              backgroundColor: _agreedToTerms
                                  ? AppTheme.primaryColor
                                  : AppTheme.textTertiary,
                            ),
                            child: authProvider.isLoading
                                ? const SizedBox(
                                    width: 24,
                                    height: 24,
                                    child: CircularProgressIndicator(
                                      color: Colors.white,
                                      strokeWidth: 2,
                                    ),
                                  )
                                : const Text(
                                    '注册',
                                    style: TextStyle(fontSize: 16),
                                  ),
                          ),
                        ),
                      ],
                    );
                  },
                ),
                const SizedBox(height: 24),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Text(
                      '已有账号？',
                      style: TextStyle(
                        color: AppTheme.textSecondary,
                        fontSize: 14,
                      ),
                    ),
                    TextButton(
                      onPressed: () {
                        Navigator.pop(context);
                      },
                      style: TextButton.styleFrom(
                        padding: const EdgeInsets.only(left: 4),
                      ),
                      child: const Text(
                        '立即登录',
                        style: TextStyle(
                          color: AppTheme.primaryColor,
                          fontWeight: FontWeight.w600,
                          fontSize: 14,
                        ),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 24),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

const String _userAgreement = '''
一、服务条款的确认和接纳

EcoSorter智能垃圾分类系统的各项服务的所有权和运营权归本平台所有。用户在使用本平台提供的各项服务之前，应仔细阅读本服务协议。

如您不同意本服务协议及/或随时对其的修改，您可以主动取消本平台提供的服务；您一旦使用本平台服务，即视为您已了解并完全同意本服务协议各项内容。

二、用户注册

1. 用户注册成功后，本平台将给予每个用户一个用户账号及相应的密码，该用户账号和密码由用户负责保管；用户应当对以其用户账号进行的所有活动和事件负法律责任。

2. 用户对以其用户账号进行的所有活动和事件负法律责任。

3. 用户账号创建后，用户账号和密码由用户负责保管，用户不应将其账号、密码转让或出借予他人使用。如用户发现其账号遭他人非法使用，应立即通知本平台。因黑客行为或用户的保管疏忽导致账号、密码遭他人非法使用，本平台不承担任何责任。

三、使用规则

1. 用户在使用本平台服务过程中，必须遵循以下原则：
   (1) 遵守中国有关的法律和法规；
   (2) 不得为任何非法目的而使用网络服务系统；
   (3) 遵守所有与网络服务有关的网络协议、规定和程序；
   (4) 不得利用本平台服务进行任何可能对互联网正常运转造成不利影响的行为；

2. 用户承诺不发布、传播以下信息：
   (1) 反对宪法所确定的基本原则的；
   (2) 危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；
   (3) 损害国家荣誉和利益的；
   (4) 煽动民族仇恨、民族歧视，破坏民族团结的；
   (5) 破坏国家宗教政策，宣扬邪教和封建迷信活动的；
   (6) 散布谣言，扰乱社会秩序，破坏社会稳定的；
   (7) 散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的；
   (8) 侮辱或者诽谤他人，侵害他人合法权益的；
   (9) 含有法律、行政法规禁止的其他内容的。

四、隐私保护

本平台重视对用户隐私的保护，保护隐私是本平台的一项基本政策。您提供的登记资料及本平台保留的有关的若干其他个人资料将受到中国有关隐私的法律和本平台《隐私政策》之规范。

五、免责声明

1. 用户明确同意其使用本平台网络服务所存在的风险将完全由其自己承担。

2. 本平台不担保服务一定能满足用户的要求，也不担保服务不会中断，对服务的及时性、安全性、出错发生都不作担保。

3. 本平台不保证为向用户提供便利而设置的外部链接的准确性和完整性，同时，对于该等外部链接指向的不由本平台实际控制的任何网页上的内容，本平台不承担任何责任。

六、服务变更、中断或终止

如因系统维护或升级的需要而需暂停网络服务，本平台将尽可能事先进行通告。如发生下列任何一种情形，本平台有权随时中断或终止向用户提供本协议项下的网络服务而无需通知用户：
1. 用户提供的个人资料不真实；
2. 用户违反本协议中规定的使用规则；
3. 用户在使用收费网络服务时未按规定向本平台支付相应的服务费。

七、其他

本协议的订立、执行和解释及争议的解决均应适用中国法律。如双方就本协议内容或其执行发生任何争议，双方应尽量友好协商解决；协商不成时，任何一方均可向本平台所在地的人民法院提起诉讼。
''';

const String _privacyPolicy = '''
EcoSorter智能垃圾分类系统（以下简称"我们"）非常重视用户的隐私和个人信息保护。本隐私政策将向您说明我们如何收集、使用、存储和保护您的个人信息，以及您享有的相关权利。

一、我们收集的信息

1. 您主动提供的信息：
   - 注册信息：用户名、邮箱地址
   - 使用服务时提供的信息：垃圾分类记录、积分信息、订单信息等

2. 我们自动收集的信息：
   - 设备信息：设备型号、操作系统版本、唯一设备标识符
   - 位置信息：用于查找附近垃圾桶位置（需您授权）
   - 日志信息：IP地址、访问时间、浏览记录

二、我们如何使用收集的信息

我们收集的信息将用于以下目的：
1. 提供、维护和改进我们的服务
2. 处理您的垃圾分类请求和积分兑换
3. 向您发送服务通知和更新
4. 分析用户行为，改善用户体验
5. 保护服务安全，防止欺诈行为

三、信息共享

我们不会向第三方出售您的个人信息。我们仅在以下情况下与第三方共享您的信息：
1. 获得您的明确同意
2. 与授权合作伙伴共享（如地图服务提供商）
3. 法律法规要求的情形

四、信息安全

我们采取多种安全措施保护您的个人信息：
1. 数据传输采用SSL加密
2. 敏感信息采用加密存储
3. 定期安全评估和漏洞修复
4. 访问权限严格控制

五、您的权利

您对您的个人信息享有以下权利：
1. 访问权：您可以查看我们持有的您的个人信息
2. 更正权：您可以更新和更正您的个人信息
3. 删除权：您可以要求删除您的个人信息
4. 撤回同意：您可以撤回之前给予的同意

六、Cookie和类似技术

我们使用Cookie和类似技术来：
1. 记住您的登录状态
2. 分析网站流量和使用模式
3. 提供个性化内容

您可以通过浏览器设置管理Cookie，但这可能影响某些功能的使用。

七、未成年人保护

我们非常重视对未成年人个人信息的保护。如果您是18岁以下的未成年人，请在监护人的陪同下阅读本政策，并在取得监护人同意后使用我们的服务。

八、隐私政策的更新

我们可能会不时更新本隐私政策。更新后的政策将在应用内公布，建议您定期查阅。如果我们对政策进行重大变更，我们会通过应用内通知或其他方式告知您。

九、联系我们

如果您对本隐私政策有任何疑问、意见或建议，请通过以下方式联系我们：
- 邮箱：support@ecosorter.com
- 客服电话：400-XXX-XXXX

我们将在15个工作日内回复您的请求。
''';
