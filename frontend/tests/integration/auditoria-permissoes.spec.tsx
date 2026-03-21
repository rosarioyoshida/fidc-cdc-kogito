import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { AuditTimeline } from "@/features/auditoria/audit-timeline";
import { TaskContextPanel } from "@/features/auditoria/task-context-panel";
import { CessaoDetail } from "@/features/cessao/cessao-detail";
import type { AuditEvent } from "@/features/auditoria/types";
import type { Cessao } from "@/features/cessao/types";
import type { PermissionContext } from "@/features/security/types";

const sampleCessao: Cessao = {
  businessKey: "BK-AUD-001",
  status: "EM_ANDAMENTO",
  workflowInstanceId: "workflow-BK-AUD-001",
  cedenteId: "CED-01",
  cessionariaId: "CESS-01",
  etapas: [{ nomeEtapa: "AUTORIZAR_PAGAMENTO", statusEtapa: "EM_EXECUCAO", ordem: 12 }]
};

const deniedPermissionContext: PermissionContext = {
  actorId: "analista",
  perfis: ["ANALISTA"],
  etapasPermitidas: ["ANALISAR_ELEGIBILIDADE", "VALIDAR_LASTROS"],
  etapaAtual: "AUTORIZAR_PAGAMENTO",
  podeExecutarEtapaAtual: false,
  taskContext: {
    businessKey: "BK-AUD-001",
    workflowInstanceId: "workflow-BK-AUD-001",
    actorId: "analista",
    currentStage: "AUTORIZAR_PAGAMENTO",
    humanTaskPending: true,
    actorAuthorizedForTask: false,
    taskName: "AUTORIZAR_PAGAMENTO",
    taskDescription: "Autorizar pagamento",
    assignedActor: null,
    candidateGroups: ["APROVADOR"],
    candidateUsers: ["aprovador"],
    businessAdministratorGroups: ["AUDITOR"],
    taskConsoleUrl: "http://localhost:8280"
  },
  managementContext: {
    businessKey: "BK-AUD-001",
    workflowInstanceId: "workflow-BK-AUD-001",
    processStatus: "EM_ANDAMENTO",
    currentStage: "AUTORIZAR_PAGAMENTO",
    humanTaskPending: true,
    waitingForTimerJob: false,
    readModelAvailable: true,
    lastProjectionEvent: "ETAPA_AVANCADA",
    lastProjectionUpdate: "2026-03-20T13:00:00Z",
    availableAdminActions: ["VIEW_PROCESS_INSTANCE", "INSPECT_HUMAN_TASK"],
    managementConsoleUrl: "http://localhost:8380",
    dataIndexUrl: "http://localhost:8180/graphql",
    jobsServiceUrl: "http://localhost:8085"
  }
};

const auditTrail: AuditEvent[] = [
  {
    id: "evt-1",
    atorId: "operador",
    perfil: "OPERADOR",
    tipoEvento: "CESSAO_CRIADA",
    resultado: "SUCESSO",
    correlationId: "corr-001",
    ocorridoEm: "2026-03-20T12:00:00Z",
    detalheRef: "businessKey=BK-AUD-001",
    etapa: "IMPORTAR_CARTEIRA"
  }
];

describe("auditoria e permissoes", () => {
  it("renders forbidden feedback when actor cannot execute current stage", () => {
    render(
      <CessaoDetail
        cessao={sampleCessao}
        permissionContext={deniedPermissionContext}
        advanceAction={vi.fn()}
        refreshAction={vi.fn()}
      />
    );

    expect(screen.queryByRole("button", { name: "Avancar etapa" })).not.toBeInTheDocument();
    expect(screen.getByText("Acao indisponivel")).toBeInTheDocument();
  });

  it("renders task and management context alongside audit events", () => {
    render(
      <div>
        <TaskContextPanel permissionContext={deniedPermissionContext} />
        <AuditTimeline items={auditTrail} />
      </div>
    );

    expect(screen.getByText("Contexto de tarefa humana")).toBeInTheDocument();
    expect(screen.getByText("APROVADOR")).toBeInTheDocument();
    expect(screen.getByText("Linha do tempo auditavel")).toBeInTheDocument();
    expect(screen.getByText("CESSAO_CRIADA")).toBeInTheDocument();
    expect(screen.getByText("corr-001")).toBeInTheDocument();
  });
});
