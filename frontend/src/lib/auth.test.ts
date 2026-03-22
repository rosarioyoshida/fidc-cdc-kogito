import {
  applyThemeMode,
  normalizeThemeMode,
  resolveBrowserThemeMode,
  THEME_COOKIE_NAME,
  THEME_STORAGE_KEY
} from "@/lib/auth";

describe("theme helpers", () => {
  beforeEach(() => {
    document.documentElement.dataset.theme = "light";
    window.localStorage.clear();
    document.cookie = `${THEME_COOKIE_NAME}=; path=/; max-age=0`;
  });

  it("normalizes invalid theme values to light", () => {
    expect(normalizeThemeMode("dark")).toBe("dark");
    expect(normalizeThemeMode("unexpected")).toBe("light");
    expect(normalizeThemeMode(undefined)).toBe("light");
  });

  it("applies theme mode to document, localStorage and cookie", () => {
    applyThemeMode("dark");

    expect(document.documentElement.dataset.theme).toBe("dark");
    expect(window.localStorage.getItem(THEME_STORAGE_KEY)).toBe("dark");
    expect(document.cookie).toContain(`${THEME_COOKIE_NAME}=dark`);
  });

  it("resolves browser theme mode from the active document theme", () => {
    document.documentElement.dataset.theme = "dark";
    window.localStorage.setItem(THEME_STORAGE_KEY, "light");

    expect(resolveBrowserThemeMode()).toBe("dark");
  });
});
