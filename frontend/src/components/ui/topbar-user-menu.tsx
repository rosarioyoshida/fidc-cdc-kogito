"use client";

import React from "react";
import { Bell, ChevronDown, LogOut } from "lucide-react";
import { AccountSettingsDialog } from "@/components/ui/account-settings-dialog";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
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
    <div className="flex items-center gap-2">
      <ThemeToggle className="bg-surface" />

      <DropdownMenu onOpenChange={(open) => {
        if (open) {
          markNotificationsAsRead();
        }
      }}>
        <DropdownMenuTrigger asChild>
          <button
            type="button"
            className="relative inline-flex h-10 w-10 items-center justify-center rounded-full border border-border bg-surface text-text-subtle transition hover:bg-surface hover:text-text"
            aria-label={unreadCount > 0 ? `${unreadCount} notificacoes pendentes` : "Notificacoes"}
          >
            <Bell className="h-4 w-4" />
            {unreadCount > 0 ? (
              <span className="absolute -right-1 -top-1">
                <Badge variant="danger" className="min-w-5 justify-center px-1.5 py-0 text-[10px] tracking-normal">
                  {unreadCount}
                </Badge>
              </span>
            ) : null}
          </button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end" className="w-80">
          <DropdownMenuLabel>Notificacoes</DropdownMenuLabel>
          <DropdownMenuSeparator />
          {notifications.map((notification) => (
            <div key={notification.id} className="rounded-lg px-2 py-2">
              <div className="flex items-start gap-3">
                <span
                  className={notification.read ? "mt-1 inline-flex h-2.5 w-2.5 rounded-full bg-border" : "mt-1 inline-flex h-2.5 w-2.5 rounded-full bg-danger"}
                />
                <div className="min-w-0 flex-1">
                  <p className="text-sm font-semibold text-text">{notification.title}</p>
                  <p className="mt-1 text-xs leading-5 text-text-subtle">{notification.description}</p>
                </div>
              </div>
            </div>
          ))}
        </DropdownMenuContent>
      </DropdownMenu>

      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <button
            type="button"
            className="inline-flex min-h-10 items-center gap-3 rounded-full border border-border bg-surface px-2 py-1.5 text-left transition hover:bg-surface"
            aria-label={`Menu da conta de ${user.nomeExibicao}`}
          >
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
              <span className="block truncate text-xs text-text-subtle">
                {toProfileLabel(user.perfilAtivo)} · {user.email}
              </span>
            </span>
            <ChevronDown className="h-4 w-4 text-text-subtle" />
          </button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end" className="w-80">
          <DropdownMenuLabel>
            <div className="space-y-1">
              <p className="text-sm font-semibold text-text">{user.nomeExibicao}</p>
              <p className="text-xs font-normal text-text-subtle">{toProfileLabel(user.perfilAtivo)} · {user.email}</p>
            </div>
          </DropdownMenuLabel>
          <DropdownMenuSeparator />
          <DropdownMenuItem className="gap-2 text-text-subtle">
            <Bell className="h-4 w-4" />
            <span>
              {unreadCount > 0
                ? `${unreadCount} notificacao${unreadCount > 1 ? "oes" : ""} requer atencao`
                : "Nenhuma notificacao pendente"}
            </span>
          </DropdownMenuItem>
          <div className="px-1 py-1">
            <AccountSettingsDialog
              user={user}
              triggerLabel="Alterar senha"
              triggerVariant="subtle"
              triggerClassName="w-full justify-start gap-2 px-2"
            />
          </div>
          <DropdownMenuSeparator />
          <div className="px-1 py-1">
            <form action={logoutAction}>
              <Button type="submit" variant="subtle" className="w-full justify-start gap-2 px-2 text-danger hover:text-danger">
                <LogOut className="h-4 w-4" />
                Logout
              </Button>
            </form>
          </div>
        </DropdownMenuContent>
      </DropdownMenu>
    </div>
  );
}
