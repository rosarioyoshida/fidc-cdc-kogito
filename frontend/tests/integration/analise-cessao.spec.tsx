import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { AnaliseDashboard } from "@/features/analise/analise-dashboard";
import type { AnaliseSnapshot } from "@/features/analise/types";
import type { Cessao } from "@/features/cessao/types";

const sampleCessao: Cessao = {
  businessKey: "BK-ANA-001",
  status: "EM_ANDAMENTO",
  workflowInstanceId: "workflow-BK-ANA-001",
  cedenteId: "CED-01",
  cessionariaId: "CESS-01",
  etapas: [{ nomeEtapa: "VALIDAR_CEDENTE", statusEtapa: "EM_EXECUCAO", ordem: 2 }]
};

const snapshot: AnaliseSnapshot = {
  regras: [
    {
      id: "RG-1",
      codigoRegra: "VALOR_TOTAL_POSITIVO",
      descricao: "Somatorio financeiro da cessao e positivo",
      resultado: "APROVADA",
      severidade: "INFORMATIVA",
      mensagem: "Base valida"
    }
  ],
  contratos: [
    {
      id: "CTR-1",
      identificadorExterno: "CTR-0001",
      sacadoId: "SAC-01",
      valorNominal: 1500,
      dataOrigem: "2026-03-20",
      statusRegistro: "PENDENTE",
      parcelas: [
        {
          id: "PAR-1",
          identificadorExterno: "PAR-0001",
          numeroParcela: 1,
          vencimento: "2026-04-20",
          valor: 1500,
          statusRegistro: "PENDENTE"
        }
      ]
    }
  ],
  parcelas: [
    {
      id: "PAR-1",
      identificadorExterno: "PAR-0001",
      numeroParcela: 1,
      vencimento: "2026-04-20",
      valor: 1500,
      statusRegistro: "PENDENTE"
    }
  ],
  pagamentos: [
    {
      id: "PG-1",
      valor: 1500,
      statusPagamento: "PENDENTE"
    }
  ],
  lastros: [
    {
      id: "LS-1",
      tipoDocumento: "NF-E",
      origem: "cedente",
      statusValidacao: "PENDENTE"
    }
  ]
};

describe("analise workspace", () => {
  it("renders operational panels and registradora actions", () => {
    render(
      <AnaliseDashboard
        cessao={sampleCessao}
        snapshot={snapshot}
        avaliarElegibilidadeAction={vi.fn()}
        apurarValorAction={vi.fn()}
        registrarLastroAction={vi.fn()}
        validarLastrosAction={vi.fn()}
        executarRegistradoraAction={vi.fn()}
      />
    );

    expect(screen.getByText("Analise BK-ANA-001")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Avaliar agora" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Apurar valor" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Registrar lastro" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /Enviar carteira/i })).toBeInTheDocument();
    expect(screen.getByText("CTR-0001")).toBeInTheDocument();
  });
});
