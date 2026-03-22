"use client";

import React from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

type LoginPanelProps = {
  errorMessage?: string;
  lastUsername?: string;
  signInAction: (formData: FormData) => void;
};

export function LoginPanel({
  errorMessage,
  lastUsername,
  signInAction
}: LoginPanelProps) {
  return (
    <main className="grid min-h-[70vh] place-items-center">
      <section className="w-full max-w-md rounded-lg border bg-surface-raised p-8 shadow-soft">
        <p className="mb-3 text-sm font-semibold uppercase tracking-[0.18em] text-text-subtle">
          Controle de Acesso
        </p>
        <h1 className="mb-3 text-3xl font-semibold text-text">Entrar no ambiente</h1>
        <p className="mb-6 text-sm leading-6 text-text-subtle">
          Use um usuario seedado do projeto para acessar as areas protegidas e operar com o perfil correspondente.
        </p>

        <form action={signInAction} className="grid gap-4">
          <label className="grid gap-2 text-sm font-medium text-text">
            Usuario
            <Input
              name="username"
              placeholder="operador"
              defaultValue={lastUsername}
              autoComplete="username"
              required
            />
          </label>

          <label className="grid gap-2 text-sm font-medium text-text">
            Senha
            <Input
              name="password"
              type="password"
              placeholder="Informe sua senha"
              autoComplete="current-password"
              required
            />
          </label>

          {errorMessage ? (
            <p className="rounded-md border border-danger/30 bg-danger/10 px-3 py-2 text-sm text-text">
              {errorMessage}
            </p>
          ) : null}

          <Button type="submit">Entrar com Basic Auth</Button>
        </form>
      </section>
    </main>
  );
}
