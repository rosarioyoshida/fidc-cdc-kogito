import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";

describe("Card", () => {
  it("renders heading, description and body content with semantic text", () => {
    render(
      <Card>
        <CardHeader>
          <CardTitle>Painel operacional</CardTitle>
          <CardDescription>Contexto resumido</CardDescription>
        </CardHeader>
        <CardContent>Conteudo principal</CardContent>
      </Card>
    );

    expect(screen.getByRole("heading", { name: "Painel operacional" })).toBeInTheDocument();
    expect(screen.getByText("Contexto resumido")).toBeInTheDocument();
    expect(screen.getByText("Conteudo principal")).toBeInTheDocument();
  });
});
