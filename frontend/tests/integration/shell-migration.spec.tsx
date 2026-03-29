import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import HomePage from "@/app/page";
import { AppShell } from "@/components/layout/app-shell";
import { seededAccounts } from "@/test-support/ui-migration-fixtures";
import {
  buildCessoesSecondaryNavigation,
  buildPrimaryAction,
  primaryNavigation
} from "@/features/navigation/shell-config";

const pathname = "/cessoes";

vi.mock("next/navigation", () => ({
  redirect: vi.fn(),
  usePathname: () => pathname,
  useRouter: () => ({
    refresh: vi.fn()
  })
}));

vi.mock("@/features/security/actions", () => ({
  getCurrentUserOptional: vi.fn().mockResolvedValue(null),
  signInAction: vi.fn(),
  logoutAction: vi.fn(),
  updateOwnEmailAction: vi.fn(),
  changeOwnPasswordAction: vi.fn()
}));

describe("shell migration", () => {
  it("covers both simplified login and authenticated shell entry points", async () => {
    const home = await HomePage({ searchParams: Promise.resolve({}) });

    render(
      <div>
        {home}
        <AppShell
          user={seededAccounts.operador}
          navigation={primaryNavigation}
          primaryAction={buildPrimaryAction()}
          secondaryNav={buildCessoesSecondaryNavigation()}
        >
          <div>Lista de cessoes migrada</div>
        </AppShell>
      </div>
    );

    expect(screen.getByRole("button", { name: "Entrar com Basic Auth" })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: "Cessoes" })).toBeInTheDocument();
    expect(screen.getAllByText("Shell compartilhado para operação, análise e auditoria.").length).toBeGreaterThan(0);
    expect(screen.getByText("Lista de cessoes migrada")).toBeInTheDocument();
  });
});
