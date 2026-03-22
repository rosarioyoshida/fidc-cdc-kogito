import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { TopbarUserMenu } from "@/components/ui/topbar-user-menu";
import { seededAccounts } from "./auth-menu-fixtures";

vi.mock("@/features/security/actions", () => ({
  logoutAction: vi.fn(),
  updateOwnEmailAction: vi.fn(),
  changeOwnPasswordAction: vi.fn()
}));

describe("topbar user menu", () => {
  it("renders authenticated user identity and account actions", () => {
    render(<TopbarUserMenu user={seededAccounts.operador} />);

    expect(screen.getByText("Operador Padrao")).toBeInTheDocument();
    expect(screen.getByText(/Operador · operador@fidc.local/)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Ajustes da conta" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Logout" })).toBeInTheDocument();
  });
});
