import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { TopbarUserMenu } from "@/components/ui/topbar-user-menu";
import { seededAccounts } from "@/test-support/ui-migration-fixtures";

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

describe("TopbarUserMenu", () => {
  it("preserves user identity, notifications, and account actions after primitive migration", () => {
    render(<TopbarUserMenu user={seededAccounts.operador} />);

    expect(screen.getByText("Usuario autenticado")).toBeInTheDocument();
    expect(screen.getByText(/Revisar alertas da conta/)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Ajustes da conta" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Logout" })).toBeInTheDocument();
  });
});
