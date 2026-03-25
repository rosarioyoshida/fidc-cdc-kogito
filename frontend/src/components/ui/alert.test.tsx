import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";

describe("Alert", () => {
  it("renders alert title and description with alert role", () => {
    render(
      <Alert variant="danger">
        <AlertTitle>Falha operacional</AlertTitle>
        <AlertDescription>Revise o estado da acao.</AlertDescription>
      </Alert>
    );

    expect(screen.getByRole("alert")).toBeInTheDocument();
    expect(screen.getByText("Falha operacional")).toBeInTheDocument();
    expect(screen.getByText("Revise o estado da acao.")).toBeInTheDocument();
  });
});
