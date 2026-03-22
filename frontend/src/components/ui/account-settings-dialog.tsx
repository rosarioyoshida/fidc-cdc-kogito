"use client";

import React from "react";
import { startTransition, useActionState, useEffect, useRef, useState } from "react";
import { usePathname, useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Dialog } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import type { CurrentUserAccount } from "@/features/security/user-account-types";
import {
  changeOwnPasswordAction,
  updateOwnEmailAction
} from "@/features/security/actions";
import { INITIAL_ACCOUNT_ACTION_STATE } from "@/features/security/account-action-state";

type AccountSettingsDialogProps = {
  user: CurrentUserAccount;
};

export function AccountSettingsDialog({ user }: AccountSettingsDialogProps) {
  const [open, setOpen] = useState(false);
  const [account, setAccount] = useState(user);
  const [emailState, emailAction, emailPending] = useActionState(
    updateOwnEmailAction,
    INITIAL_ACCOUNT_ACTION_STATE
  );
  const [passwordState, passwordAction, passwordPending] = useActionState(
    changeOwnPasswordAction,
    INITIAL_ACCOUNT_ACTION_STATE
  );
  const pathname = usePathname();
  const router = useRouter();
  const passwordFormRef = useRef<HTMLFormElement>(null);
  const handledEmailState = useRef<string | undefined>(undefined);
  const handledPasswordState = useRef<string | undefined>(undefined);

  useEffect(() => {
    setAccount(user);
  }, [user]);

  useEffect(() => {
    if (emailState.status !== "success" || emailState.message === handledEmailState.current) {
      return;
    }

    handledEmailState.current = emailState.message;
    if (emailState.updatedEmail) {
      setAccount((current) => ({ ...current, email: emailState.updatedEmail! }));
    }

    startTransition(() => {
      router.refresh();
    });
  }, [emailState, router]);

  useEffect(() => {
    if (passwordState.status !== "success" || passwordState.message === handledPasswordState.current) {
      return;
    }

    handledPasswordState.current = passwordState.message;
    passwordFormRef.current?.reset();
    startTransition(() => {
      router.refresh();
    });
  }, [passwordState, router]);

  return (
    <>
      <Button type="button" variant="secondary" onClick={() => setOpen(true)}>
        Ajustes da conta
      </Button>

      <Dialog
        title="Ajustes da conta"
        open={open}
        onClose={() => setOpen(false)}
        closeLabel="Fechar ajustes da conta"
      >
        <div className="grid gap-6">
          <div className="rounded-lg border bg-surface p-4">
            <p className="text-sm font-semibold text-text">{account.nomeExibicao}</p>
            <p className="mt-1 text-sm text-text-subtle">{account.email}</p>
            <p className="mt-2 text-xs uppercase tracking-[0.18em] text-text-subtle">
              Perfil ativo: {account.perfilAtivo}
            </p>
          </div>

          <form action={emailAction} className="grid gap-3 rounded-lg border bg-surface p-4">
            <input type="hidden" name="currentPath" value={pathname} />
            <label className="grid gap-2 text-sm font-medium text-text">
              Alterar email
              <Input name="email" type="email" defaultValue={account.email} required />
            </label>
            {emailState.message ? (
              <p
                className={emailState.status === "error" ? "text-sm text-danger" : "text-sm text-success"}
                role="status"
              >
                {emailState.message}
              </p>
            ) : null}
            <div className="flex justify-end">
              <Button type="submit" disabled={emailPending}>
                {emailPending ? "Salvando email..." : "Salvar email"}
              </Button>
            </div>
          </form>

          <form
            ref={passwordFormRef}
            action={passwordAction}
            className="grid gap-3 rounded-lg border bg-surface p-4"
          >
            <input type="hidden" name="currentPath" value={pathname} />
            <label className="grid gap-2 text-sm font-medium text-text">
              Senha atual
              <Input name="currentPassword" type="password" autoComplete="current-password" required />
            </label>
            <label className="grid gap-2 text-sm font-medium text-text">
              Nova senha
              <Input name="newPassword" type="password" autoComplete="new-password" required />
            </label>
            {passwordState.message ? (
              <p
                className={passwordState.status === "error" ? "text-sm text-danger" : "text-sm text-success"}
                role="status"
              >
                {passwordState.message}
              </p>
            ) : null}
            <div className="flex justify-end">
              <Button type="submit" disabled={passwordPending}>
                {passwordPending ? "Alterando senha..." : "Alterar senha"}
              </Button>
            </div>
          </form>
        </div>
      </Dialog>
    </>
  );
}
