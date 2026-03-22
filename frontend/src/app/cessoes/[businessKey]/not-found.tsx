import Link from "next/link";

export default function CessaoNotFound() {
  return (
    <main className="mx-auto flex min-h-[60vh] max-w-3xl flex-col items-start justify-center gap-4 px-6">
      <p className="text-sm font-semibold uppercase tracking-[0.18em] text-text-subtle">
        Cessao
      </p>
      <h1 className="text-3xl font-semibold text-text">Cessao nao encontrada</h1>
      <p className="max-w-2xl text-sm leading-6 text-text-subtle">
        A chave informada nao corresponde a uma cessao disponivel no ambiente atual. Revise o
        business key ou volte para a lista operacional.
      </p>
      <Link
        href="/cessoes"
        className="inline-flex min-h-10 items-center justify-center rounded-md border bg-surface-raised px-4 py-2 text-sm font-semibold text-text transition hover:bg-surface"
      >
        Voltar para cessoes
      </Link>
    </main>
  );
}
