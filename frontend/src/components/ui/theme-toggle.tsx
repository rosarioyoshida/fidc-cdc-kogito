"use client";

import React from "react";
import { Button } from "@/components/ui/button";

export function ThemeToggle() {
  const [theme, setTheme] = React.useState<"light" | "dark">("light");

  React.useEffect(() => {
    const current = document.documentElement.dataset.theme === "dark" ? "dark" : "light";
    setTheme(current);
  }, []);

  function toggleTheme() {
    const next = theme === "light" ? "dark" : "light";
    document.documentElement.dataset.theme = next;
    window.localStorage.setItem("fidc-theme", next);
    setTheme(next);
  }

  return (
    <Button
      type="button"
      variant="secondary"
      aria-label={theme === "light" ? "Ativar tema escuro" : "Ativar tema claro"}
      onClick={toggleTheme}
    >
      {theme === "light" ? "Modo escuro" : "Modo claro"}
    </Button>
  );
}
