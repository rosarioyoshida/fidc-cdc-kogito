import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { CalculoPanel } from "@/features/analise/calculo-panel";
import { pagamentosFixture } from "@/test-support/ui-migration-fixtures";

describe("CalculoPanel", () => {
  it("preserves calculation communication and migrated table rendering", () => {
    render(
      <CalculoPanel
        businessKey="CESSAO-001"
        pagamentos={pagamentosFixture}
        apurarAction={vi.fn()}
      />
    );

    expect(screen.getByRole("button", { name: "Apurar valor" })).toBeInTheDocument();
    expect(screen.getByText("Calculo e pagamento")).toBeInTheDocument();
    expect(screen.getByText("AUTORIZADO")).toBeInTheDocument();
  });
});
