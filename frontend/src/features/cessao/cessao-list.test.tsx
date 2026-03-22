import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { CessaoList } from "@/features/cessao/cessao-list";
import { cessaoFixture } from "@/test-support/ui-migration-fixtures";

describe("CessaoList", () => {
  it("preserves form semantics and user-facing communication", () => {
    render(<CessaoList items={[cessaoFixture]} createAction={vi.fn()} />);

    expect(screen.getByRole("button", { name: "Iniciar cessao" })).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Business key")).toBeInTheDocument();
    expect(screen.getByText("Registrar operacao")).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /Abrir detalhe/ })).toBeInTheDocument();
  });
});
