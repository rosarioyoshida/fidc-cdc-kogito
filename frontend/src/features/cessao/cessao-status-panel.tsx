import React from "react";
import type { Cessao } from "@/features/cessao/types";

type CessaoStatusPanelProps = {
  cessao?: Cessao;
  errorMessage?: string;
};

export function CessaoStatusPanel({ cessao, errorMessage }: CessaoStatusPanelProps) {
  if (errorMessage) {
    return (
      <section className="rounded-lg border border-danger/40 bg-surface p-6">
        <p className="mb-2 text-sm font-semibold uppercase tracking-[0.18em] text-danger">
          Falha operacional
        </p>
        <p className="text-sm leading-6 text-text-subtle">{errorMessage}</p>
      </section>
    );
  }

  if (!cessao) {
    return (
      <section className="rounded-lg border border-dashed bg-surface p-6">
        <p className="text-sm leading-6 text-text-subtle">
          Aguardando dados da cessao para apresentar o painel operacional.
        </p>
      </section>
    );
  }

  const totalEtapas = cessao.etapas?.length ?? 0;
  const etapasConcluidas =
    cessao.etapas?.filter((etapa) => etapa.statusEtapa === "CONCLUIDA").length ?? 0;
  const etapaAtual =
    cessao.etapas?.find((etapa) => etapa.statusEtapa === "EM_EXECUCAO")?.nomeEtapa ??
    "SEM_ETAPA_ATIVA";

  return (
    <section className="grid gap-4 rounded-lg border bg-surface-raised p-6 shadow-soft md:grid-cols-4">
      <div>
        <p className="text-xs font-semibold uppercase tracking-[0.14em] text-text-subtle">
          Status
        </p>
        <p className="mt-2 text-lg font-semibold">{cessao.status}</p>
      </div>
      <div>
        <p className="text-xs font-semibold uppercase tracking-[0.14em] text-text-subtle">
          Etapa atual
        </p>
        <p className="mt-2 text-sm font-semibold">{etapaAtual}</p>
      </div>
      <div>
        <p className="text-xs font-semibold uppercase tracking-[0.14em] text-text-subtle">
          Progresso
        </p>
        <p className="mt-2 text-sm font-semibold">
          {etapasConcluidas}/{totalEtapas} etapas concluidas
        </p>
      </div>
      <div>
        <p className="text-xs font-semibold uppercase tracking-[0.14em] text-text-subtle">
          Workflow
        </p>
        <p className="mt-2 text-sm font-semibold">
          {cessao.workflowInstanceId ?? "Nao inicializado"}
        </p>
      </div>
    </section>
  );
}
