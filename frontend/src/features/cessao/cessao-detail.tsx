import React from "react";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from "@/components/ui/table";
import { CessaoStatusPanel } from "@/features/cessao/cessao-status-panel";
import type { Cessao } from "@/features/cessao/types";
import { PermissionGuard } from "@/features/security/permission-guard";
import type { PermissionContext } from "@/features/security/types";

type CessaoDetailProps = {
  cessao: Cessao;
  permissionContext: PermissionContext;
  errorMessage?: string;
  advanceAction?: (formData: FormData) => void | Promise<void>;
  refreshAction?: (formData: FormData) => void | Promise<void>;
};

export function CessaoDetail({
  cessao,
  permissionContext,
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

        <div className="flex flex-wrap gap-3">
          <Link
            href={`/cessoes/${cessao.businessKey}/analise`}
            className="inline-flex min-h-10 items-center justify-center rounded-md border bg-surface-raised px-4 py-2 text-sm font-semibold text-text transition hover:bg-surface"
          >
            Abrir analise
          </Link>
          <Link
            href={`/cessoes/${cessao.businessKey}/auditoria`}
            className="inline-flex min-h-10 items-center justify-center rounded-md border bg-surface-raised px-4 py-2 text-sm font-semibold text-text transition hover:bg-surface"
          >
            Abrir auditoria
          </Link>

          <form action={refreshAction}>
            <input type="hidden" name="businessKey" value={cessao.businessKey} />
            <Button type="submit" variant="secondary">
              Atualizar status
            </Button>
          </form>
        </div>
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
          <TableHeader className="bg-surface">
            <TableRow>
              <TableHead>Ordem</TableHead>
              <TableHead>Etapa</TableHead>
              <TableHead>Status</TableHead>
              <TableHead>Responsavel</TableHead>
              <TableHead className="text-right">Acao</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {(cessao.etapas ?? []).map((etapa) => (
              <TableRow key={etapa.nomeEtapa}>
                <TableCell>{etapa.ordem}</TableCell>
                <TableCell className="font-semibold">{etapa.nomeEtapa}</TableCell>
                <TableCell className="text-text-subtle">{etapa.statusEtapa}</TableCell>
                <TableCell className="text-text-subtle">
                  {etapa.responsavelId ?? "Nao atribuido"}
                </TableCell>
                <TableCell className="text-right">
                  {etapa.statusEtapa === "EM_EXECUCAO" ? (
                    <PermissionGuard
                      allowed={
                        permissionContext.podeExecutarEtapaAtual &&
                        permissionContext.etapaAtual === etapa.nomeEtapa
                      }
                      fallbackDescription="O perfil autenticado nao pode concluir esta etapa. Consulte a trilha de auditoria ou o Task Console."
                    >
                      <form action={advanceAction} className="inline-flex">
                        <input type="hidden" name="businessKey" value={cessao.businessKey} />
                        <input type="hidden" name="etapaNome" value={etapa.nomeEtapa} />
                        <input
                          type="hidden"
                          name="responsavelId"
                          value={permissionContext.actorId}
                        />
                        <Button type="submit">Avancar etapa</Button>
                      </form>
                    </PermissionGuard>
                  ) : (
                    <span className="text-sm text-text-subtle">Sem acao</span>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </section>
    </div>
  );
}
