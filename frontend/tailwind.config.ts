import type { Config } from "tailwindcss";

const config: Config = {
  darkMode: ["class", "[data-theme='dark']"],
  content: [
    "./src/app/**/*.{ts,tsx}",
    "./src/components/**/*.{ts,tsx}",
    "./src/features/**/*.{ts,tsx}"
  ],
  theme: {
    extend: {
      colors: {
        surface: "var(--ds-surface)",
        "surface-raised": "var(--ds-surface-raised)",
        text: "var(--ds-text)",
        "text-subtle": "var(--ds-text-subtle)",
        brand: "var(--ds-brand)",
        success: "var(--ds-success)",
        warning: "var(--ds-warning)",
        danger: "var(--ds-danger)",
        border: "var(--ds-border)"
      },
      borderRadius: {
        md: "var(--ds-radius-md)",
        lg: "var(--ds-radius-lg)"
      },
      boxShadow: {
        soft: "var(--ds-shadow-soft)"
      }
    }
  },
  plugins: []
};

export default config;
