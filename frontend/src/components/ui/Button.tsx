import React from 'react';
import { cn } from '@/lib/utils';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'danger' | 'success' | 'outline' | 'ghost';
  size?: 'sm' | 'md' | 'lg';
  loading?: boolean;
  children: React.ReactNode;
}

const variantClasses = {
  primary: 'btn-primary',
  secondary: 'btn-secondary',
  danger: 'btn-danger',
  success: 'btn-success',
  outline: 'border border-gray-300 bg-transparent text-gray-700 hover:bg-gray-50',
  ghost: 'bg-transparent text-gray-700 hover:bg-gray-100',
};

const sizeClasses = {
  sm: 'px-3 py-1.5 text-sm',
  md: 'px-4 py-2 text-sm',
  lg: 'px-6 py-3 text-base',
};

export const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  (
    {
      className,
      variant = 'primary',
      size = 'md',
      loading = false,
      disabled,
      children,
      ...props
    },
    ref,
  ) => {
    const isDisabled = disabled || loading

    return (
      <button
        ref={ref}
        className={cn(
          'btn',
          variantClasses[variant],
          sizeClasses[size],
          isDisabled && 'opacity-50 cursor-not-allowed',
          loading && 'relative text-transparent',
          className,
        )}
        disabled={isDisabled}
        {...props}
      >
        {loading && (
          <div className="absolute inset-0 flex items-center justify-center">
            <div className="w-4 h-4 border-2 border-current border-t-transparent rounded-full animate-spin" />
          </div>
        )}
        {children}
      </button>
    )
  },
)

Button.displayName = 'Button'

// Icon button variant
export const IconButton = React.forwardRef<
  HTMLButtonElement,
  Omit<ButtonProps, 'children'> & {
    icon: React.ReactNode
    label: string
  }
>(({ icon, label, size = 'md', className, ...props }, ref) => {
  const sizeClasses = {
    sm: 'p-1.5',
    md: 'p-2',
    lg: 'p-3',
  }

  return (
    <Button
      ref={ref}
      size={size}
      className={cn('rounded-full', sizeClasses[size], className)}
      aria-label={label}
      {...props}
    >
      {icon}
    </Button>
  )
})

IconButton.displayName = 'IconButton'

// Button group component
export const ButtonGroup: React.FC<{
  children: React.ReactNode
  className?: string
}> = ({ children, className }) => {
  return (
    <div className={cn('flex space-x-2', className)}>
      {React.Children.map(children, (child, index) => {
        if (React.isValidElement<React.HTMLAttributes<HTMLElement>>(child) && 'className' in child.props) {
          return React.cloneElement<React.HTMLAttributes<HTMLElement>>(child, {
            className: cn(
              child.props.className,
              index > 0 && 'rounded-l-none',
              index < React.Children.count(children) - 1 && 'rounded-r-none'
            ),
          })
        }
        return child
      })}
    </div>
  )
}