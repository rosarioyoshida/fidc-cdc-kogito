import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";
import { ContratosPanel } from "@/features/analise/contratos-panel";
import { contratosFixture } from "@/test-support/ui-migration-fixtures";

describe("ContratosPanel", () => {
  it("preserves table readability for imported contracts", () => {
    render(<ContratosPanel contratos={contratosFixture} parcelas={contratosFixture[0].parcelas} />);

    expect(screen.getByText("Contratos e parcelas")).toBeInTheDocument();
    expect(screen.getByText("CTR-001")).toBeInTheDocument();
    expect(screen.getByText("SAC-123")).toBeInTheDocument();
  });
});
