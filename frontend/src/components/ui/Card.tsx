import React from 'react'
import { cn } from '@/lib/utils'

interface CardProps extends React.HTMLAttributes<HTMLDivElement> {
  variant?: 'default' | 'outline' | 'filled'
}

const Card = React.forwardRef<HTMLDivElement, CardProps>(
  ({ className, variant = 'default', ...props }, ref) => {
    const variantClasses = {
      default: 'card',
      outline: 'border border-gray-200 bg-transparent',
      filled: 'bg-gray-50 border-0',
    }

    return (
      <div
        ref={ref}
        className={cn(variantClasses[variant], className)}
        {...props}
      />
    )
  },
)

Card.displayName = 'Card'

const CardHeader = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement>
>(({ className, ...props }, ref) => (
  <div
    ref={ref}
    className={cn('card-header', className)}
    {...props}
  />
))

CardHeader.displayName = 'CardHeader'

const CardTitle = React.forwardRef<
  HTMLParagraphElement,
  React.HTMLAttributes<HTMLHeadingElement>
>(({ className, ...props }, ref) => (
  <h3
    ref={ref}
    className={cn('text-lg font-semibold text-gray-900', className)}
    {...props}
  />
))

CardTitle.displayName = 'CardTitle'

const CardDescription = React.forwardRef<
  HTMLParagraphElement,
  React.HTMLAttributes<HTMLParagraphElement>
>(({ className, ...props }, ref) => (
  <p
    ref={ref}
    className={cn('text-sm text-gray-600', className)}
    {...props}
  />
))

CardDescription.displayName = 'CardDescription'

const CardContent = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement>
>(({ className, ...props }, ref) => (
  <div
    ref={ref}
    className={cn('card-body', className)}
    {...props}
  />
))

CardContent.displayName = 'CardContent'

const CardFooter = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement>
>(({ className, ...props }, ref) => (
  <div
    ref={ref}
    className={cn('flex items-center p-6 pt-0', className)}
    {...props}
  />
))

CardFooter.displayName = 'CardFooter'

// Card with image
interface CardWithImageProps extends CardProps {
  imageUrl: string
  imageAlt?: string
  imageClassName?: string
}

const CardWithImage = React.forwardRef<HTMLDivElement, CardWithImageProps>(
  ({ className, imageUrl, imageAlt, imageClassName, children, ...props }, ref) => (
    <Card ref={ref} className={cn('overflow-hidden', className)} {...props}>
      <img
        src={imageUrl}
        alt={imageAlt || 'Card image'}
        className={cn('w-full h-48 object-cover', imageClassName)}
      />
      {children}
    </Card>
  ),
)

CardWithImage.displayName = 'CardWithImage'

// Card with action
interface CardWithActionProps extends CardProps {
  action?: React.ReactNode
}

const CardWithAction = React.forwardRef<HTMLDivElement, CardWithActionProps>(
  ({ className, action, children, ...props }, ref) => (
    <Card ref={ref} className={cn('relative', className)} {...props}>
      {children}
      {action && (
        <div className="absolute top-4 right-4">
          {action}
        </div>
      )}
    </Card>
  ),
)

CardWithAction.displayName = 'CardWithAction'

// Stats card
interface StatsCardProps {
  title: string
  value: string | number
  icon: React.ReactNode
  trend?: {
    value: number
    isPositive: boolean
  }
  subtitle?: string
  className?: string
}

const StatsCard: React.FC<StatsCardProps> = ({
  title,
  value,
  icon,
  trend,
  subtitle,
  className,
}) => {
  return (
    <Card className={cn('p-6', className)}>
      <div className="flex items-center justify-between">
        <div className="flex-1">
          <p className="text-sm font-medium text-gray-600 truncate">
            {title}
          </p>
          <p className="mt-1 text-2xl font-semibold text-gray-900">
            {value}
          </p>
          {trend && (
            <p className={cn(
              'mt-1 text-sm',
              trend.isPositive ? 'text-green-600' : 'text-red-600'
            )}>
              {trend.isPositive ? '↑' : '↓'} {Math.abs(trend.value)}%
            </p>
          )}
          {subtitle && (
            <p className="mt-1 text-sm text-gray-500">
              {subtitle}
            </p>
          )}
        </div>
        <div className="flex-shrink-0">
          {icon}
        </div>
      </div>
    </Card>
  )
}

export {
  Card,
  CardHeader,
  CardFooter,
  CardTitle,
  CardDescription,
  CardContent,
  CardWithImage,
  CardWithAction,
  StatsCard,
}