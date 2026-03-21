import React from "react";
import Link from "next/link";
import { EmptyState } from "@/components/feedback/empty-state";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import type { Cessao } from "@/features/cessao/types";

type CessaoListProps = {
  items: Cessao[];
  errorMessage?: string;
  createAction?: (formData: FormData) => void | Promise<void>;
};

export function CessaoList({ items, errorMessage, createAction }: CessaoListProps) {
  return (
    <div className="grid gap-6">
      <section className="rounded-lg border bg-surface-raised p-6 shadow-soft">
        <div className="mb-4 flex items-center justify-between">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.18em] text-text-subtle">
              Nova cessao
            </p>
            <h2 className="text-2xl font-semibold">Registrar operacao</h2>
          </div>
        </div>

        <form action={createAction} className="grid gap-4 md:grid-cols-4">
          <Input name="businessKey" placeholder="Business key" required />
          <Input name="cedenteId" placeholder="Cedente" required />
          <Input name="cessionariaId" placeholder="Cessionaria" required />
          <Button type="submit">Iniciar cessao</Button>
        </form>
      </section>

      {errorMessage ? (
        <EmptyState title="Falha ao consultar cessoes" description={errorMessage} />
      ) : null}

      {items.length === 0 ? (
        <EmptyState
          title="Nenhuma cessao carregada"
          description="Registre a primeira operacao para iniciar a jornada operacional da US1."
        />
      ) : (
        <section className="grid gap-4">
          {items.map((item) => (
            <Link
              key={item.businessKey}
              href={`/cessoes/${item.businessKey}`}
              className="rounded-lg border bg-surface p-5 transition hover:-translate-y-0.5 hover:shadow-soft"
            >
              <div className="flex items-center justify-between gap-4">
                <div>
                  <p className="text-xs font-semibold uppercase tracking-[0.14em] text-text-subtle">
                    {item.status}
                  </p>
                  <h3 className="mt-2 text-lg font-semibold">{item.businessKey}</h3>
                  <p className="mt-1 text-sm text-text-subtle">
                    Cedente {item.cedenteId} • Cessionaria {item.cessionariaId}
                  </p>
                </div>
                <span className="text-sm font-semibold text-brand">Abrir detalhe</span>
              </div>
            </Link>
          ))}
        </section>
      )}
    </div>
  );
}
