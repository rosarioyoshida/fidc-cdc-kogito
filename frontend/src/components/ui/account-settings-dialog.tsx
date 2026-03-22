"use client";

import React from "react";
import { useState } from "react";
import { usePathname } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Dialog } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import type { CurrentUserAccount } from "@/features/security/user-account-types";
import { changeOwnPasswordAction, updateOwnEmailAction } from "@/features/security/actions";

type AccountSettingsDialogProps = {
  user: CurrentUserAccount;
};

export function AccountSettingsDialog({ user }: AccountSettingsDialogProps) {
  const [open, setOpen] = useState(false);
  const pathname = usePathname();

  return (
    <>
      <Button type="button" variant="secondary" onClick={() => setOpen(true)}>
        Ajustes da conta
      </Button>

      <Dialog title="Ajustes da conta" open={open}>
        <div className="grid gap-6">
          <div className="rounded-lg border bg-surface p-4">
            <p className="text-sm font-semibold text-text">{user.nomeExibicao}</p>
            <p className="mt-1 text-sm text-text-subtle">{user.email}</p>
            <p className="mt-2 text-xs uppercase tracking-[0.18em] text-text-subtle">
              Perfil ativo: {user.perfilAtivo}
            </p>
          </div>

          <form action={updateOwnEmailAction} className="grid gap-3 rounded-lg border bg-surface p-4">
            <input type="hidden" name="redirectTo" value={pathname} />
            <label className="grid gap-2 text-sm font-medium text-text">
              Alterar email
              <Input name="email" type="email" defaultValue={user.email} required />
            </label>
            <div className="flex justify-end">
              <Button type="submit">Salvar email</Button>
            </div>
          </form>

          <form action={changeOwnPasswordAction} className="grid gap-3 rounded-lg border bg-surface p-4">
            <input type="hidden" name="redirectTo" value={pathname} />
            <label className="grid gap-2 text-sm font-medium text-text">
              Senha atual
              <Input name="currentPassword" type="password" autoComplete="current-password" required />
            </label>
            <label className="grid gap-2 text-sm font-medium text-text">
              Nova senha
              <Input name="newPassword" type="password" autoComplete="new-password" required />
            </label>
            <div className="flex items-center justify-between gap-3">
              <Button type="button" variant="subtle" onClick={() => setOpen(false)}>
                Fechar
              </Button>
              <Button type="submit">Alterar senha</Button>
            </div>
          </form>
        </div>
      </Dialog>
    </>
  );
}
