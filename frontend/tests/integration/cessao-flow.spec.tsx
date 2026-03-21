import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { CessaoList } from "@/features/cessao/cessao-list";
import { CessaoDetail } from "@/features/cessao/cessao-detail";
import type { Cessao } from "@/features/cessao/types";
import type { PermissionContext } from "@/features/security/types";

const sampleCessao: Cessao = {
  businessKey: "BK-UI-001",
  status: "EM_ANDAMENTO",
  workflowInstanceId: "workflow-BK-UI-001",
  cedenteId: "CED-01",
  cessionariaId: "CESS-01",
  etapas: [
    { nomeEtapa: "IMPORTAR_CARTEIRA", statusEtapa: "CONCLUIDA", ordem: 1 },
    { nomeEtapa: "VALIDAR_CEDENTE", statusEtapa: "EM_EXECUCAO", ordem: 2 }
  ]
};

const allowedPermissionContext: PermissionContext = {
  actorId: "operador",
  perfis: ["OPERADOR"],
  etapasPermitidas: ["VALIDAR_CEDENTE"],
  etapaAtual: "VALIDAR_CEDENTE",
  podeExecutarEtapaAtual: true,
  taskContext: {
    businessKey: "BK-UI-001",
    workflowInstanceId: "workflow-BK-UI-001",
    actorId: "operador",
    currentStage: "VALIDAR_CEDENTE",
    humanTaskPending: true,
    actorAuthorizedForTask: true,
    taskName: "VALIDAR_CEDENTE",
    taskDescription: "Validar cedente",
    assignedActor: null,
    candidateGroups: ["OPERADOR"],
    candidateUsers: ["operador"],
    businessAdministratorGroups: ["AUDITOR"],
    taskConsoleUrl: "http://localhost:8280"
  },
  managementContext: {
    businessKey: "BK-UI-001",
    workflowInstanceId: "workflow-BK-UI-001",
    processStatus: "EM_ANDAMENTO",
    currentStage: "VALIDAR_CEDENTE",
    humanTaskPending: true,
    waitingForTimerJob: false,
    readModelAvailable: true,
    lastProjectionEvent: "ETAPA_AVANCADA",
    lastProjectionUpdate: "2026-03-20T12:00:00Z",
    availableAdminActions: ["VIEW_PROCESS_INSTANCE"],
    managementConsoleUrl: "http://localhost:8380",
    dataIndexUrl: "http://localhost:8180/graphql",
    jobsServiceUrl: "http://localhost:8085"
  }
};

describe("cessao flow views", () => {
  it("renders list and create action", () => {
    render(<CessaoList items={[sampleCessao]} createAction={vi.fn()} />);

    expect(screen.getByText("BK-UI-001")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Iniciar cessao" })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /Abrir detalhe/i })).toHaveAttribute(
      "href",
      "/cessoes/BK-UI-001"
    );
  });

  it("renders detail and active stage action", () => {
    render(
      <CessaoDetail
        cessao={sampleCessao}
        permissionContext={allowedPermissionContext}
        advanceAction={vi.fn()}
        refreshAction={vi.fn()}
      />
    );

    expect(screen.getByText("workflow-BK-UI-001")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Avancar etapa" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Atualizar status" })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: "Abrir auditoria" })).toHaveAttribute(
      "href",
      "/cessoes/BK-UI-001/auditoria"
    );
  });
});
