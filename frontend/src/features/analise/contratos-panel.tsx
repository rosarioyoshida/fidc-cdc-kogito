import React from "react";
import { EmptyState } from "@/components/feedback/empty-state";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from "@/components/ui/table";
import type { ContratoAnalise, ParcelaAnalise } from "@/features/analise/types";

type ContratosPanelProps = {
  contratos: ContratoAnalise[];
  parcelas: ParcelaAnalise[];
};

export function ContratosPanel({ contratos, parcelas }: ContratosPanelProps) {
  return (
    <section className="rounded-lg border bg-surface-raised p-6 shadow-soft">
      <div className="mb-4">
        <h2 className="text-xl font-semibold">Contratos e parcelas</h2>
        <p className="text-sm leading-6 text-text-subtle">
          Base financeira carregada para calculo, envio a registradora e conciliacao do fluxo.
        </p>
      </div>

      <div className="mb-4 grid gap-3 md:grid-cols-3">
        <article className="rounded-md border bg-surface p-4">
          <p className="text-xs uppercase tracking-[0.16em] text-text-subtle">Contratos</p>
          <p className="mt-2 text-2xl font-semibold">{contratos.length}</p>
        </article>
        <article className="rounded-md border bg-surface p-4">
          <p className="text-xs uppercase tracking-[0.16em] text-text-subtle">Parcelas</p>
          <p className="mt-2 text-2xl font-semibold">{parcelas.length}</p>
        </article>
        <article className="rounded-md border bg-surface p-4">
          <p className="text-xs uppercase tracking-[0.16em] text-text-subtle">Valor nominal</p>
          <p className="mt-2 text-2xl font-semibold">
            {contratos.reduce((acc, contrato) => acc + contrato.valorNominal, 0).toLocaleString("pt-BR", {
              style: "currency",
              currency: "BRL"
            })}
          </p>
        </article>
      </div>

      {contratos.length === 0 ? (
        <EmptyState
          title="Nenhum contrato importado"
          description="A cessao ainda nao recebeu contratos e parcelas para a trilha de analise."
        />
      ) : (
        <Table>
          <TableHeader className="bg-surface">
            <TableRow>
              <TableHead>Contrato</TableHead>
              <TableHead>Sacado</TableHead>
              <TableHead>Status</TableHead>
              <TableHead>Parcelas</TableHead>
              <TableHead className="text-right">Valor nominal</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {contratos.map((contrato) => (
              <TableRow key={contrato.id}>
                <TableCell className="font-semibold">{contrato.identificadorExterno}</TableCell>
                <TableCell>{contrato.sacadoId}</TableCell>
                <TableCell>{contrato.statusRegistro}</TableCell>
                <TableCell className="text-text-subtle">{contrato.parcelas.length}</TableCell>
                <TableCell className="text-right">
                  {contrato.valorNominal.toLocaleString("pt-BR", {
                    style: "currency",
                    currency: "BRL"
                  })}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}
    </section>
  );
}
