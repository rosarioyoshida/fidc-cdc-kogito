export default function CessaoDetailLoading() {
  return (
    <main className="mx-auto flex min-h-[60vh] w-full max-w-7xl flex-col gap-6 px-6 py-8">
      <section className="rounded-[28px] border border-border/80 bg-surface-raised p-6 shadow-soft">
        <p className="text-sm font-semibold uppercase tracking-[0.18em] text-text-subtle">
          Atualizando detalhe da cessao
        </p>
        <div className="mt-4 h-32 animate-pulse rounded-2xl bg-surface" />
      </section>
      <section className="rounded-[28px] border border-border/80 bg-surface-raised p-6 shadow-soft">
        <div className="h-40 animate-pulse rounded-2xl bg-surface" />
      </section>
    </main>
  );
}
