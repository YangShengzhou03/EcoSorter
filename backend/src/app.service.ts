import { Injectable } from '@nestjs/common';

@Injectable()
export class AppService {
  getHealth(): { status: string; timestamp: string } {
    return {
      status: 'OK',
      timestamp: new Date().toISOString(),
    };
  }

  getVersion(): { version: string; name: string } {
    return {
      version: '1.0.0',
      name: 'EcoSorter Backend API',
    };
  }
}