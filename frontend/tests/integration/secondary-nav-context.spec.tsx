import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { SecondaryNav } from "@/components/layout/secondary-nav";
import { Button } from "@/components/ui/button";
import { buildCessaoContextNavigation } from "@/features/navigation/shell-config";

const pathname = "/cessoes/BK-200/analise";

vi.mock("next/navigation", () => ({
  usePathname: () => pathname
}));

describe("secondary navigation context", () => {
  it("shows the active contextual destination and route-level controls", () => {
    render(
      <SecondaryNav
        config={buildCessaoContextNavigation("BK-200")}
        controls={<Button variant="secondary">Atualizar painel</Button>}
      />
    );

    expect(screen.getByRole("link", { name: "Resumo" })).toHaveAttribute("href", "/cessoes/BK-200");
    expect(screen.getByRole("link", { name: "Analise" }).className).toContain("bg-brand/10");
    expect(screen.getByRole("button", { name: "Atualizar painel" })).toBeInTheDocument();
  });
});
