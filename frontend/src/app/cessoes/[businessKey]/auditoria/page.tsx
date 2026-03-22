import Link from "next/link";
import { redirect } from "next/navigation";
import { getAuditTrail } from "@/features/auditoria/actions";
import { AuditTimeline } from "@/features/auditoria/audit-timeline";
import { TaskContextPanel } from "@/features/auditoria/task-context-panel";
import { getCessao } from "@/features/cessao/actions";
import { getPermissionContext } from "@/features/security/actions";
import { ApiError } from "@/lib/api-client";

type AuditoriaPageProps = {
  params: Promise<{ businessKey: string }>;
};

export default async function AuditoriaPage({ params }: AuditoriaPageProps) {
  const { businessKey } = await params;
  try {
    const [cessao, permissionContext, auditTrail] = await Promise.all([
      getCessao(businessKey),
      getPermissionContext(businessKey),
      getAuditTrail(businessKey)
    ]);

    return (
      <main className="grid gap-6">
        <header className="flex flex-col gap-4 rounded-lg border bg-surface-raised p-6 shadow-soft md:flex-row md:items-center md:justify-between">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.18em] text-text-subtle">
              Auditoria e segregacao
            </p>
            <h1 className="text-3xl font-semibold">{cessao.businessKey}</h1>
            <p className="mt-2 text-sm leading-6 text-text-subtle">
              Consulta auditavel, contexto de task console e visibilidade operacional do processo.
            </p>
          </div>

          <div className="flex flex-wrap gap-3">
            <Link
              href={`/cessoes/${cessao.businessKey}`}
              className="inline-flex min-h-10 items-center justify-center rounded-md border bg-surface-raised px-4 py-2 text-sm font-semibold text-text transition hover:bg-surface"
            >
              Voltar para cessao
            </Link>
            <Link
              href={`/cessoes/${cessao.businessKey}/analise`}
              className="inline-flex min-h-10 items-center justify-center rounded-md border bg-surface-raised px-4 py-2 text-sm font-semibold text-text transition hover:bg-surface"
            >
              Voltar para analise
            </Link>
          </div>
        </header>

        <TaskContextPanel permissionContext={permissionContext} />
        <AuditTimeline items={auditTrail} />
      </main>
    );
  } catch (error) {
    if (error instanceof ApiError && error.status === 401) {
      redirect(`/?error=${encodeURIComponent(error.message)}`);
    }
    throw error;
  }
}
