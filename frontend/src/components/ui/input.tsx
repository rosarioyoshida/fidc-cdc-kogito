import React, { forwardRef, InputHTMLAttributes } from "react";
import { cn } from "@/lib/cn";

export const Input = forwardRef<HTMLInputElement, InputHTMLAttributes<HTMLInputElement>>(
  function Input({ className, ...props }, ref) {
    return (
      <input
        ref={ref}
        className={cn(
          "min-h-10 w-full rounded-md border bg-surface px-3 py-2 text-sm text-text shadow-sm outline-none transition placeholder:text-text-subtle focus-visible:ring-2 focus-visible:ring-brand",
          className
        )}
        {...props}
      />
    );
  }
);
