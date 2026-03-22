import React from "react";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it } from "vitest";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from "@/components/ui/dialog";

describe("Dialog", () => {
  it("opens with accessible semantics and closes through the explicit close action", async () => {
    const user = userEvent.setup();

    render(
      <Dialog>
        <DialogTrigger asChild>
          <button type="button">Abrir dialogo</button>
        </DialogTrigger>
        <DialogContent closeLabel="Fechar dialogo de teste">
          <DialogHeader>
            <DialogTitle>Dialogo de teste</DialogTitle>
            <DialogDescription>Descricao operacional</DialogDescription>
          </DialogHeader>
        </DialogContent>
      </Dialog>
    );

    await user.click(screen.getByRole("button", { name: "Abrir dialogo" }));

    expect(screen.getByRole("dialog", { name: "Dialogo de teste" })).toBeInTheDocument();
    expect(screen.getByText("Descricao operacional")).toBeInTheDocument();

    await user.click(screen.getByRole("button", { name: "Fechar dialogo de teste" }));

    expect(screen.queryByRole("dialog", { name: "Dialogo de teste" })).not.toBeInTheDocument();
  });
});
