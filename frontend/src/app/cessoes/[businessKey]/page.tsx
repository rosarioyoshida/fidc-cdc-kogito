import { EmptyState } from "@/components/feedback/empty-state";

type CessaoDetailPageProps = {
  params: Promise<{ businessKey: string }>;
};

export default async function CessaoDetailPage({ params }: CessaoDetailPageProps) {
  const { businessKey } = await params;

  return (
    <main className="grid gap-6">
      <header className="rounded-lg border bg-surface-raised p-6 shadow-soft">
        <p className="text-sm font-semibold uppercase tracking-[0.18em] text-text-subtle">
          Cessao
        </p>
        <h1 className="text-3xl font-semibold">{businessKey}</h1>
      </header>

      <EmptyState
        title="Detalhe em preparacao"
        description="A jornada ponta a ponta desta cessao sera habilitada na implementacao da US1."
      />
    </main>
  );
}
