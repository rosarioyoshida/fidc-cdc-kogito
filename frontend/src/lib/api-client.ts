import { getBasicAuthHeader } from "@/lib/auth";
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
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    headers: {
      Accept: "application/json",
      Authorization: getBasicAuthHeader(),
      "Content-Type": "application/json",
      ...init?.headers
    },
    cache: "no-store"
  });

  if (!response.ok) {
    const problem = await parseProblemDetails(response);
    throw new ApiError(problem.detail, response.status, problem);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return (await response.json()) as T;
}
