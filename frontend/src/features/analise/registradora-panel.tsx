import React from "react";
import { Button } from "@/components/ui/button";
import { registradoraOperations, type RegistradoraOperation } from "@/features/analise/types";

type RegistradoraPanelProps = {
  businessKey: string;
  executarAction?: (formData: FormData) => void | Promise<void>;
};

const descriptions: Record<RegistradoraOperation, string> = {
  CARTEIRA: "Sincroniza carteira base da cessao com a registradora.",
  CONTRATO: "Envia contratos importados para registro externo.",
  PARCELA: "Envia parcelas associadas aos contratos aprovados.",
  OFERTA: "Submete oferta financeira calculada para a cessao.",
  ACEITE: "Publica termo e aceite operacional no canal externo.",
  PAGAMENTO: "Compartilha status financeiro da liberacao de pagamento.",
  LASTRO: "Envia o resumo documental e status de validacao dos lastros."
};

type OperationCardProps = {
  businessKey: string;
  operation: RegistradoraOperation;
  executarAction?: (formData: FormData) => void | Promise<void>;
};

function OperationCard({ businessKey, operation, executarAction }: OperationCardProps) {
  return (
    <article className="rounded-md border bg-surface p-4">
      <div className="mb-3">
        <p className="text-xs uppercase tracking-[0.16em] text-text-subtle">Operacao</p>
        <h3 className="mt-1 text-lg font-semibold">{operation}</h3>
      </div>

      <p className="mb-4 text-sm leading-6 text-text-subtle">{descriptions[operation]}</p>

      <form action={executarAction}>
        <input type="hidden" name="businessKey" value={businessKey} />
        <input type="hidden" name="tipoOperacao" value={operation} />
        <Button type="submit" variant="secondary" className="w-full">
          Enviar {operation.toLowerCase()}
        </Button>
      </form>
    </article>
  );
}

export function RegistradoraPanel({
  businessKey,
  executarAction
}: RegistradoraPanelProps) {
  return (
    <section className="rounded-lg border bg-surface-raised p-6 shadow-soft">
      <div className="mb-4">
        <h2 className="text-xl font-semibold">Registradora</h2>
        <p className="text-sm leading-6 text-text-subtle">
          Integracao REST sincrona com retry automatico limitado e rastreamento de evidencias por tentativa.
        </p>
      </div>

      <div className="mb-4 rounded-md border border-dashed bg-surface p-4 text-sm leading-6 text-text-subtle">
        O backend executa ate 3 tentativas controladas antes de escalar a falha operacional.
      </div>

      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {registradoraOperations.map((operation) => (
          <OperationCard
            key={operation}
            businessKey={businessKey}
            operation={operation}
            executarAction={executarAction}
          />
        ))}
      </div>
    </section>
  );
}
