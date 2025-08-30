import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { TypeOrmModuleOptions, TypeOrmOptionsFactory } from '@nestjs/typeorm';

@Injectable()
export class DatabaseConfig implements TypeOrmOptionsFactory {
  constructor(private configService: ConfigService) {}

  createTypeOrmOptions(): TypeOrmModuleOptions {
    return {
      type: 'postgres',
      host: this.configService.get('DB_HOST', 'localhost'),
      port: this.configService.get('DB_PORT', 5432),
      username: this.configService.get('DB_USERNAME', 'postgres'),
      password: this.configService.get('DB_PASSWORD', 'postgres'),
      database: this.configService.get('DB_DATABASE', 'eco_sorter'),
      entities: [__dirname + '/../**/*.entity{.ts,.js}'],
      synchronize: this.configService.get('DB_SYNCHRONIZE', 'true') === 'true',
      logging: this.configService.get('DB_LOGGING', 'false') === 'true',
      migrations: [__dirname + '/../migrations/*{.ts,.js}'],
      migrationsRun: this.configService.get('DB_MIGRATIONS_RUN', 'false') === 'true',
      ssl: this.configService.get('DB_SSL', 'false') === 'true',
    };
  }
}