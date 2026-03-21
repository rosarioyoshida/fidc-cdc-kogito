import React from "react";
import Link from "next/link";
import { CessaoStatusPanel } from "@/features/cessao/cessao-status-panel";
import type { Cessao } from "@/features/cessao/types";
import { CalculoPanel } from "@/features/analise/calculo-panel";
import { ContratosPanel } from "@/features/analise/contratos-panel";
import { ElegibilidadePanel } from "@/features/analise/elegibilidade-panel";
import { LastroPanel } from "@/features/analise/lastro-panel";
import { RegistradoraPanel } from "@/features/analise/registradora-panel";
import type { AnaliseSnapshot } from "@/features/analise/types";

type AnaliseDashboardProps = {
  cessao: Cessao;
  snapshot: AnaliseSnapshot;
  errorMessage?: string;
  successMessage?: string;
  avaliarElegibilidadeAction?: (formData: FormData) => void | Promise<void>;
  apurarValorAction?: (formData: FormData) => void | Promise<void>;
  registrarLastroAction?: (formData: FormData) => void | Promise<void>;
  validarLastrosAction?: (formData: FormData) => void | Promise<void>;
  executarRegistradoraAction?: (formData: FormData) => void | Promise<void>;
  refreshAction?: (formData: FormData) => void | Promise<void>;
};

export function AnaliseDashboard({
  cessao,
  snapshot,
  errorMessage,
  successMessage,
  avaliarElegibilidadeAction,
  apurarValorAction,
  registrarLastroAction,
  validarLastrosAction,
  executarRegistradoraAction,
  refreshAction
}: AnaliseDashboardProps) {
  return (
    <div className="grid gap-6">
      <header className="flex flex-col gap-4 rounded-lg border bg-surface-raised p-6 shadow-soft md:flex-row md:items-center md:justify-between">
        <div>
          <p className="text-sm font-semibold uppercase tracking-[0.18em] text-text-subtle">
            Analise da cessao
          </p>
          <h1 className="text-3xl font-semibold">{cessao.businessKey}</h1>
          <p className="mt-2 text-sm leading-6 text-text-subtle">
            Painel consolidado para elegibilidade, financeiro, documental e integracao com registradora.
          </p>
        </div>

        <div className="flex flex-wrap gap-3">
          <Link
            href={`/cessoes/${cessao.businessKey}`}
            className="inline-flex min-h-10 items-center justify-center rounded-md border bg-surface-raised px-4 py-2 text-sm font-semibold text-text transition hover:bg-surface"
          >
            Voltar para cessao
          </Link>

          <form action={refreshAction}>
            <input type="hidden" name="businessKey" value={cessao.businessKey} />
            <button
              type="submit"
              className="inline-flex min-h-10 items-center justify-center rounded-md border bg-surface px-4 py-2 text-sm font-semibold text-text transition hover:bg-surface-raised"
            >
              Atualizar painel
            </button>
          </form>
        </div>
      </header>

      {successMessage ? (
        <section className="rounded-lg border border-success bg-success/10 px-4 py-3 text-sm text-text">
          {successMessage}
        </section>
      ) : null}

      <CessaoStatusPanel cessao={cessao} errorMessage={errorMessage} />

      <div className="grid gap-6 xl:grid-cols-[1.05fr,0.95fr]">
        <ElegibilidadePanel
          businessKey={cessao.businessKey}
          regras={snapshot.regras}
          avaliarAction={avaliarElegibilidadeAction}
        />
        <CalculoPanel
          businessKey={cessao.businessKey}
          pagamentos={snapshot.pagamentos}
          apurarAction={apurarValorAction}
        />
      </div>

      <ContratosPanel contratos={snapshot.contratos} parcelas={snapshot.parcelas} />

      <div className="grid gap-6 xl:grid-cols-[1.1fr,0.9fr]">
        <LastroPanel
          businessKey={cessao.businessKey}
          lastros={snapshot.lastros}
          registrarAction={registrarLastroAction}
          validarAction={validarLastrosAction}
        />
        <RegistradoraPanel
          businessKey={cessao.businessKey}
          executarAction={executarRegistradoraAction}
        />
      </div>
    </div>
  );
}
