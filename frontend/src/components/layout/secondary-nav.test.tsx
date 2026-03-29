import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { SecondaryNav } from "@/components/layout/secondary-nav";
import { Input } from "@/components/ui/input";
import { buildCessaoContextNavigation } from "@/features/navigation/shell-config";

const pathname = "/cessoes/CESSAO-001/analise";

vi.mock("next/navigation", () => ({
  usePathname: () => pathname
}));

describe("SecondaryNav", () => {
  it("renders contextual destinations and local controls for the active route", () => {
    render(
      <SecondaryNav
        config={buildCessaoContextNavigation("CESSAO-001")}
        controls={<Input aria-label="Buscar na rota" placeholder="Buscar" />}
      />
    );

    expect(screen.getByRole("link", { name: "Resumo" })).toHaveAttribute(
      "href",
      "/cessoes/CESSAO-001"
    );
    expect(screen.getByRole("link", { name: "Analise" })).toHaveAttribute(
      "href",
      "/cessoes/CESSAO-001/analise"
    );
    expect(screen.getByRole("textbox", { name: "Buscar na rota" })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: "Analise" }).className).toContain("bg-brand/10");
  });
});
