"use client";

import React from "react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/cn";
import { applyThemeMode, resolveBrowserThemeMode, type ThemeMode } from "@/lib/auth";

type ThemeToggleProps = {
  className?: string;
};

export function ThemeToggle({ className }: ThemeToggleProps) {
  const [theme, setTheme] = React.useState<ThemeMode>("light");

  React.useEffect(() => {
    const current = resolveBrowserThemeMode();
    applyThemeMode(current);
    setTheme(current);
  }, []);

  function toggleTheme() {
    const next = theme === "light" ? "dark" : "light";
    applyThemeMode(next);
    setTheme(next);
  }

  return (
    <Button
      type="button"
      variant="secondary"
      className={cn("gap-2", className)}
      aria-label={theme === "light" ? "Ativar tema escuro" : "Ativar tema claro"}
      onClick={toggleTheme}
      title={theme === "light" ? "Alternar para modo escuro" : "Alternar para modo claro"}
    >
      <span aria-hidden="true">{theme === "light" ? "☀" : "☾"}</span>
      {theme === "light" ? "Modo claro" : "Modo escuro"}
    </Button>
  );
}
