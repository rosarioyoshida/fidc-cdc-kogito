type EmptyStateProps = {
  title: string;
  description: string;
};

export function EmptyState({ title, description }: EmptyStateProps) {
  return (
    <section className="rounded-lg border border-dashed bg-surface p-10 text-center">
      <h2 className="mb-3 text-2xl font-semibold">{title}</h2>
      <p className="mx-auto max-w-xl text-sm leading-6 text-text-subtle">{description}</p>
    </section>
  );
}
