import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { AnaliseDashboard } from "@/features/analise/analise-dashboard";
import { AuditTimeline } from "@/features/auditoria/audit-timeline";
import { TaskContextPanel } from "@/features/auditoria/task-context-panel";
import {
  cessaoFixture,
  contratosFixture,
  lastrosFixture,
  pagamentosFixture,
  permissionContextFixture,
  regrasFixture
} from "@/test-support/ui-migration-fixtures";

describe("content shell sections", () => {
  it("organizes analysis and audit content into consistent sections", () => {
    render(
      <div className="grid gap-6">
        <AnaliseDashboard
          cessao={cessaoFixture}
          snapshot={{
            regras: regrasFixture,
            contratos: contratosFixture,
            parcelas: contratosFixture[0]?.parcelas ?? [],
            pagamentos: pagamentosFixture,
            lastros: lastrosFixture
          }}
          avaliarElegibilidadeAction={vi.fn()}
          apurarValorAction={vi.fn()}
          registrarLastroAction={vi.fn()}
          validarLastrosAction={vi.fn()}
          executarRegistradoraAction={vi.fn()}
          refreshAction={vi.fn()}
        />
        <TaskContextPanel permissionContext={permissionContextFixture} />
        <AuditTimeline
          items={[
            {
              id: "evt-1",
              atorId: "operador",
              perfil: "OPERADOR",
              tipoEvento: "CESSAO_CRIADA",
              resultado: "SUCESSO",
              correlationId: "corr-001",
              ocorridoEm: "2026-03-22T10:00:00Z",
              detalheRef: "businessKey=CESSAO-001",
              etapa: "ANALISE"
            }
          ]}
        />
      </div>
    );

    expect(screen.getByText("Analise CESSAO-001")).toBeInTheDocument();
    expect(screen.getByText("Contexto de tarefa humana")).toBeInTheDocument();
    expect(screen.getByText("Linha do tempo auditavel")).toBeInTheDocument();
    expect(screen.getAllByRole("article").length).toBeGreaterThan(0);
  });
});
