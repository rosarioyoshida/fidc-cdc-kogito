import React from "react";
import { Button } from "@/components/ui/button";
import { Table } from "@/components/ui/table";
import { CessaoStatusPanel } from "@/features/cessao/cessao-status-panel";
import type { Cessao } from "@/features/cessao/types";

type CessaoDetailProps = {
  cessao: Cessao;
  errorMessage?: string;
  advanceAction?: (formData: FormData) => void | Promise<void>;
  refreshAction?: (formData: FormData) => void | Promise<void>;
};

export function CessaoDetail({
  cessao,
  errorMessage,
  advanceAction,
  refreshAction
}: CessaoDetailProps) {
  return (
    <div className="grid gap-6">
      <header className="flex flex-col gap-4 rounded-lg border bg-surface-raised p-6 shadow-soft md:flex-row md:items-center md:justify-between">
        <div>
          <p className="text-sm font-semibold uppercase tracking-[0.18em] text-text-subtle">
            Cessao
          </p>
          <h1 className="text-3xl font-semibold">{cessao.businessKey}</h1>
        </div>

        <form action={refreshAction}>
          <input type="hidden" name="businessKey" value={cessao.businessKey} />
          <Button type="submit" variant="secondary">
            Atualizar status
          </Button>
        </form>
      </header>

      <CessaoStatusPanel cessao={cessao} errorMessage={errorMessage} />

      <section className="rounded-lg border bg-surface-raised p-6 shadow-soft">
        <div className="mb-4">
          <h2 className="text-xl font-semibold">Etapas operacionais</h2>
          <p className="text-sm leading-6 text-text-subtle">
            Avanco manual apenas para a etapa atualmente em execucao.
          </p>
        </div>

        <Table>
          <thead className="bg-surface">
            <tr>
              <th className="px-4 py-3">Ordem</th>
              <th className="px-4 py-3">Etapa</th>
              <th className="px-4 py-3">Status</th>
              <th className="px-4 py-3">Responsavel</th>
              <th className="px-4 py-3 text-right">Acao</th>
            </tr>
          </thead>
          <tbody>
            {(cessao.etapas ?? []).map((etapa) => (
              <tr key={etapa.nomeEtapa} className="border-t">
                <td className="px-4 py-3">{etapa.ordem}</td>
                <td className="px-4 py-3 font-semibold">{etapa.nomeEtapa}</td>
                <td className="px-4 py-3 text-text-subtle">{etapa.statusEtapa}</td>
                <td className="px-4 py-3 text-text-subtle">
                  {etapa.responsavelId ?? "Nao atribuido"}
                </td>
                <td className="px-4 py-3 text-right">
                  {etapa.statusEtapa === "EM_EXECUCAO" ? (
                    <form action={advanceAction} className="inline-flex">
                      <input type="hidden" name="businessKey" value={cessao.businessKey} />
                      <input type="hidden" name="etapaNome" value={etapa.nomeEtapa} />
                      <input type="hidden" name="responsavelId" value="operador" />
                      <Button type="submit">Avancar etapa</Button>
                    </form>
                  ) : (
                    <span className="text-sm text-text-subtle">Sem acao</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </section>
    </div>
  );
}
