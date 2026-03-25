import React from "react";
import { DataRowCard } from "@/components/layout/data-row-card";
import { PageSection } from "@/components/layout/page-section";
import { EmptyState } from "@/components/feedback/empty-state";
import { Badge } from "@/components/ui/badge";
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
    <PageSection
      title="Linha do tempo auditavel"
      description="Evidencias imutaveis por ator, perfil, correlacao e resultado operacional."
    >
      {items.map((item) => (
        <DataRowCard
          key={item.id}
          leading={<Badge variant="brand">{item.tipoEvento}</Badge>}
          primary={`${item.atorId} · ${item.perfil}`}
          secondary={item.detalheRef ?? "Sem detalhe adicional"}
          columns={[
            <Badge key="resultado" variant={item.resultado === "SUCESSO" ? "success" : "warning"}>
              {item.resultado}
            </Badge>,
            <span key="etapa">{item.etapa ?? "Sem etapa"}</span>,
            <span key="ocorrido">{formatDateTime(item.ocorridoEm)}</span>
          ]}
          primaryAction={<span className="text-sm text-text-subtle">{item.correlationId}</span>}
        />
      ))}
    </PageSection>
  );
}
