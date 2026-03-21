import { CessaoList } from "@/features/cessao/cessao-list";
import { createCessaoAction, listCessoes } from "@/features/cessao/actions";
import type { Cessao } from "@/features/cessao/types";

type CessoesPageProps = {
  searchParams?: Promise<Record<string, string | string[] | undefined>>;
};

export default async function CessoesPage({ searchParams }: CessoesPageProps) {
  const params = (await searchParams) ?? {};
  const errorMessage =
    typeof params.error === "string" ? decodeURIComponent(params.error) : undefined;

  let items: Cessao[] = [];
  let queryError = errorMessage;

  try {
    items = await listCessoes();
  } catch (error) {
    if (!queryError) {
      queryError = error instanceof Error ? error.message : "Falha ao consultar cessoes.";
    }
  }

  return (
    <main className="grid gap-6">
      <header className="flex items-center justify-between rounded-lg border bg-surface-raised p-6 shadow-soft">
        <div>
          <p className="text-sm font-semibold uppercase tracking-[0.18em] text-text-subtle">
            Operacao
          </p>
          <h1 className="text-3xl font-semibold">Cessoes</h1>
        </div>
      </header>

      <CessaoList items={items} errorMessage={queryError} createAction={createCessaoAction} />
    </main>
  );
}
