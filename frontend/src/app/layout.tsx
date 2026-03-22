import type { Metadata } from "next";
import { cookies } from "next/headers";
import "./globals.css";
import { TopbarUserMenu } from "@/components/ui/topbar-user-menu";
import { getCurrentUserOptional } from "@/features/security/actions";
import { THEME_COOKIE_NAME, normalizeThemeMode } from "@/lib/auth";

export const metadata: Metadata = {
  title: "FIDC CDC Kogito",
  description: "Operacao de cessao de FIDC com workflow, auditoria e monitoramento"
};

function ThemeBootScript({ initialTheme }: { initialTheme: "light" | "dark" }) {
  const script = `
    (() => {
      const saved = window.localStorage.getItem('fidc-theme');
      const theme = saved ?? '${initialTheme}';
      document.documentElement.dataset.theme = theme;
      document.cookie = 'fidc_theme=' + theme + '; path=/; max-age=31536000; samesite=lax';
    })();
  `;
  return <script dangerouslySetInnerHTML={{ __html: script }} />;
}

export default async function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  const cookieStore = await cookies();
  const initialTheme = normalizeThemeMode(cookieStore.get(THEME_COOKIE_NAME)?.value);
  const userPromise = getCurrentUserOptional();

  return (
    <html lang="pt-BR" data-theme={initialTheme} suppressHydrationWarning>
      <body className="text-text antialiased">
        <ThemeBootScript initialTheme={initialTheme} />
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

  return <div className="mb-6">{user ? <TopbarUserMenu user={user} /> : null}</div>;
}
