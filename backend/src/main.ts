import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { AppModule } from './app.module';
import { setupSwagger } from './config/swagger';
import { ConfigService } from '@nestjs/config';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  const configService = app.get(ConfigService);

  // 全局验证管道
  app.useGlobalPipes(new ValidationPipe({
    whitelist: true,
    forbidNonWhitelisted: true,
    transform: true,
  }));

  // 启用CORS
  app.enableCors({
    origin: configService.get('CORS_ORIGIN', '*'),
    credentials: true,
  });

  // 设置Swagger文档
  if (configService.get('NODE_ENV') !== 'production') {
    setupSwagger(app);
  }

  const port = configService.get('PORT', 3000);
  await app.listen(port);
  
  console.log(`🚀 Server running on http://localhost:${port}`);
  if (configService.get('NODE_ENV') !== 'production') {
    console.log(`📚 API Documentation: http://localhost:${port}/api`);
  }
}

bootstrap();