import { redirect } from "next/navigation";
import { AppShell } from "@/components/layout/app-shell";
import { Button } from "@/components/ui/button";
import { AnaliseDashboard } from "@/features/analise/analise-dashboard";
import {
  buildCessaoContextNavigation,
  buildPrimaryAction,
  primaryNavigation
} from "@/features/navigation/shell-config";
import {
  apurarValorAction,
  avaliarElegibilidadeAction,
  executarRegistradoraAction,
  getAnaliseSnapshot,
  refreshAnaliseAction,
  registrarLastroAction,
  validarLastrosAction
} from "@/features/analise/actions";
import { getCessao } from "@/features/cessao/actions";
import { getCurrentUser } from "@/features/security/actions";
import { ApiError } from "@/lib/api-client";

type AnalisePageProps = {
  params: Promise<{ businessKey: string }>;
  searchParams?: Promise<Record<string, string | string[] | undefined>>;
};

export default async function AnalisePage({ params, searchParams }: AnalisePageProps) {
  const { businessKey } = await params;
  const query = (await searchParams) ?? {};
  const errorMessage =
    typeof query.error === "string" ? decodeURIComponent(query.error) : undefined;
  const successMessage =
    typeof query.message === "string" ? decodeURIComponent(query.message) : undefined;

  try {
    const [user, cessao, snapshot] = await Promise.all([
      getCurrentUser(),
      getCessao(businessKey),
      getAnaliseSnapshot(businessKey)
    ]);

    return (
      <AppShell
        user={user}
        navigation={primaryNavigation}
        primaryAction={buildPrimaryAction()}
        secondaryNav={buildCessaoContextNavigation(businessKey)}
        secondaryControls={
          <form action={refreshAnaliseAction}>
            <input type="hidden" name="businessKey" value={businessKey} />
            <Button type="submit" variant="secondary">
              Atualizar painel
            </Button>
          </form>
        }
      >
        <AnaliseDashboard
          cessao={cessao}
          snapshot={snapshot}
          errorMessage={errorMessage}
          successMessage={successMessage}
          avaliarElegibilidadeAction={avaliarElegibilidadeAction}
          apurarValorAction={apurarValorAction}
          registrarLastroAction={registrarLastroAction}
          validarLastrosAction={validarLastrosAction}
          executarRegistradoraAction={executarRegistradoraAction}
        />
      </AppShell>
    );
  } catch (error) {
    if (error instanceof ApiError && error.status === 401) {
      redirect(`/?error=${encodeURIComponent(error.message)}`);
    }
    throw error;
  }
}
