import React from 'react';
import { cn } from '@/lib/utils';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string
  error?: string
  helperText?: string
  startIcon?: React.ReactNode
  endIcon?: React.ReactNode
  fullWidth?: boolean
}

export const Input = React.forwardRef<HTMLInputElement, InputProps>(
  (
    {
      className,
      label,
      error,
      helperText,
      startIcon,
      endIcon,
      fullWidth = true,
      id,
      ...props
    },
    ref,
  ) => {
    const inputId = id || `input-${Math.random().toString(36).substr(2, 9)}`
    const hasError = !!error

    return (
      <div className={cn('space-y-1', fullWidth && 'w-full')}>
        {label && (
          <label
            htmlFor={inputId}
            className={cn(
              'form-label',
              hasError && 'text-red-600',
            )}
          >
            {label}
          </label>
        )}

        <div className="relative">
          {startIcon && (
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              {startIcon}
            </div>
          )}

          <input
            ref={ref}
            id={inputId}
            className={cn(
              'form-input',
              hasError && 'border-red-300 focus:ring-red-500 focus:border-red-500',
              startIcon && 'pl-10',
              endIcon && 'pr-10',
              props.disabled && 'bg-gray-100 cursor-not-allowed',
              className,
            )}
            {...props}
          />

          {endIcon && (
            <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
              {endIcon}
            </div>
          )}
        </div>

        {hasError && (
          <p className="form-error">{error}</p>
        )}

        {helperText && !hasError && (
          <p className="text-sm text-gray-500">{helperText}</p>
        )}
      </div>
    )
  },
)

Input.displayName = 'Input'

interface TextareaProps {
  className?: string
  label?: string
  error?: string
  helperText?: string
  fullWidth?: boolean
  id?: string
  rows?: number
  disabled?: boolean
  value?: string
  defaultValue?: string
  onChange?: React.ChangeEventHandler<HTMLTextAreaElement>
  onBlur?: React.FocusEventHandler<HTMLTextAreaElement>
  onFocus?: React.FocusEventHandler<HTMLTextAreaElement>
  name?: string
  required?: boolean
  placeholder?: string
}

// Textarea component
export const Textarea = React.forwardRef<
  HTMLTextAreaElement,
  TextareaProps
>(({ className, label, error, helperText, fullWidth = true, id, rows = 3, ...props }, ref) => {
  const textareaId = id || `textarea-${Math.random().toString(36).substr(2, 9)}`
  const hasError = !!error

  return (
    <div className={cn('space-y-1', fullWidth && 'w-full')}>
      {label && (
        <label
          htmlFor={textareaId}
          className={cn(
            'form-label',
            hasError && 'text-red-600',
          )}
        >
          {label}
        </label>
      )}

      <textarea
        ref={ref}
        id={textareaId}
        rows={rows}
        className={cn(
          'form-input resize-vertical min-h-[80px]',
          hasError && 'border-red-300 focus:ring-red-500 focus:border-red-500',
          props.disabled && 'bg-gray-100 cursor-not-allowed',
          className,
        )}
        {...props}
      />

      {hasError && (
        <p className="form-error">{error}</p>
      )}

      {helperText && !hasError && (
        <p className="text-sm text-gray-500">{helperText}</p>
      )}
    </div>
  )
})

Textarea.displayName = 'Textarea'

// Select component
interface SelectProps {
  className?: string
  label?: string
  error?: string
  helperText?: string
  fullWidth?: boolean
  id?: string
  options: Array<{ value: string; label: string; disabled?: boolean }>
  disabled?: boolean
  value?: string
  defaultValue?: string
  onChange?: React.ChangeEventHandler<HTMLSelectElement>
  onBlur?: React.FocusEventHandler<HTMLSelectElement>
  onFocus?: React.FocusEventHandler<HTMLSelectElement>
  name?: string
  required?: boolean
}

export const Select = React.forwardRef<
  HTMLSelectElement,
  SelectProps
>(({ className, label, error, helperText, fullWidth = true, id, options, ...props }, ref) => {
  const selectId = id || `select-${Math.random().toString(36).substr(2, 9)}`
  const hasError = !!error

  return (
    <div className={cn('space-y-1', fullWidth && 'w-full')}>
      {label && (
        <label
          htmlFor={selectId}
          className={cn(
            'form-label',
            hasError && 'text-red-600',
          )}
        >
          {label}
        </label>
      )}

      <select
        ref={ref}
        id={selectId}
        className={cn(
          'form-input',
          hasError && 'border-red-300 focus:ring-red-500 focus:border-red-500',
          props.disabled && 'bg-gray-100 cursor-not-allowed',
          className,
        )}
        {...props}
      >
        {options.map((option) => (
          <option
            key={option.value}
            value={option.value}
            disabled={option.disabled}
          >
            {option.label}
          </option>
        ))}
      </select>

      {hasError && (
        <p className="form-error">{error}</p>
      )}

      {helperText && !hasError && (
        <p className="text-sm text-gray-500">{helperText}</p>
      )}
    </div>
  )
})

Select.displayName = 'Select'

// Form group component
export const FormGroup: React.FC<{
  children: React.ReactNode
  className?: string
}> = ({ children, className }) => {
  return (
    <div className={cn('space-y-4', className)}>
      {children}
    </div>
  )
}