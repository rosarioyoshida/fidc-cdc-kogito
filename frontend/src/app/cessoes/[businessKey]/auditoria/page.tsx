import Link from "next/link";
import { redirect } from "next/navigation";
import { AppShell } from "@/components/layout/app-shell";
import { PageSection } from "@/components/layout/page-section";
import { getAuditTrail } from "@/features/auditoria/actions";
import { AuditTimeline } from "@/features/auditoria/audit-timeline";
import { TaskContextPanel } from "@/features/auditoria/task-context-panel";
import { getCessao } from "@/features/cessao/actions";
import {
  buildCessaoContextNavigation,
  buildPrimaryAction,
  primaryNavigation
} from "@/features/navigation/shell-config";
import { getCurrentUser, getPermissionContext } from "@/features/security/actions";
import { ApiError } from "@/lib/api-client";

type AuditoriaPageProps = {
  params: Promise<{ businessKey: string }>;
};

export default async function AuditoriaPage({ params }: AuditoriaPageProps) {
  const { businessKey } = await params;
  try {
    const [user, cessao, permissionContext, auditTrail] = await Promise.all([
      getCurrentUser(),
      getCessao(businessKey),
      getPermissionContext(businessKey),
      getAuditTrail(businessKey)
    ]);

    return (
      <AppShell
        user={user}
        navigation={primaryNavigation}
        primaryAction={buildPrimaryAction()}
        secondaryNav={buildCessaoContextNavigation(businessKey)}
        secondaryControls={
          <>
            <Link
              href={`/cessoes/${cessao.businessKey}`}
              className="inline-flex min-h-10 items-center justify-center rounded-full border border-border bg-surface-raised px-4 py-2 text-sm font-semibold text-text transition hover:bg-surface"
            >
              Voltar para cessao
            </Link>
            <Link
              href={`/cessoes/${cessao.businessKey}/analise`}
              className="inline-flex min-h-10 items-center justify-center rounded-full border border-border bg-surface-raised px-4 py-2 text-sm font-semibold text-text transition hover:bg-surface"
            >
              Voltar para analise
            </Link>
          </>
        }
      >
        <PageSection
          title={`Auditoria ${cessao.businessKey}`}
          description="Consulta auditavel, contexto de task console e visibilidade operacional do processo."
        >
          <TaskContextPanel permissionContext={permissionContext} />
        </PageSection>
        <AuditTimeline items={auditTrail} />
      </AppShell>
    );
  } catch (error) {
    if (error instanceof ApiError && error.status === 401) {
      redirect(`/?error=${encodeURIComponent(error.message)}`);
    }
    throw error;
  }
}
