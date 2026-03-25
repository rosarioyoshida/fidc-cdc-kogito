import React from "react";

type EmptyStateProps = {
  title: string;
  description: string;
};

export function EmptyState({ title, description }: EmptyStateProps) {
  return (
    <section className="rounded-[28px] border border-dashed border-border bg-surface p-10 text-center shadow-soft">
      <p className="mb-3 text-xs font-semibold uppercase tracking-[0.18em] text-text-subtle">
        Estado vazio
      </p>
      <h2 className="mb-3 text-2xl font-semibold text-text">{title}</h2>
      <p className="mx-auto max-w-xl text-sm leading-6 text-text-subtle">{description}</p>
    </section>
  );
}
