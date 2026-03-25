import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { AppShell } from "@/components/layout/app-shell";
import { seededAccounts } from "@/test-support/ui-migration-fixtures";
import {
  buildCessaoContextNavigation,
  buildPrimaryAction,
  primaryNavigation
} from "@/features/navigation/shell-config";

const pathname = "/cessoes/CESSAO-001";

vi.mock("next/navigation", () => ({
  usePathname: () => pathname,
  useRouter: () => ({
    refresh: vi.fn()
  })
}));

vi.mock("@/features/security/actions", () => ({
  logoutAction: vi.fn(),
  updateOwnEmailAction: vi.fn(),
  changeOwnPasswordAction: vi.fn()
}));

describe("AppShell", () => {
  it("renders the global shell, contextual nav, content, and footer", () => {
    render(
      <AppShell
        user={seededAccounts.operador}
        navigation={primaryNavigation}
        primaryAction={buildPrimaryAction()}
        secondaryNav={buildCessaoContextNavigation("CESSAO-001")}
      >
        <div>Conteudo principal migrado</div>
      </AppShell>
    );

    expect(screen.getByRole("link", { name: /FIDC CDC KOGITO/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: "Cessoes" })).toHaveAttribute("href", "/cessoes");
    expect(screen.getByRole("link", { name: "Nova cessao" })).toHaveAttribute(
      "href",
      "/cessoes#nova-cessao"
    );
    expect(screen.getByRole("link", { name: "Resumo" })).toHaveAttribute(
      "href",
      "/cessoes/CESSAO-001"
    );
    expect(screen.getByRole("link", { name: "Analise" })).toHaveAttribute(
      "href",
      "/cessoes/CESSAO-001/analise"
    );
    expect(screen.getByText("Conteudo principal migrado")).toBeInTheDocument();
    expect(screen.getByText("Shell compartilhado para operação, análise e auditoria.")).toBeInTheDocument();
  });
});
