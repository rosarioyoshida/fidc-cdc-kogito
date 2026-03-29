import React from "react";
import { DataRowCard } from "@/components/layout/data-row-card";
import { PageSection } from "@/components/layout/page-section";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { CessaoStatusPanel } from "@/features/cessao/cessao-status-panel";
import type { Cessao } from "@/features/cessao/types";
import { PermissionGuard } from "@/features/security/permission-guard";
import type { PermissionContext } from "@/features/security/types";

type CessaoDetailProps = {
  cessao: Cessao;
  permissionContext: PermissionContext;
  errorMessage?: string;
  advanceAction?: (formData: FormData) => void | Promise<void>;
};

export function CessaoDetail({
  cessao,
  permissionContext,
  errorMessage,
  advanceAction
}: CessaoDetailProps) {
  return (
    <div className="grid gap-6">
      <PageSection
        title={cessao.businessKey}
        description="Resumo operacional da cessao e visao das etapas em andamento."
      >
        <CessaoStatusPanel cessao={cessao} errorMessage={errorMessage} />
      </PageSection>

      <PageSection
        title="Etapas operacionais"
        description="Avanco manual apenas para a etapa atualmente em execucao."
      >
        {(cessao.etapas ?? []).map((etapa) => (
          <DataRowCard
            key={etapa.nomeEtapa}
            leading={<Badge variant={etapa.statusEtapa === "EM_EXECUCAO" ? "warning" : "subtle"}>{`#${etapa.ordem}`}</Badge>}
            primary={etapa.nomeEtapa}
            secondary={`Responsavel: ${etapa.responsavelId ?? "Nao atribuido"}`}
            columns={[
              <span key="status">{etapa.statusEtapa}</span>,
              <span key="inicio">{etapa.inicioEm ?? "Sem inicio registrado"}</span>,
              <span key="resultado">{etapa.resultado ?? "Sem resultado"}</span>
            ]}
            primaryAction={
              etapa.statusEtapa === "EM_EXECUCAO" ? (
                <PermissionGuard
                  allowed={
                    permissionContext.podeExecutarEtapaAtual &&
                    permissionContext.etapaAtual === etapa.nomeEtapa
                  }
                  fallbackDescription="O perfil autenticado nao pode concluir esta etapa. Consulte a trilha de auditoria ou o Task Console."
                >
                  <form action={advanceAction} className="inline-flex">
                    <input type="hidden" name="businessKey" value={cessao.businessKey} />
                    <input type="hidden" name="etapaNome" value={etapa.nomeEtapa} />
                    <input type="hidden" name="responsavelId" value={permissionContext.actorId} />
                    <Button type="submit">Avancar etapa</Button>
                  </form>
                </PermissionGuard>
              ) : (
                <Badge variant="success">Sem acao</Badge>
              )
            }
          />
        ))}
      </PageSection>
    </div>
  );
}
