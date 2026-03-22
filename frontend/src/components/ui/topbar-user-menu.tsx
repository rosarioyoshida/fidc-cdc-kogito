"use client";

import React from "react";
import { Button } from "@/components/ui/button";
import { AccountSettingsDialog } from "@/components/ui/account-settings-dialog";
import { ThemeToggle } from "@/components/ui/theme-toggle";
import type { CurrentUserAccount } from "@/features/security/user-account-types";
import { logoutAction } from "@/features/security/actions";
import { toProfileLabel } from "@/features/security/user-account-types";

type TopbarUserMenuProps = {
  user: CurrentUserAccount;
};

type NotificationItem = {
  id: string;
  title: string;
  description: string;
  read: boolean;
};

function BellIcon() {
  return (
    <svg viewBox="0 0 24 24" className="h-4 w-4 fill-none stroke-current" strokeWidth="1.8" aria-hidden="true">
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M12 4a4 4 0 0 0-4 4v1.2c0 .8-.24 1.58-.68 2.24L6 13.5v1.5h12v-1.5l-1.32-2.06A4.2 4.2 0 0 1 16 9.2V8a4 4 0 0 0-4-4Z"
      />
      <path strokeLinecap="round" strokeLinejoin="round" d="M10 18a2 2 0 0 0 4 0" />
    </svg>
  );
}

function SettingsIcon() {
  return (
    <svg viewBox="0 0 24 24" className="h-4 w-4 fill-none stroke-current" strokeWidth="1.8" aria-hidden="true">
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M10.3 4.32a1 1 0 0 1 1.4-.64l.77.31a1 1 0 0 0 .76 0l.77-.31a1 1 0 0 1 1.4.64l.37.74a1 1 0 0 0 .57.49l.8.27a1 1 0 0 1 .66 1.38l-.36.75a1 1 0 0 0 0 .78l.36.75a1 1 0 0 1-.66 1.38l-.8.27a1 1 0 0 0-.57.49l-.37.74a1 1 0 0 1-1.4.64l-.77-.31a1 1 0 0 0-.76 0l-.77.31a1 1 0 0 1-1.4-.64l-.37-.74a1 1 0 0 0-.57-.49l-.8-.27a1 1 0 0 1-.66-1.38l.36-.75a1 1 0 0 0 0-.78l-.36-.75a1 1 0 0 1 .66-1.38l.8-.27a1 1 0 0 0 .57-.49l.37-.74Z"
      />
      <circle cx="12" cy="12" r="2.75" />
    </svg>
  );
}

function LogOutIcon() {
  return (
    <svg viewBox="0 0 24 24" className="h-4 w-4 fill-none stroke-current" strokeWidth="1.8" aria-hidden="true">
      <path strokeLinecap="round" strokeLinejoin="round" d="M9 6H5v12h4" />
      <path strokeLinecap="round" strokeLinejoin="round" d="m14 8 4 4-4 4" />
      <path strokeLinecap="round" strokeLinejoin="round" d="M18 12H9" />
    </svg>
  );
}

function ChevronDownIcon() {
  return (
    <svg viewBox="0 0 24 24" className="h-4 w-4 fill-none stroke-current" strokeWidth="1.8" aria-hidden="true">
      <path strokeLinecap="round" strokeLinejoin="round" d="m6 9 6 6 6-6" />
    </svg>
  );
}

