import React from "react";

type ForbiddenStateProps = {
  title: string;
  description: string;
  compact?: boolean;
};

export function ForbiddenState({
  title,
  description,
  compact = false
}: ForbiddenStateProps) {
  return (
    <section
      className={[
        "rounded-[24px] border border-danger/40 bg-danger/10 text-text shadow-soft",
        compact ? "px-3 py-2" : "p-6"
      ].join(" ")}
    >
      <h2 className={compact ? "text-sm font-semibold" : "text-lg font-semibold"}>{title}</h2>
      <p
        className={[
          "text-text-subtle",
          compact ? "mt-1 text-xs leading-5" : "mt-2 text-sm leading-6"
        ].join(" ")}
      >
        {description}
      </p>
    </section>
  );
}
