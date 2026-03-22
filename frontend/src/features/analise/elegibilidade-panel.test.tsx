import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { ElegibilidadePanel } from "@/features/analise/elegibilidade-panel";
import { regrasFixture } from "@/test-support/ui-migration-fixtures";

describe("ElegibilidadePanel", () => {
  it("preserves eligibility feedback and blocking counts", () => {
    render(
      <ElegibilidadePanel
        businessKey="CESSAO-001"
        regras={regrasFixture}
        avaliarAction={vi.fn()}
      />
    );

    expect(screen.getByRole("button", { name: "Avaliar agora" })).toBeInTheDocument();
    expect(screen.getByText("Limite excedido")).toBeInTheDocument();
    expect(screen.getByText("Bloqueios")).toBeInTheDocument();
  });
});
