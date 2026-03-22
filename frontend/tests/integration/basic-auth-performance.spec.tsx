import React from "react";
import { render } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { LoginPanel } from "@/features/security/login-panel";

describe("basic auth performance", () => {
  it("renders login entry point well below the one-minute target", () => {
    const startedAt = performance.now();
    render(<LoginPanel signInAction={vi.fn()} />);
    const elapsed = performance.now() - startedAt;

    expect(elapsed).toBeLessThan(60_000);
  });
});
