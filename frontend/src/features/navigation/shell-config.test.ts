import { describe, expect, it } from "vitest";
import {
  buildCessaoContextNavigation,
  buildCessoesSecondaryNavigation,
  buildPrimaryAction,
  primaryNavigation
} from "@/features/navigation/shell-config";

describe("shell-config", () => {
  it("defines the authenticated primary navigation and primary action", () => {
    expect(primaryNavigation).toEqual([
      {
        label: "Cessoes",
        href: "/cessoes",
        exact: false
      }
    ]);
    expect(buildPrimaryAction()).toEqual({
      label: "Nova cessao",
      href: "/cessoes#nova-cessao"
    });
  });

  it("builds route contextual navigation for list and cessao detail routes", () => {
    expect(buildCessoesSecondaryNavigation()).toEqual({
      items: [{ label: "Lista", href: "/cessoes", exact: true }]
    });
    expect(buildCessaoContextNavigation("BK-123").items).toEqual([
      { label: "Resumo", href: "/cessoes/BK-123", exact: true },
      { label: "Analise", href: "/cessoes/BK-123/analise" },
      { label: "Auditoria", href: "/cessoes/BK-123/auditoria" }
    ]);
  });
});
