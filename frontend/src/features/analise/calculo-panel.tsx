import React from "react";
import { EmptyState } from "@/components/feedback/empty-state";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from "@/components/ui/table";
import type { PagamentoAnalise } from "@/features/analise/types";

type CalculoPanelProps = {
  businessKey: string;
  pagamentos: PagamentoAnalise[];
  apurarAction?: (formData: FormData) => void | Promise<void>;
};

export function CalculoPanel({
  businessKey,
  pagamentos,
  apurarAction
}: CalculoPanelProps) {
  const totalAprovado = pagamentos.reduce((acc, pagamento) => acc + pagamento.valor, 0);

  return (
    <section className="rounded-lg border bg-surface-raised p-6 shadow-soft">
      <div className="mb-4 flex flex-col gap-3 md:flex-row md:items-start md:justify-between">
        <div>
          <h2 className="text-xl font-semibold">Calculo e pagamento</h2>
          <p className="text-sm leading-6 text-text-subtle">
            Apura a base financeira da cessao e expõe o historico de liberacao financeira.
          </p>
        </div>

        <form action={apurarAction}>
          <input type="hidden" name="businessKey" value={businessKey} />
          <Button type="submit">Apurar valor</Button>
        </form>
      </div>

      <div className="mb-4 grid gap-3 md:grid-cols-2">
        <article className="rounded-md border bg-surface p-4">
          <p className="text-xs uppercase tracking-[0.16em] text-text-subtle">Pagamentos registrados</p>
          <p className="mt-2 text-2xl font-semibold">{pagamentos.length}</p>
        </article>
        <article className="rounded-md border bg-surface p-4">
          <p className="text-xs uppercase tracking-[0.16em] text-text-subtle">Valor agregado</p>
          <p className="mt-2 text-2xl font-semibold">
            {totalAprovado.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
          </p>
        </article>
      </div>

      {pagamentos.length === 0 ? (
        <EmptyState
          title="Nenhum pagamento gerado"
          description="Execute a apuracao para registrar a base financeira e preparar a etapa de pagamento."
        />
      ) : (
        <Table>
          <TableHeader className="bg-surface">
            <TableRow>
              <TableHead>Valor</TableHead>
              <TableHead>Status</TableHead>
              <TableHead>Autorizado por</TableHead>
              <TableHead>Autorizado em</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {pagamentos.map((pagamento) => (
              <TableRow key={pagamento.id}>
                <TableCell className="font-semibold">
                  {pagamento.valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
                </TableCell>
                <TableCell>{pagamento.statusPagamento}</TableCell>
                <TableCell>{pagamento.autorizadoPor ?? "Nao informado"}</TableCell>
                <TableCell className="text-text-subtle">
                  {pagamento.autorizadoEm ?? "Aguardando"}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}
    </section>
  );
}
