import { redirect } from "next/navigation";
import { AnaliseDashboard } from "@/features/analise/analise-dashboard";
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
    const [cessao, snapshot] = await Promise.all([
      getCessao(businessKey),
      getAnaliseSnapshot(businessKey)
    ]);

    return (
      <main>
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
          refreshAction={refreshAnaliseAction}
        />
      </main>
    );
  } catch (error) {
    if (error instanceof ApiError && error.status === 401) {
      redirect(`/?error=${encodeURIComponent(error.message)}`);
    }
    throw error;
  }
}
