export const colorSemantics = {
  status: {
    success: "var(--ds-success)",
    warning: "var(--ds-warning)",
    danger: "var(--ds-danger)",
    info: "var(--ds-info)"
  },
  emphasis: {
    primary: "var(--ds-brand)",
    subtleText: "var(--ds-text-subtle)"
  },
  surface: {
    canvas: "var(--ds-surface)",
    raised: "var(--ds-surface-raised)",
    muted: "var(--ds-surface-muted)"
  }
} as const;
