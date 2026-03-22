import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { RegistradoraPanel } from "@/features/analise/registradora-panel";

describe("RegistradoraPanel", () => {
  it("preserves operational submission semantics across migrated action buttons", () => {
    render(<RegistradoraPanel businessKey="CESSAO-001" executarAction={vi.fn()} />);

    expect(screen.getByText("Registradora")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Enviar carteira" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Enviar pagamento" })).toBeInTheDocument();
  });
});
