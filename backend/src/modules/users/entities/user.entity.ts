import { Entity, Column, PrimaryGeneratedColumn, CreateDateColumn, UpdateDateColumn } from 'typeorm';
import { ApiProperty } from '@nestjs/swagger';

@Entity('users')
export class User {
  @ApiProperty({ description: '用户ID' })
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @ApiProperty({ description: '用户名' })
  @Column({ unique: true })
  username: string;

  @ApiProperty({ description: '邮箱' })
  @Column({ unique: true })
  email: string;

  @ApiProperty({ description: '密码' })
  @Column()
  password: string;

  @ApiProperty({ description: '手机号', required: false })
  @Column({ nullable: true })
  phone?: string;

  @ApiProperty({ description: '头像URL', required: false })
  @Column({ nullable: true })
  avatar?: string;

  @ApiProperty({ description: '积分' })
  @Column({ default: 0 })
  points: number;

  @ApiProperty({ description: '用户状态' })
  @Column({ default: true })
  isActive: boolean;

  @ApiProperty({ description: '创建时间' })
  @CreateDateColumn()
  createdAt: Date;

  @ApiProperty({ description: '更新时间' })
  @UpdateDateColumn()
  updatedAt: Date;
}