import 'package:flutter/material.dart';
import 'package:ecosorter/theme/app_theme.dart';

class UserAgreementScreen extends StatelessWidget {
  const UserAgreementScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.backgroundColor,
      appBar: AppBar(
        title: const Text('用户协议'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildSection(
              '一、服务条款的接受',
              '欢迎使用ECO-Sorter智能垃圾分类督导系统（以下简称"本系统"）。本用户协议（以下简称"本协议"）是您与本系统运营方之间关于使用本系统所订立的协议。请您在注册成为本系统用户前，仔细阅读本协议，特别是免除或限制责任的条款。如您不同意本协议的任意内容，请勿注册或使用本系统服务。如您注册成为本系统用户，即表示您已充分阅读、理解并同意接受本协议的全部内容。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '二、服务内容',
              '本系统提供智能垃圾分类、回收预约、积分兑换等服务。我们保留随时修改或中断服务而不需通知用户的权利。我们保留随时对本协议条款进行修改的权利，修改后的协议一旦公布即代替原协议。如您不同意修改后的协议，请停止使用本系统服务。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '三、用户账户',
              '3.1 注册账户\n\n用户在注册时需要提供真实、准确、完整的个人信息。用户对账户的安全负全部责任，因保管不善造成的损失由用户自行承担。\n\n3.2 账户注销\n\n用户可以随时申请注销账户。注销后，您将无法使用本系统服务，且您的个人信息将按照隐私政策的规定进行处理。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '四、用户行为规范',
              '4.1 禁止行为\n\n用户在使用本系统时，不得从事以下行为：\n• 发布违法、有害、虚假、骚扰性信息\n• 侵犯他人知识产权、隐私权等合法权益\n• 利用本系统进行欺诈、诈骗等违法行为\n• 其他违反法律法规或本协议的行为\n\n4.2 违规处理\n\n如您违反本协议，我们有权根据情节轻重采取以下措施：警告、限制功能、暂停服务、终止账户、追究法律责任。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '五、隐私保护',
              '我们重视您的隐私保护。关于我们如何收集、使用、存储和保护您的个人信息，请参阅我们的《隐私政策》。使用本系统即表示您同意我们按照隐私政策处理您的个人信息。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '六、知识产权',
              '本系统中的所有内容，包括但不限于文字、图片、音频、视频、软件、程序、版面设计等，均受著作权法、商标法、专利法及其他法律保护。未经本系统运营方书面许可，任何人不得以任何形式使用或传播。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '七、免责声明',
              '7.1 本系统可能包含第三方网站的链接。我们不对这些第三方网站的内容、隐私政策或做法负责。访问第三方网站的风险由您自行承担。\n\n7.2 因不可抗力、网络故障、系统维护等原因导致的服务中断或数据丢失，我们不承担责任。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '八、积分规则',
              '8.1 积分获取\n\n用户通过正确分类投放垃圾、参与活动等方式获取积分。具体积分规则以系统公告为准。\n\n8.2 积分使用\n\n用户可使用积分兑换礼品或服务。积分兑换后不可撤销。\n\n8.3 积分清零\n\n如用户违反本协议或存在作弊行为，我们有权取消或清零其积分。如用户账户注销超过一年未重新激活，积分可能被清零。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '九、协议的修改与终止',
              '9.1 协议修改\n\n我们有权根据需要随时修改本协议。修改后的协议将在本系统上公布，自公布之日起生效。如您继续使用本系统服务，即视为您接受修改后的协议。\n\n9.2 协议终止\n\n如出现以下情况，我们有权终止本协议：\n• 您严重违反本协议\n• 您提供虚假信息\n• 您的行为损害本系统或其他用户的利益\n\n协议终止后，您的账户将被注销，相关权利和义务同时终止。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '十、争议解决',
              '10.1 适用法律\n\n本协议的订立、执行、解释及争议解决均适用中华人民共和国法律。\n\n10.2 争议处理\n\n因本协议引起的任何争议，双方应友好协商解决。协商不成的，任何一方可向本系统所在地有管辖权的人民法院提起诉讼。',
            ),
            const SizedBox(height: 16),
            _buildSection(
              '十一、联系我们',
              '如您对本用户协议有任何疑问、意见或建议，通过以下方式与我们联系：\n\n• 邮箱：support@ecosorter.com\n• 电话：400-XXX-XXXX\n• 地址：江西省南昌市高新区XX路XX号',
            ),
            const SizedBox(height: 32),
          ],
        ),
      ),
    );
  }

  Widget _buildSection(String title, String content) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.04),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            title,
            style: const TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.w600,
              color: AppTheme.textPrimary,
            ),
          ),
          const SizedBox(height: 12),
          Text(
            content,
            style: TextStyle(
              fontSize: 14,
              color: Colors.grey[600],
              height: 1.7,
            ),
          ),
        ],
      ),
    );
  }
}
