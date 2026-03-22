import React from "react";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it } from "vitest";
import { THEME_STORAGE_KEY } from "@/lib/auth";
import { ThemeToggle } from "@/components/ui/theme-toggle";

describe("ThemeToggle", () => {
  it("toggles theme state and preserves semantic labeling", async () => {
    const user = userEvent.setup();
    document.documentElement.dataset.theme = "light";
    window.localStorage.setItem(THEME_STORAGE_KEY, "light");

    render(<ThemeToggle />);

    const toggle = screen.getByRole("button", { name: "Ativar tema escuro" });
    expect(toggle).toHaveTextContent("Modo claro");

    await user.click(toggle);

    expect(document.documentElement.dataset.theme).toBe("dark");
    expect(window.localStorage.getItem(THEME_STORAGE_KEY)).toBe("dark");
    expect(screen.getByRole("button", { name: "Ativar tema claro" })).toHaveTextContent("Modo escuro");
  });
});
