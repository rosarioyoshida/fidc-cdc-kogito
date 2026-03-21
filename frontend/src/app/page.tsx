import Link from "next/link";

export default function HomePage() {
  return (
    <main className="grid gap-8">
      <section className="rounded-lg border bg-surface-raised p-8 shadow-soft">
        <p className="mb-3 text-sm font-semibold uppercase tracking-[0.18em] text-text-subtle">
          Controle de Cessao de FIDC
        </p>
        <h1 className="mb-4 text-4xl font-semibold text-text">
          Runtime transacional, consulta operacional e governanca visual alinhados.
        </h1>
        <p className="max-w-3xl text-base leading-7 text-text-subtle">
          Esta base inicial organiza o frontend operacional em Next.js com tokens visuais
          alinhados ao Design System da Atlassian, suporte a tema claro/escuro e integrações
          preparadas para a API versionada do backend.
        </p>
      </section>

      <section className="grid gap-4 md:grid-cols-2">
        <Link
          className="rounded-lg border bg-surface p-6 transition hover:-translate-y-0.5 hover:shadow-soft"
          href="/cessoes"
        >
          <h2 className="mb-2 text-xl font-semibold">Operacao de cessoes</h2>
          <p className="text-sm leading-6 text-text-subtle">
            Lista operacional, detalhe do fluxo, estados de carregamento e falhas RFC 9457.
          </p>
        </Link>

        <div className="rounded-lg border bg-surface p-6">
          <h2 className="mb-2 text-xl font-semibold">Consoles Kogito</h2>
          <p className="text-sm leading-6 text-text-subtle">
            O plano mantem Task Console e Management Console como canais especializados,
            distintos do frontend de negocio.
          </p>
        </div>
      </section>
    </main>
  );
}
