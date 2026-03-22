export const AUTH_COOKIE_NAME = "fidc_auth";
export const THEME_COOKIE_NAME = "fidc_theme";
export const THEME_STORAGE_KEY = "fidc-theme";

export type ThemeMode = "light" | "dark";

export type AuthSession = {
  username: string;
  password: string;
};

export const SEEDED_USER_FIXTURES = {
  operador: { username: "operador", password: "operador123", perfil: "OPERADOR" },
  analista: { username: "analista", password: "analista123", perfil: "ANALISTA" },
  aprovador: { username: "aprovador", password: "aprovador123", perfil: "APROVADOR" },
  auditor: { username: "auditor", password: "auditor123", perfil: "AUDITOR" },
  integracao: { username: "integracao", password: "integracao123", perfil: "INTEGRACAO" }
} as const;

export function buildBasicAuthHeader(username: string, password: string) {
  const token = Buffer.from(`${username}:${password}`).toString("base64");
  return `Basic ${token}`;
}

export function serializeAuthSession(session: AuthSession) {
  return Buffer.from(JSON.stringify(session)).toString("base64url");
}

export function deserializeAuthSession(rawValue?: string | null): AuthSession | null {
  if (!rawValue) {
    return null;
  }

  try {
    const payload = Buffer.from(rawValue, "base64url").toString("utf8");
    const parsed = JSON.parse(payload) as Partial<AuthSession>;
    if (!parsed.username || !parsed.password) {
      return null;
    }
    return { username: parsed.username, password: parsed.password };
  } catch {
    return null;
  }
}

export function readAuthCookieFromDocument() {
  if (typeof document === "undefined") {
    return null;
  }

  const cookie = document.cookie
    .split(";")
    .map((value) => value.trim())
    .find((value) => value.startsWith(`${AUTH_COOKIE_NAME}=`));

  return cookie ? decodeURIComponent(cookie.substring(AUTH_COOKIE_NAME.length + 1)) : null;
}

export function getBrowserAuthHeader() {
  const session = deserializeAuthSession(readAuthCookieFromDocument());
  return session ? buildBasicAuthHeader(session.username, session.password) : null;
}

export function normalizeThemeMode(value?: string | null): ThemeMode {
  return value === "dark" ? "dark" : "light";
}

export function applyThemeMode(theme: ThemeMode) {
  if (typeof document === "undefined") {
    return;
  }

  document.documentElement.dataset.theme = theme;
  window.localStorage.setItem(THEME_STORAGE_KEY, theme);
  document.cookie = `${THEME_COOKIE_NAME}=${theme}; path=/; max-age=31536000; samesite=lax`;
}

export function resolveBrowserThemeMode(): ThemeMode {
  if (typeof document === "undefined") {
    return "light";
  }

  const documentTheme = normalizeThemeMode(document.documentElement.dataset.theme);
  const storedTheme = normalizeThemeMode(window.localStorage.getItem(THEME_STORAGE_KEY));
  const cookieTheme = normalizeThemeMode(readCookieValue(THEME_COOKIE_NAME));

  if (document.documentElement.dataset.theme) {
    return documentTheme;
  }

  if (window.localStorage.getItem(THEME_STORAGE_KEY)) {
    return storedTheme;
  }

  return cookieTheme;
}

function readCookieValue(name: string) {
  if (typeof document === "undefined") {
    return null;
  }

  const cookie = document.cookie
    .split(";")
    .map((value) => value.trim())
    .find((value) => value.startsWith(`${name}=`));

  return cookie ? decodeURIComponent(cookie.substring(name.length + 1)) : null;
}
