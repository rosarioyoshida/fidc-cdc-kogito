import React from "react";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";
import { AccountSettingsDialog } from "@/components/ui/account-settings-dialog";
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

describe("account actions", () => {
  it("opens account settings with email and password actions", async () => {
    const user = userEvent.setup();
    render(<AccountSettingsDialog user={seededAccounts.analista} />);

    await user.click(screen.getByRole("button", { name: "Ajustes da conta" }));

    expect(screen.getByRole("dialog", { name: "Ajustes da conta" })).toBeInTheDocument();
    expect(screen.getByDisplayValue("analista@fidc.local")).toBeInTheDocument();
    expect(screen.getByLabelText("Senha atual")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Salvar email" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Alterar senha" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Fechar ajustes da conta" })).toBeInTheDocument();
  });

  it("closes account settings through the explicit close action", async () => {
    const user = userEvent.setup();
    render(<AccountSettingsDialog user={seededAccounts.analista} />);

    await user.click(screen.getByRole("button", { name: "Ajustes da conta" }));
    await user.click(screen.getByRole("button", { name: "Fechar ajustes da conta" }));

    expect(screen.queryByRole("dialog", { name: "Ajustes da conta" })).not.toBeInTheDocument();
  });
});
