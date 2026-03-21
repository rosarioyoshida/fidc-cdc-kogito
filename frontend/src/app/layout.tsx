import type { Metadata } from "next";
import "./globals.css";
import { ThemeToggle } from "@/components/ui/theme-toggle";

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
    <html lang="pt-BR" data-theme="light" suppressHydrationWarning>
      <body className="text-text antialiased">
        <ThemeBootScript />
        <div className="mx-auto min-h-screen max-w-7xl px-6 py-8">
          <div className="mb-6 flex justify-end">
            <ThemeToggle />
          </div>
          {children}
        </div>
      </body>
    </html>
  );
}
