import React from "react";
import { EmptyState } from "@/components/feedback/empty-state";
import type { AuditEvent } from "@/features/auditoria/types";

type AuditTimelineProps = {
  items: AuditEvent[];
};

function formatDateTime(value?: string | null) {
  if (!value) {
    return "Sem registro";
  }

  return new Intl.DateTimeFormat("pt-BR", {
    dateStyle: "short",
    timeStyle: "short"
  }).format(new Date(value));
}

export function AuditTimeline({ items }: AuditTimelineProps) {
  if (items.length === 0) {
    return (
      <EmptyState
        title="Sem eventos auditaveis"
        description="Os eventos da trilha de auditoria aparecerao aqui conforme o fluxo operacional for executado."
      />
    );
  }

  return (
    <section className="rounded-lg border bg-surface-raised p-6 shadow-soft">
      <div className="mb-5">
        <h2 className="text-xl font-semibold">Linha do tempo auditavel</h2>
        <p className="text-sm leading-6 text-text-subtle">
          Evidencias imutaveis por ator, perfil, correlacao e resultado operacional.
        </p>
      </div>

      <ol className="grid gap-4">
        {items.map((item) => (
          <li key={item.id} className="rounded-lg border bg-surface p-4">
            <div className="flex flex-col gap-3 md:flex-row md:items-start md:justify-between">
              <div className="space-y-2">
                <div className="flex flex-wrap items-center gap-2">
                  <span className="rounded-full bg-brand/10 px-3 py-1 text-xs font-semibold uppercase tracking-[0.16em] text-brand">
                    {item.tipoEvento}
                  </span>
                  <span className="rounded-full bg-surface-raised px-3 py-1 text-xs font-semibold uppercase tracking-[0.16em] text-text-subtle">
                    {item.resultado}
                  </span>
                  {item.etapa ? (
                    <span className="text-xs font-medium text-text-subtle">{item.etapa}</span>
                  ) : null}
                </div>
                <p className="text-sm text-text">
                  <span className="font-semibold">{item.atorId}</span>
                  {" · "}
                  <span className="text-text-subtle">{item.perfil}</span>
                </p>
                {item.detalheRef ? (
                  <p className="text-sm leading-6 text-text-subtle">{item.detalheRef}</p>
                ) : null}
              </div>

              <dl className="grid gap-1 text-sm text-text-subtle md:text-right">
                <div>
                  <dt className="font-semibold text-text">Ocorrido em</dt>
                  <dd>{formatDateTime(item.ocorridoEm)}</dd>
                </div>
                <div>
                  <dt className="font-semibold text-text">Correlation ID</dt>
                  <dd className="break-all">{item.correlationId}</dd>
                </div>
              </dl>
            </div>
          </li>
        ))}
      </ol>
    </section>
  );
}
