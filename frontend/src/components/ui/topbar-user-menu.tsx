"use client";

import React from "react";
import { Button } from "@/components/ui/button";
import { AccountSettingsDialog } from "@/components/ui/account-settings-dialog";
import type { CurrentUserAccount } from "@/features/security/user-account-types";
import { logoutAction } from "@/features/security/actions";
import { toProfileLabel } from "@/features/security/user-account-types";

type TopbarUserMenuProps = {
  user: CurrentUserAccount;
};

export function TopbarUserMenu({ user }: TopbarUserMenuProps) {
  return (
    <section className="flex flex-col gap-3 rounded-lg border bg-surface-raised p-4 shadow-soft md:flex-row md:items-center md:justify-between">
      <div>
        <p className="text-xs font-semibold uppercase tracking-[0.18em] text-text-subtle">
          Usuario autenticado
        </p>
        <h2 className="text-lg font-semibold text-text">{user.nomeExibicao}</h2>
        <p className="text-sm text-text-subtle">
          {toProfileLabel(user.perfilAtivo)} · {user.email}
        </p>
      </div>

      <div className="flex flex-wrap items-center gap-3">
        <AccountSettingsDialog user={user} />
        <form action={logoutAction}>
          <Button type="submit" variant="subtle">
            Logout
          </Button>
        </form>
      </div>
    </section>
  );
}
