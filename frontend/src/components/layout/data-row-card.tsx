import React from "react";
import type { ReactNode } from "react";
import { cn } from "@/lib/cn";

type DataRowCardProps = {
  leading?: ReactNode;
  primary: ReactNode;
  secondary?: ReactNode;
  columns?: ReactNode[];
  primaryAction?: ReactNode;
  secondaryActions?: ReactNode;
  className?: string;
};

export function DataRowCard({
  leading,
  primary,
  secondary,
  columns = [],
  primaryAction,
  secondaryActions,
  className
}: DataRowCardProps) {
  return (
    <article className={cn("rounded-3xl border border-border/80 bg-surface-raised p-5 shadow-soft", className)}>
      <div className="grid gap-4 xl:grid-cols-[minmax(0,0.85fr)_minmax(0,1.5fr)_repeat(3,minmax(0,0.9fr))_auto_auto] xl:items-center">
        <div className="flex items-center gap-3">
          {leading}
          <div className="min-w-0">
            <div className="truncate text-sm font-semibold text-text">{primary}</div>
            {secondary ? <div className="mt-1 text-sm text-text-subtle">{secondary}</div> : null}
          </div>
        </div>

        {columns.map((column, index) => (
          <div key={index} className="text-sm text-text-subtle">
            {column}
          </div>
        ))}

        <div className="flex items-center justify-start xl:justify-end">{primaryAction}</div>
        <div className="flex items-center justify-start gap-2 xl:justify-end">{secondaryActions}</div>
      </div>
    </article>
  );
}
