import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { LoginPanel } from "@/features/security/login-panel";

describe("basic auth access", () => {
  it("renders login inputs and submit action for protected access", () => {
    render(<LoginPanel signInAction={vi.fn()} />);

    expect(screen.getByRole("heading", { name: "Entrar no ambiente" })).toBeInTheDocument();
    expect(screen.getByLabelText("Usuario")).toBeInTheDocument();
    expect(screen.getByLabelText("Senha")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Entrar com Basic Auth" })).toBeInTheDocument();
  });

  it("renders backend auth failure feedback clearly", () => {
    render(<LoginPanel signInAction={vi.fn()} errorMessage="Credenciais invalidas." />);

    expect(screen.getByText("Credenciais invalidas.")).toBeInTheDocument();
  });
});