export function TopbarUserMenu({ user }: TopbarUserMenuProps) {
  const [notifications, setNotifications] = React.useState<NotificationItem[]>([
    {
      id: "account-security",
      title: "Revisar alertas da conta",
      description: "Existe uma atualizacao pendente para sua atencao no ambiente autenticado.",
      read: false
    }
  ]);
  const unreadCount = notifications.filter((notification) => !notification.read).length;

  function markNotificationsAsRead() {
    setNotifications((current) =>
      current.map((notification) =>
        notification.read ? notification : { ...notification, read: true }
      )
    );
  }

  return (
    <section className="rounded-2xl border bg-surface-raised px-4 py-4 shadow-soft">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
        <div className="min-w-0 flex-1">
          <p className="text-xs font-semibold uppercase tracking-[0.18em] text-text-subtle">
            Usuario autenticado
          </p>
          <div className="mt-1 flex flex-col gap-1 sm:flex-row sm:items-baseline sm:gap-3">
            <h2 className="text-lg font-semibold text-text">{user.nomeExibicao}</h2>
            <p className="text-sm text-text-subtle">{toProfileLabel(user.perfilAtivo)}</p>
          </div>
          <p className="text-sm text-text-subtle">
            {toProfileLabel(user.perfilAtivo)} · {user.email}
          </p>
        </div>

        <div className="flex flex-wrap items-center justify-end gap-3">
          <ThemeToggle className="bg-surface" />

          <details className="group relative" onToggle={(event) => {
            if ((event.currentTarget as HTMLDetailsElement).open) {
              markNotificationsAsRead();
            }
          }}>
            <summary
              className={
                unreadCount > 0
                  ? "relative flex min-h-10 min-w-10 cursor-pointer list-none items-center justify-center rounded-full border border-danger/25 bg-danger/10 px-0 text-danger transition hover:bg-danger/15 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand"
                  : "relative flex min-h-10 min-w-10 cursor-pointer list-none items-center justify-center rounded-full border bg-surface px-0 text-text-subtle transition hover:bg-surface-raised hover:text-text focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand"
              }
              aria-label={
                unreadCount > 0
                  ? `${unreadCount} notificacao${unreadCount > 1 ? "oes" : ""} nao lida${unreadCount > 1 ? "s" : ""}`
                  : "Nenhuma notificacao nao lida"
              }
              title={unreadCount > 0 ? "Abrir notificacoes nao lidas" : "Abrir notificacoes"}
            >
              <BellIcon />
              {unreadCount > 0 ? (
                <span className="absolute right-2 top-2 h-2.5 w-2.5 rounded-full bg-danger ring-2 ring-surface-raised" />
              ) : null}
            </summary>

            <div className="absolute right-0 z-20 mt-3 w-80 rounded-xl border bg-surface-raised p-2 shadow-soft">
              <div className="border-b px-3 py-3">
                <p className="text-sm font-semibold text-text">Notificacoes</p>
                <p className="mt-1 text-xs text-text-subtle">
                  {unreadCount > 0
                    ? `${unreadCount} mensagem${unreadCount > 1 ? "ens" : ""} nao lida${unreadCount > 1 ? "s" : ""}`
                    : "Nenhuma mensagem nao lida"}
                </p>
              </div>

              <div className="grid gap-2 px-1 py-2">
                {notifications.map((notification) => (
                  <div
                    key={notification.id}
                    className="rounded-lg border bg-surface px-3 py-3"
                  >
                    <div className="flex items-start gap-3">
                      <span
                        className={
                          notification.read
                            ? "mt-1 inline-flex h-2.5 w-2.5 rounded-full bg-border"
                            : "mt-1 inline-flex h-2.5 w-2.5 rounded-full bg-danger"
                        }
                      />
                      <div className="min-w-0 flex-1">
                        <p className="text-sm font-semibold text-text">{notification.title}</p>
                        <p className="mt-1 text-xs leading-5 text-text-subtle">{notification.description}</p>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </details>

          <details className="group relative">
            <summary className="flex min-h-10 cursor-pointer list-none items-center gap-3 rounded-md border bg-surface px-3 py-2 text-left transition hover:bg-surface-raised focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand">
              <span className="inline-flex h-8 w-8 items-center justify-center rounded-full bg-brand text-sm font-semibold text-white">
                {user.nomeExibicao
                  .split(" ")
                  .filter(Boolean)
                  .slice(0, 2)
                  .map((part) => part[0]?.toUpperCase())
                  .join("")}
              </span>
              <span className="hidden min-w-0 md:block">
                <span className="block truncate text-sm font-semibold text-text">{user.nomeExibicao}</span>
                <span className="block truncate text-xs text-text-subtle">{user.email}</span>
              </span>
              <span className="text-text-subtle transition group-open:rotate-180">
                <ChevronDownIcon />
              </span>
            </summary>

            <div className="absolute right-0 z-20 mt-3 w-72 rounded-xl border bg-surface-raised p-2 shadow-soft">
              <div className="border-b px-3 py-3">
                <p className="text-sm font-semibold text-text">{user.nomeExibicao}</p>
                <p className="mt-1 text-xs text-text-subtle">{user.email}</p>
              </div>

              <div className="grid gap-1 px-1 py-2">
                <div className="flex items-center gap-2 rounded-md px-2 py-2 text-sm text-text-subtle">
                  <BellIcon />
                  <span>
                    {unreadCount > 0
                      ? `${unreadCount} notificacao${unreadCount > 1 ? "oes" : ""} requer atencao`
                      : "Nenhuma notificacao pendente"}
                  </span>
                </div>

                <div className="flex items-center gap-2 rounded-md px-2 py-1 hover:bg-surface">
                  <span className="text-text-subtle">
                    <SettingsIcon />
                  </span>
                  <AccountSettingsDialog user={user} />
                </div>

                <form action={logoutAction}>
                  <Button type="submit" variant="subtle" className="w-full justify-start gap-2 px-2 text-danger hover:text-danger">
                    <LogOutIcon />
                    Logout
                  </Button>
                </form>
              </div>
            </div>
          </details>
        </div>
      </div>
    </section>
  );
}
