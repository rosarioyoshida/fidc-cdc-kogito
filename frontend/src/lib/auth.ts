export const AUTH_COOKIE_NAME = "fidc_auth";

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
