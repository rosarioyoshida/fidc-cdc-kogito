import type { Metadata } from "next";
import "./globals.css";
import { ThemeToggle } from "@/components/ui/theme-toggle";
import { TopbarUserMenu } from "@/components/ui/topbar-user-menu";
import { getCurrentUserOptional } from "@/features/security/actions";

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
  const userPromise = getCurrentUserOptional();

  return (
    <html lang="pt-BR" data-theme="light" suppressHydrationWarning>
      <body className="text-text antialiased">
        <ThemeBootScript />
        <div className="mx-auto min-h-screen max-w-7xl px-6 py-8">
          <HeaderSlot userPromise={userPromise} />
          {children}
        </div>
      </body>
    </html>
  );
}

async function HeaderSlot({
  userPromise
}: {
  userPromise: ReturnType<typeof getCurrentUserOptional>;
}) {
  const user = await userPromise;

  return (
    <div className="mb-6 grid gap-4">
      <div className="flex justify-end">
        <ThemeToggle />
      </div>
      {user ? <TopbarUserMenu user={user} /> : null}
    </div>
  );
}
