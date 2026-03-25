"use client";

import React from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/cn";
import { TopbarUserMenu } from "@/components/ui/topbar-user-menu";
import type { CurrentUserAccount } from "@/features/security/user-account-types";
import type { NavigationItem } from "@/features/navigation/shell-config";

type AppHeaderProps = {
  user: CurrentUserAccount;
  navigation: NavigationItem[];
  primaryAction: {
    label: string;
    href: string;
  };
};

export function AppHeader({ user, navigation, primaryAction }: AppHeaderProps) {
  const pathname = usePathname();

  return (
    <header className="sticky top-0 z-30 w-full border-b border-border/80 bg-[var(--ds-shell-header)] backdrop-blur">
      <div className="mx-auto flex max-w-7xl items-center gap-4 px-6 py-4">
        <Link href="/cessoes" className="flex min-w-0 items-center gap-3">
          <span className="inline-flex h-10 w-10 items-center justify-center rounded-2xl bg-brand text-sm font-bold text-white">
            FC
          </span>
          <span className="hidden text-sm font-semibold tracking-[0.16em] text-text sm:inline">
            FIDC CDC KOGITO
          </span>
        </Link>

        <nav className="hidden flex-1 items-center justify-center gap-2 lg:flex">
          {navigation.map((item) => {
            const active = item.exact ? pathname === item.href : pathname.startsWith(item.href);
            return (
              <Link
                key={item.href}
                href={item.href}
                aria-current={active ? "page" : undefined}
                className={cn(
                  "rounded-full px-4 py-2 text-sm font-medium transition",
                  active
                    ? "bg-brand/12 text-brand"
                    : "text-text-subtle hover:bg-surface hover:text-text"
                )}
              >
                {item.label}
              </Link>
            );
          })}
        </nav>

        <div className="ml-auto flex items-center gap-3">
          <Button asChild className="hidden sm:inline-flex">
            <Link href={primaryAction.href}>{primaryAction.label}</Link>
          </Button>
          <TopbarUserMenu user={user} />
        </div>
      </div>
    </header>
  );
}
