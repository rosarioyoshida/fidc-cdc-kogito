import { ApiError, apiFetch } from "@/lib/api-client";

describe("apiFetch", () => {
  afterEach(() => {
    vi.unstubAllGlobals();
  });

  it("wraps network failures as ApiError with service unavailable status", async () => {
    vi.stubGlobal(
      "fetch",
      vi.fn().mockRejectedValueOnce(new TypeError("fetch failed"))
    );

    await expect(apiFetch("/cessoes/12345")).rejects.toMatchObject<ApiError>({
      status: 503,
      message: "O backend de cessao nao esta acessivel no momento."
    });
  });

  it("keeps backend problem details for non successful responses", async () => {
    vi.stubGlobal(
      "fetch",
      vi.fn().mockResolvedValueOnce(
        new Response(
          JSON.stringify({
            type: "about:blank",
            title: "Not Found",
            status: 404,
            detail: "Cessao nao encontrada."
          }),
          {
            status: 404,
            headers: { "Content-Type": "application/json" }
          }
        )
      )
    );

    await expect(apiFetch("/cessoes/12345")).rejects.toMatchObject<ApiError>({
      status: 404,
      message: "Cessao nao encontrada."
    });
  });
});
