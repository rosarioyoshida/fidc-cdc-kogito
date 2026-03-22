import { buildBasicAuthHeader, deserializeAuthSession, getBrowserAuthHeader } from "@/lib/auth";
import { parseProblemDetails } from "@/lib/problem-details";

const API_BASE_URL =
  process.env.FIDC_API_INTERNAL_URL ??
  process.env.NEXT_PUBLIC_FIDC_API_URL ??
  "http://localhost:8080/api/v1";

export class ApiError extends Error {
  constructor(
    message: string,
    public readonly status: number,
    public readonly payload: unknown
  ) {
    super(message);
  }
}

export async function apiFetch<T>(path: string, init?: RequestInit): Promise<T> {
  let response: Response;
  const authorization = await resolveAuthorizationHeader(init?.headers);

  try {
    response = await fetch(`${API_BASE_URL}${path}`, {
      ...init,
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        ...(authorization ? { Authorization: authorization } : {}),
        ...init?.headers
      },
      cache: "no-store"
    });
  } catch (error) {
    throw new ApiError(
      "O backend de cessao nao esta acessivel no momento.",
      503,
      error
    );
  }

  if (!response.ok) {
    const problem = await parseProblemDetails(response);
    throw new ApiError(problem.detail, response.status, problem);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return (await response.json()) as T;
}

async function resolveAuthorizationHeader(headers?: HeadersInit) {
  const explicitHeader = new Headers(headers ?? {}).get("Authorization");
  if (explicitHeader) {
    return explicitHeader;
  }

  if (typeof window !== "undefined") {
    return getBrowserAuthHeader();
  }

  try {
    const { cookies } = await import("next/headers");
    const cookieStore = await cookies();
    const session = deserializeAuthSession(cookieStore.get("fidc_auth")?.value);
    return session ? buildBasicAuthHeader(session.username, session.password) : null;
  } catch {
    return null;
  }
}
