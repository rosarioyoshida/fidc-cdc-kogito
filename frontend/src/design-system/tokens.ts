export const tokens = {
  light: {
    surface: "#f7f8fa",
    surfaceRaised: "#ffffff",
    text: "#172b4d",
    textSubtle: "#5e6c84",
    brand: "#0c66e4",
    success: "#216e4e",
    warning: "#a54800",
    danger: "#c9372c",
    border: "#dfe1e6",
    shellHeader: "rgba(255, 255, 255, 0.88)",
    shellAccent: "rgba(12, 102, 228, 0.12)"
  },
  dark: {
    surface: "#0f172a",
    surfaceRaised: "#111827",
    text: "#e5e7eb",
    textSubtle: "#94a3b8",
    brand: "#85b8ff",
    success: "#7ee2b8",
    warning: "#f5cd47",
    danger: "#fd9891",
    border: "#334155",
    shellHeader: "rgba(17, 24, 39, 0.9)",
    shellAccent: "rgba(133, 184, 255, 0.16)"
  },
  radius: {
    md: "12px",
    lg: "18px",
    xl: "28px"
  },
  shadow: {
    soft: "0 12px 40px rgba(15, 23, 42, 0.08)"
  }
} as const;
