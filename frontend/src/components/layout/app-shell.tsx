import React from "react";
import type { ReactNode } from "react";
import { AppFooter } from "@/components/layout/app-footer";
import { AppHeader } from "@/components/layout/app-header";
import { SecondaryNav } from "@/components/layout/secondary-nav";
import type { CurrentUserAccount } from "@/features/security/user-account-types";
import type { NavigationItem, SecondaryNavigationConfig } from "@/features/navigation/shell-config";

type AppShellProps = {
  user: CurrentUserAccount;
  navigation: NavigationItem[];
  primaryAction: {
    label: string;
    href: string;
  };
  secondaryNav?: SecondaryNavigationConfig;
  secondaryControls?: ReactNode;
  children: ReactNode;
};

export function AppShell({
  user,
  navigation,
  primaryAction,
  secondaryNav,
  secondaryControls,
  children
}: AppShellProps) {
  return (
    <div className="min-h-screen">
      <AppHeader user={user} navigation={navigation} primaryAction={primaryAction} />
      {secondaryNav ? <SecondaryNav config={secondaryNav} controls={secondaryControls} /> : null}
      <main className="mx-auto flex w-full max-w-7xl flex-1 flex-col gap-8 px-6 py-8">{children}</main>
      <AppFooter />
    </div>
  );
}
