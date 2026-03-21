import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { CessaoList } from "@/features/cessao/cessao-list";
import { CessaoDetail } from "@/features/cessao/cessao-detail";
import type { Cessao } from "@/features/cessao/types";

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
        advanceAction={vi.fn()}
        refreshAction={vi.fn()}
      />
    );

    expect(screen.getByText("workflow-BK-UI-001")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Avancar etapa" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Atualizar status" })).toBeInTheDocument();
  });
});
