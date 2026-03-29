import * as React from "react";
import { cva, type VariantProps } from "class-variance-authority";
import { cn } from "@/lib/cn";

const alertVariants = cva("relative w-full rounded-lg border px-4 py-3 text-sm shadow-sm", {
  variants: {
    variant: {
      default: "border-border bg-surface-raised text-text",
      info: "border-info/25 bg-info/10 text-text",
      success: "border-success/25 bg-success/10 text-text",
      warning: "border-warning/25 bg-warning/10 text-text",
      danger: "border-danger/25 bg-danger/10 text-text"
    }
  },
  defaultVariants: {
    variant: "default"
  }
});

const Alert = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement> & VariantProps<typeof alertVariants>
>(function Alert({ className, variant, ...props }, ref) {
  return (
    <div
      ref={ref}
      role="alert"
      data-feedback-variant={variant ?? "default"}
      className={cn(alertVariants({ variant }), className)}
      {...props}
    />
  );
});

const AlertTitle = React.forwardRef<HTMLParagraphElement, React.HTMLAttributes<HTMLHeadingElement>>(
  function AlertTitle({ className, ...props }, ref) {
    return <h5 ref={ref} className={cn("mb-1 font-semibold leading-none tracking-tight", className)} {...props} />;
  }
);

const AlertDescription = React.forwardRef<HTMLDivElement, React.HTMLAttributes<HTMLDivElement>>(
  function AlertDescription({ className, ...props }, ref) {
    return <div ref={ref} className={cn("text-sm leading-6 text-text-subtle [&_p]:leading-6", className)} {...props} />;
  }
);

export { Alert, AlertDescription, AlertTitle };
