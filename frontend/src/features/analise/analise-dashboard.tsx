import React from "react";
import { PageSection } from "@/components/layout/page-section";
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
  executarRegistradoraAction
}: AnaliseDashboardProps) {
  return (
    <div className="grid gap-6">
      <PageSection
        title={`Analise ${cessao.businessKey}`}
        description="Painel consolidado para elegibilidade, financeiro, documental e integracao com registradora."
      >
        <CessaoStatusPanel cessao={cessao} errorMessage={errorMessage} />
      </PageSection>

      {successMessage ? (
        <section className="rounded-lg border border-success bg-success/10 px-4 py-3 text-sm text-text">
          {successMessage}
        </section>
      ) : null}

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
