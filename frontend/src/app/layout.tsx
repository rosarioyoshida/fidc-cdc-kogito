import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "FIDC CDC Kogito",
  description: "Operacao de cessao de FIDC com workflow, auditoria e monitoramento"
};

function ThemeBootScript() {
  const script = `
    (() => {
      const saved = window.localStorage.getItem('fidc-theme');
      const theme = saved ?? 'light';
      document.documentElement.dataset.theme = theme;
    })();
  `;
  return <script dangerouslySetInnerHTML={{ __html: script }} />;
}

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="pt-BR" data-theme="light">
      <body className="text-text antialiased">
        <ThemeBootScript />
        <div className="mx-auto min-h-screen max-w-7xl px-6 py-8">{children}</div>
      </body>
    </html>
  );
}
