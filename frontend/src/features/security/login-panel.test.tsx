import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { LoginPanel } from "@/features/security/login-panel";

describe("LoginPanel", () => {
  it("renders migrated button and input primitives with authentication feedback", () => {
    render(
      <LoginPanel
        errorMessage="Credenciais invalidas"
        lastUsername="operador"
        signInAction={vi.fn()}
      />
    );

    expect(screen.getByLabelText("Usuario")).toHaveValue("operador");
    expect(screen.getByLabelText("Senha")).toHaveAttribute("type", "password");
    expect(screen.getByText("Credenciais invalidas")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Entrar com Basic Auth" })).toBeInTheDocument();
  });
});
