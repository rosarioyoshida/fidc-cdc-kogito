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
    <main className="mx-auto grid min-h-[calc(100vh-96px)] w-full max-w-5xl place-items-center px-6 py-10">
      <section className="grid w-full gap-8 rounded-[32px] border border-border/80 bg-surface-raised p-8 shadow-soft lg:grid-cols-[1.1fr,0.9fr] lg:p-10">
        <div className="flex flex-col justify-center">
          <p className="mb-3 text-sm font-semibold uppercase tracking-[0.18em] text-text-subtle">
            Plataforma
          </p>
          <h1 className="mb-4 text-4xl font-semibold text-text">Novo shell operacional</h1>
          <p className="max-w-xl text-sm leading-7 text-text-subtle">
            Acesse a experiência unificada de cessões, análise e auditoria com navegação consistente,
            contexto por rota e ações de conta preservadas.
          </p>
        </div>

        <div className="rounded-[28px] border border-border/70 bg-surface p-6 shadow-soft">
          <p className="mb-3 text-sm font-semibold uppercase tracking-[0.18em] text-text-subtle">
          Controle de Acesso
        </p>
          <h2 className="mb-3 text-3xl font-semibold text-text">Entrar no ambiente</h2>
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
        </div>
      </section>
    </main>
  );
}
