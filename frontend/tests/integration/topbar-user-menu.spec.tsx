import React from "react";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";
import { TopbarUserMenu } from "@/components/ui/topbar-user-menu";
import { seededAccounts } from "./auth-menu-fixtures";

vi.mock("next/navigation", () => ({
  usePathname: () => "/cessoes",
  useRouter: () => ({
    refresh: vi.fn()
  })
}));

vi.mock("@/features/security/actions", () => ({
  logoutAction: vi.fn(),
  updateOwnEmailAction: vi.fn(),
  changeOwnPasswordAction: vi.fn()
}));

describe("topbar user menu", () => {
  it("renders authenticated user identity and account actions", async () => {
    const user = userEvent.setup();

    render(<TopbarUserMenu user={seededAccounts.operador} />);

    expect(screen.getByRole("button", { name: /1 notificacoes pendentes/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /Menu da conta de Operador Padrao/i })).toBeInTheDocument();

    await user.click(screen.getByRole("button", { name: /Menu da conta de Operador Padrao/i }));

    expect(screen.getAllByText("Operador Padrao").length).toBeGreaterThan(0);
    expect(screen.getAllByText(/Operador · operador@fidc.local/).length).toBeGreaterThan(0);
    expect(screen.getByRole("button", { name: "Alterar senha" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Logout" })).toBeInTheDocument();
  });
});
