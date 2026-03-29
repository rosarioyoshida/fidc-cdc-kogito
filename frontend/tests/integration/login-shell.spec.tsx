import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import HomePage from "@/app/page";

vi.mock("next/navigation", () => ({
  redirect: vi.fn()
}));

vi.mock("@/features/security/actions", () => ({
  getCurrentUserOptional: vi.fn().mockResolvedValue(null),
  signInAction: vi.fn()
}));

describe("login shell", () => {
  it("renders the simplified login shell without global header", async () => {
    const page = await HomePage({
      searchParams: Promise.resolve({
        error: encodeURIComponent("Credenciais invalidas"),
        username: encodeURIComponent("operador")
      })
    });

    render(page);

    expect(screen.getByRole("heading", { name: /Novo shell operacional/i })).toBeInTheDocument();
    expect(screen.getByRole("heading", { name: /Entrar no ambiente/i })).toBeInTheDocument();
    expect(screen.getByLabelText("Usuario")).toHaveValue("operador");
    expect(screen.getByText("Credenciais invalidas")).toBeInTheDocument();
    expect(screen.queryByRole("link", { name: /^Cessoes$/ })).not.toBeInTheDocument();
    expect(screen.getByText("Shell compartilhado para operação, análise e auditoria.")).toBeInTheDocument();
  });
});
