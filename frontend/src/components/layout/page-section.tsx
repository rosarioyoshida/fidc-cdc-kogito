import React from "react";
import type { ReactNode } from "react";
import { cn } from "@/lib/cn";

type PageSectionProps = {
  title: string;
  description?: string;
  children: ReactNode;
  className?: string;
  actions?: ReactNode;
};

export function PageSection({ title, description, children, className, actions }: PageSectionProps) {
  return (
    <section className={cn("grid gap-4", className)}>
      <div className="flex flex-col gap-3 md:flex-row md:items-end md:justify-between">
        <div>
          <h2 className="text-xl font-semibold text-text">{title}</h2>
          {description ? <p className="mt-1 text-sm leading-6 text-text-subtle">{description}</p> : null}
        </div>
        {actions ? <div className="flex flex-wrap items-center gap-2">{actions}</div> : null}
      </div>
      {children}
    </section>
  );
}
