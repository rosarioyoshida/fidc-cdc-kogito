import React from "react";
import Link from "next/link";
import { DataRowCard } from "@/components/layout/data-row-card";
import { PageSection } from "@/components/layout/page-section";
import { EmptyState } from "@/components/feedback/empty-state";
import { Badge } from "@/components/ui/badge";
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
      <PageSection
        title="Registrar operacao"
        description="Inicie uma nova cessao sem sair do shell compartilhado."
        className="rounded-[32px] border border-border/80 bg-surface-raised p-6 shadow-soft"
      >
        <form action={createAction} className="grid gap-4 md:grid-cols-4" id="nova-cessao">
          <Input name="businessKey" placeholder="Business key" required />
          <Input name="cedenteId" placeholder="Cedente" required />
          <Input name="cessionariaId" placeholder="Cessionaria" required />
          <Button type="submit">Iniciar cessao</Button>
        </form>
      </PageSection>

      {errorMessage ? (
        <EmptyState title="Falha ao consultar cessoes" description={errorMessage} />
      ) : null}

      {items.length === 0 ? (
        <EmptyState
          title="Nenhuma cessao carregada"
          description="Registre a primeira operacao para iniciar a jornada operacional da US1."
        />
      ) : (
        <PageSection
          title="Cessoes ativas"
          description="Acompanhe status, participantes e acesso rapido ao detalhe de cada operacao."
        >
          {items.map((item) => (
            <DataRowCard
              key={item.businessKey}
              leading={<Badge variant="brand">{item.status}</Badge>}
              primary={item.businessKey}
              secondary={`Cedente ${item.cedenteId} • Cessionaria ${item.cessionariaId}`}
              columns={[
                <span key="workflow">{item.workflowInstanceId ?? "Workflow pendente"}</span>,
                <span key="importacao">{item.dataImportacao ?? "Sem data de importacao"}</span>,
                <span key="encerramento">{item.dataEncerramento ?? "Em andamento"}</span>
              ]}
              primaryAction={
                <Button asChild variant="secondary">
                  <Link href={`/cessoes/${item.businessKey}`}>Abrir detalhe</Link>
                </Button>
              }
            />
          ))}
        </PageSection>
      )}
    </div>
  );
}
