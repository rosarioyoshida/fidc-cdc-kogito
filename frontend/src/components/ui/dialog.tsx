import React, { ReactNode } from "react";
import { cn } from "@/lib/cn";

type DialogProps = {
  title: string;
  open?: boolean;
  children: ReactNode;
  className?: string;
};

export function Dialog({ title, open = true, children, className }: DialogProps) {
  if (!open) {
    return null;
  }

  return (
    <div className="fixed inset-0 z-50 grid place-items-center bg-slate-950/40 p-6">
      <section
        role="dialog"
        aria-modal="true"
        aria-labelledby="dialog-title"
        className={cn("w-full max-w-2xl rounded-lg border bg-surface-raised p-6 shadow-soft", className)}
      >
        <h2 id="dialog-title" className="mb-4 text-xl font-semibold">{title}</h2>
        {children}
      </section>
    </div>
  );
}
