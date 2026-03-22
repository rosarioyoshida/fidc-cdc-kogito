import React, { ReactNode } from "react";
import { cn } from "@/lib/cn";

type DialogProps = {
  title: string;
  open?: boolean;
  children: ReactNode;
  className?: string;
  onClose?: () => void;
  closeLabel?: string;
};

export function Dialog({
  title,
  open = true,
  children,
  className,
  onClose,
  closeLabel = "Fechar janela"
}: DialogProps) {
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
        <div className="mb-4 flex items-start justify-between gap-4">
          <h2 id="dialog-title" className="text-xl font-semibold">{title}</h2>
          {onClose ? (
            <button
              type="button"
              aria-label={closeLabel}
              className="rounded-md border px-3 py-1 text-sm font-medium text-text transition hover:bg-surface"
              onClick={onClose}
            >
              Fechar
            </button>
          ) : null}
        </div>
        {children}
      </section>
    </div>
  );
}
