import { ButtonHTMLAttributes, forwardRef } from "react";
import { cn } from "@/lib/cn";

type ButtonVariant = "primary" | "secondary" | "subtle";

type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: ButtonVariant;
};

const variants: Record<ButtonVariant, string> = {
  primary: "bg-brand text-white hover:opacity-90",
  secondary: "border bg-surface-raised text-text hover:bg-surface",
  subtle: "bg-transparent text-text-subtle hover:text-text"
};

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(function Button(
  { className, variant = "primary", ...props },
  ref
) {
  return (
    <button
      ref={ref}
      className={cn(
        "inline-flex min-h-10 items-center justify-center rounded-md px-4 py-2 text-sm font-semibold transition focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand disabled:cursor-not-allowed disabled:opacity-60",
        variants[variant],
        className
      )}
      {...props}
    />
  );
});
