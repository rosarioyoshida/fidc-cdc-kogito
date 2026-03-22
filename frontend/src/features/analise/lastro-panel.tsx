import React from "react";
import { EmptyState } from "@/components/feedback/empty-state";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from "@/components/ui/table";
import type { LastroAnalise } from "@/features/analise/types";

type LastroPanelProps = {
  businessKey: string;
  lastros: LastroAnalise[];
  registrarAction?: (formData: FormData) => void | Promise<void>;
  validarAction?: (formData: FormData) => void | Promise<void>;
};

export function LastroPanel({
  businessKey,
  lastros,
  registrarAction,
  validarAction
}: LastroPanelProps) {
  const rejeitados = lastros.filter((lastro) => lastro.statusValidacao === "REJEITADO").length;

  return (
    <section className="rounded-lg border bg-surface-raised p-6 shadow-soft">
      <div className="mb-4 flex flex-col gap-3 md:flex-row md:items-start md:justify-between">
        <div>
          <h2 className="text-xl font-semibold">Lastros e historico documental</h2>
          <p className="text-sm leading-6 text-text-subtle">
            Registra recebimento documental, inconsistencias e bloqueios antes do aceite final.
          </p>
        </div>

        <form action={validarAction}>
          <input type="hidden" name="businessKey" value={businessKey} />
          <Button type="submit" variant="secondary">
            Validar lastros
          </Button>
        </form>
      </div>

      <form action={registrarAction} className="mb-5 grid gap-3 rounded-md border bg-surface p-4 md:grid-cols-5">
        <input type="hidden" name="businessKey" value={businessKey} />
        <label className="grid gap-2 md:col-span-2">
          <span className="text-xs font-semibold uppercase tracking-[0.16em] text-text-subtle">
            Tipo do documento
          </span>
          <Input name="tipoDocumento" placeholder="Ex.: Duplicata, NF-e" required />
        </label>
        <label className="grid gap-2 md:col-span-2">
          <span className="text-xs font-semibold uppercase tracking-[0.16em] text-text-subtle">
            Origem
          </span>
          <Input name="origem" placeholder="Cedente, registradora, parceiro" required />
        </label>
        <div className="flex items-end">
          <Button type="submit" className="w-full">
            Registrar lastro
          </Button>
        </div>
        <label className="grid gap-2">
          <span className="text-xs font-semibold uppercase tracking-[0.16em] text-text-subtle">
            Contrato ID
          </span>
          <Input name="contratoId" placeholder="Opcional" />
        </label>
        <label className="grid gap-2">
          <span className="text-xs font-semibold uppercase tracking-[0.16em] text-text-subtle">
            Parcela ID
          </span>
          <Input name="parcelaId" placeholder="Opcional" />
        </label>
      </form>

      <div className="mb-4 grid gap-3 md:grid-cols-3">
        <article className="rounded-md border bg-surface p-4">
          <p className="text-xs uppercase tracking-[0.16em] text-text-subtle">Lastros recebidos</p>
          <p className="mt-2 text-2xl font-semibold">{lastros.length}</p>
        </article>
        <article className="rounded-md border bg-surface p-4">
          <p className="text-xs uppercase tracking-[0.16em] text-text-subtle">Rejeitados</p>
          <p className="mt-2 text-2xl font-semibold text-danger">{rejeitados}</p>
        </article>
        <article className="rounded-md border bg-surface p-4">
          <p className="text-xs uppercase tracking-[0.16em] text-text-subtle">Prontos para aceite</p>
          <p className="mt-2 text-2xl font-semibold">
            {lastros.filter((lastro) => lastro.statusValidacao === "VALIDADO").length}
          </p>
        </article>
      </div>

      {lastros.length === 0 ? (
        <EmptyState
          title="Nenhum lastro recebido"
          description="A trilha documental ainda nao recebeu comprovacoes para a cessao."
        />
      ) : (
        <Table>
          <TableHeader className="bg-surface">
            <TableRow>
              <TableHead>Documento</TableHead>
              <TableHead>Origem</TableHead>
              <TableHead>Status</TableHead>
              <TableHead>Recebido em</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {lastros.map((lastro) => (
              <TableRow key={lastro.id}>
                <TableCell className="font-semibold">{lastro.tipoDocumento}</TableCell>
                <TableCell>{lastro.origem}</TableCell>
                <TableCell>{lastro.statusValidacao}</TableCell>
                <TableCell className="text-text-subtle">{lastro.recebidoEm ?? "Agora"}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}
    </section>
  );
}
