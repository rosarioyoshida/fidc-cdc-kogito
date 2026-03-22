import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { LastroPanel } from "@/features/analise/lastro-panel";
import { lastrosFixture } from "@/test-support/ui-migration-fixtures";

describe("LastroPanel", () => {
  it("preserves validation flow feedback for migrated input, button, and table primitives", () => {
    render(
      <LastroPanel
        businessKey="CESSAO-001"
        lastros={lastrosFixture}
        registrarAction={vi.fn()}
        validarAction={vi.fn()}
      />
    );

    expect(screen.getByRole("button", { name: "Validar lastros" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Registrar lastro" })).toBeInTheDocument();
    expect(screen.getByText("REJEITADO")).toBeInTheDocument();
  });
});
