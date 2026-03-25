"use client";

import React from "react";
import type { ReactNode } from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/cn";
import type { SecondaryNavigationConfig } from "@/features/navigation/shell-config";

type SecondaryNavProps = {
  config: SecondaryNavigationConfig;
  controls?: ReactNode;
};

export function SecondaryNav({ config, controls }: SecondaryNavProps) {
  const pathname = usePathname();

  return (
    <div className="w-full border-b border-border/70 bg-surface">
      <div className="mx-auto flex max-w-7xl flex-col gap-4 px-6 py-3 lg:flex-row lg:items-center lg:justify-between">
        <nav className="flex flex-wrap items-center gap-2">
          {config.items.map((item) => {
            const active = item.exact ? pathname === item.href : pathname.startsWith(item.href);
            return (
              <Link
                key={item.href}
                href={item.href}
                aria-current={active ? "page" : undefined}
                className={cn(
                  "rounded-full border px-3 py-2 text-sm font-medium transition",
                  active
                    ? "border-brand/20 bg-brand/10 text-brand shadow-sm"
                    : "border-transparent text-text-subtle hover:border-border hover:bg-surface-raised hover:text-text"
                )}
              >
                {item.label}
              </Link>
            );
          })}
        </nav>

        {controls ? <div className="flex flex-wrap items-center gap-2">{controls}</div> : null}
      </div>
    </div>
  );
}
