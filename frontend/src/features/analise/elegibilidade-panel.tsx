import React from "react";
import { EmptyState } from "@/components/feedback/empty-state";
import { Button } from "@/components/ui/button";
import { Table } from "@/components/ui/table";
import type { RegraElegibilidade } from "@/features/analise/types";

type ElegibilidadePanelProps = {
  businessKey: string;
  regras: RegraElegibilidade[];
  avaliarAction?: (formData: FormData) => void | Promise<void>;
};

export function ElegibilidadePanel({
  businessKey,
  regras,
  avaliarAction
}: ElegibilidadePanelProps) {
  const bloqueios = regras.filter((regra) => regra.resultado === "REPROVADA").length;

  return (
    <section className="rounded-lg border bg-surface-raised p-6 shadow-soft">
      <div className="mb-4 flex flex-col gap-3 md:flex-row md:items-start md:justify-between">
        <div>
          <h2 className="text-xl font-semibold">Elegibilidade</h2>
          <p className="text-sm leading-6 text-text-subtle">
            Reprocessa regras impeditivas e alertas antes do envio para etapas financeiras.
          </p>
        </div>

        <form action={avaliarAction}>
          <input type="hidden" name="businessKey" value={businessKey} />
          <Button type="submit">Avaliar agora</Button>
        </form>
      </div>

      <div className="mb-4 grid gap-3 md:grid-cols-3">
        <article className="rounded-md border bg-surface p-4">
          <p className="text-xs uppercase tracking-[0.16em] text-text-subtle">Regras avaliadas</p>
          <p className="mt-2 text-2xl font-semibold">{regras.length}</p>
        </article>
        <article className="rounded-md border bg-surface p-4">
          <p className="text-xs uppercase tracking-[0.16em] text-text-subtle">Bloqueios</p>
          <p className="mt-2 text-2xl font-semibold text-danger">{bloqueios}</p>
        </article>
        <article className="rounded-md border bg-surface p-4">
          <p className="text-xs uppercase tracking-[0.16em] text-text-subtle">Alertas</p>
          <p className="mt-2 text-2xl font-semibold">
            {regras.filter((regra) => regra.resultado === "ALERTA").length}
          </p>
        </article>
      </div>

      {regras.length === 0 ? (
        <EmptyState
          title="Nenhuma regra processada"
          description="Execute a avaliacao para registrar o resultado das regras de elegibilidade."
        />
      ) : (
        <Table>
          <thead className="bg-surface">
            <tr>
              <th className="px-4 py-3">Codigo</th>
              <th className="px-4 py-3">Descricao</th>
              <th className="px-4 py-3">Resultado</th>
              <th className="px-4 py-3">Severidade</th>
              <th className="px-4 py-3">Mensagem</th>
            </tr>
          </thead>
          <tbody>
            {regras.map((regra) => (
              <tr key={regra.id} className="border-t">
                <td className="px-4 py-3 font-semibold">{regra.codigoRegra}</td>
                <td className="px-4 py-3">{regra.descricao}</td>
                <td className="px-4 py-3">{regra.resultado}</td>
                <td className="px-4 py-3">{regra.severidade}</td>
                <td className="px-4 py-3 text-text-subtle">{regra.mensagem}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </section>
  );
}
