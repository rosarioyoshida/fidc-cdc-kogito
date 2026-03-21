export type ProblemDetails = {
  type: string;
  title: string;
  status: number;
  detail: string;
  instance?: string;
  errors?: Array<{ field?: string; message?: string }>;
};

export async function parseProblemDetails(response: Response): Promise<ProblemDetails> {
  const fallback = {
    type: "about:blank",
    title: "Falha na requisicao",
    status: response.status,
    detail: "Nao foi possivel interpretar a resposta de erro."
  };

  try {
    const payload = (await response.json()) as ProblemDetails;
    return {
      ...fallback,
      ...payload
    };
  } catch {
    return fallback;
  }
}
