import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { CessaoDetail } from "@/features/cessao/cessao-detail";
import {
  cessaoFixture,
  permissionContextFixture
} from "@/test-support/ui-migration-fixtures";

describe("CessaoDetail", () => {
  it("renders migrated table and action controls without losing status semantics", () => {
    render(
      <CessaoDetail
        cessao={cessaoFixture}
        permissionContext={permissionContextFixture}
        advanceAction={vi.fn()}
        refreshAction={vi.fn()}
      />
    );

    expect(screen.getByRole("button", { name: "Atualizar status" })).toBeInTheDocument();
    expect(screen.getByText("Etapas operacionais")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Avancar etapa" })).toBeInTheDocument();
    expect(screen.getByText("EM_EXECUCAO")).toBeInTheDocument();
  });
});
