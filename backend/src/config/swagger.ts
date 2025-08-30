import { INestApplication } from '@nestjs/common';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';

export function setupSwagger(app: INestApplication): void {
  const config = new DocumentBuilder()
    .setTitle('EcoSorter API')
    .setDescription('智能垃圾分类督导系统 API 文档')
    .setVersion('1.0.0')
    .addBearerAuth({
      type: 'http',
      scheme: 'bearer',
      bearerFormat: 'JWT',
      name: 'JWT',
      description: '输入 JWT token',
      in: 'header',
    })
    .addTag('auth', '身份认证')
    .addTag('users', '用户管理')
    .addTag('garbage', '垃圾分类')
    .addTag('iot', '物联网设备')
    .addTag('rewards', '积分奖励')
    .addTag('statistics', '数据统计')
    .build();

  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('api', app, document, {
    swaggerOptions: {
      persistAuthorization: true,
      tagsSorter: 'alpha',
      operationsSorter: 'alpha',
    },
  });
}