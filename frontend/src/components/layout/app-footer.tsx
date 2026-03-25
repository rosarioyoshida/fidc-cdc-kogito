import React from "react";
import Link from "next/link";

const footerLinks = [
  { label: "Requisitos", href: "/cessoes" },
  { label: "Termos", href: "/cessoes" },
  { label: "Privacidade", href: "/cessoes" },
  { label: "Ajuda", href: "/cessoes" }
];

export function AppFooter({ compact = false }: { compact?: boolean }) {
  return (
    <footer className={compact ? "border-t border-border/70 px-6 py-4" : "border-t border-border/70 bg-surface-raised/90"}>
      <div className={compact ? "mx-auto flex max-w-5xl flex-col gap-4 text-sm text-text-subtle md:flex-row md:items-center md:justify-between" : "mx-auto flex max-w-7xl flex-col gap-4 px-6 py-5 text-sm text-text-subtle md:flex-row md:items-center md:justify-between"}>
        <div className="flex items-center gap-3">
          <span className="inline-flex h-8 w-8 items-center justify-center rounded-full bg-brand/12 text-sm font-semibold text-brand">
            FC
          </span>
          <div>
            <p className="font-semibold text-text">FIDC CDC Kogito</p>
            <p>Shell compartilhado para operação, análise e auditoria.</p>
          </div>
        </div>

        <nav className="flex flex-wrap gap-4">
          {footerLinks.map((link) => (
            <Link key={link.label} href={link.href} className="transition hover:text-text">
              {link.label}
            </Link>
          ))}
        </nav>
      </div>
    </footer>
  );
}
