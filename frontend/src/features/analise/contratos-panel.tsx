import React from "react";
import { EmptyState } from "@/components/feedback/empty-state";
import { Table } from "@/components/ui/table";
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
          <thead className="bg-surface">
            <tr>
              <th className="px-4 py-3">Contrato</th>
              <th className="px-4 py-3">Sacado</th>
              <th className="px-4 py-3">Status</th>
              <th className="px-4 py-3">Parcelas</th>
              <th className="px-4 py-3 text-right">Valor nominal</th>
            </tr>
          </thead>
          <tbody>
            {contratos.map((contrato) => (
              <tr key={contrato.id} className="border-t">
                <td className="px-4 py-3 font-semibold">{contrato.identificadorExterno}</td>
                <td className="px-4 py-3">{contrato.sacadoId}</td>
                <td className="px-4 py-3">{contrato.statusRegistro}</td>
                <td className="px-4 py-3 text-text-subtle">{contrato.parcelas.length}</td>
                <td className="px-4 py-3 text-right">
                  {contrato.valorNominal.toLocaleString("pt-BR", {
                    style: "currency",
                    currency: "BRL"
                  })}
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </section>
  );
}
