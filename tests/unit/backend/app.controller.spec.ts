import { Test, TestingModule } from '@nestjs/testing';
import { AppController } from '../../../../backend/src/app.controller';
import { AppService } from '../../../../backend/src/app.service';

describe('AppController', () => {
  let appController: AppController;

  beforeEach(async () => {
    const app: TestingModule = await Test.createTestingModule({
      controllers: [AppController],
      providers: [AppService],
    }).compile();

    appController = app.get<AppController>(AppController);
  });

  describe('root', () => {
    it('should return health status', () => {
      const result = appController.getHealth();
      expect(result.status).toBe('OK');
      expect(result.timestamp).toBeDefined();
    });

    it('should return version info', () => {
      const result = appController.getVersion();
      expect(result.version).toBe('1.0.0');
      expect(result.name).toBe('EcoSorter Backend API');
    });
  });
});