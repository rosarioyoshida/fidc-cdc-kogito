export default function CessoesLoading() {
  return (
    <section className="rounded-lg border bg-surface-raised p-6 shadow-soft">
      <p className="text-sm font-semibold uppercase tracking-[0.18em] text-text-subtle">
        Carregando operacao
      </p>
      <div className="mt-4 h-24 animate-pulse rounded-lg bg-surface" />
    </section>
  );
}
