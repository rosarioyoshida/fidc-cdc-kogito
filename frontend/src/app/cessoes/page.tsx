import { EmptyState } from "@/components/feedback/empty-state";

export default function CessoesPage() {
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

      <EmptyState
        title="Nenhuma cessao carregada"
        description="A lista operacional sera preenchida quando os endpoints da US1 estiverem ativos."
      />
    </main>
  );
}
