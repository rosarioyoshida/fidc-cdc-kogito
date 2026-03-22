import { ApiError, apiFetch } from "@/lib/api-client";
import { advanceEtapaAction, createCessaoAction } from "@/features/cessao/actions";
import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";

vi.mock("@/lib/api-client", () => ({
  apiFetch: vi.fn(),
  ApiError: class ApiError extends Error {
    constructor(message: string, public readonly status = 500, public readonly details?: unknown) {
      super(message);
      this.name = "ApiError";
    }
  }
}));

vi.mock("next/cache", () => ({
  revalidatePath: vi.fn()
}));

vi.mock("next/navigation", () => ({
  redirect: vi.fn((location: string) => {
    throw new Error(`NEXT_REDIRECT:${location}`);
  })
}));

const apiFetchMock = vi.mocked(apiFetch);
const revalidatePathMock = vi.mocked(revalidatePath);
const redirectMock = vi.mocked(redirect);

describe("cessao server actions", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("redirects to the detail page after advancing the etapa without treating redirect as an error", async () => {
    apiFetchMock.mockResolvedValueOnce({} as never);

    const formData = new FormData();
    formData.set("businessKey", "12345");
    formData.set("etapaNome", "IMPORTAR_CARTEIRA");
    formData.set("responsavelId", "operador");

    await expect(advanceEtapaAction(formData)).rejects.toThrow("NEXT_REDIRECT:/cessoes/12345");

    expect(apiFetchMock).toHaveBeenCalledWith("/cessoes/12345/etapas/IMPORTAR_CARTEIRA/acoes/avancar", {
      method: "POST",
      body: JSON.stringify({ responsavelId: "operador" })
    });
    expect(revalidatePathMock).toHaveBeenCalledWith("/cessoes/12345");
    expect(redirectMock).toHaveBeenCalledTimes(1);
    expect(redirectMock).toHaveBeenCalledWith("/cessoes/12345");
  });

  it("redirects to the error state when the backend call fails", async () => {
    apiFetchMock.mockRejectedValueOnce(new ApiError("Falha no backend", 500));

    const formData = new FormData();
    formData.set("businessKey", "12345");
    formData.set("etapaNome", "IMPORTAR_CARTEIRA");

    await expect(advanceEtapaAction(formData)).rejects.toThrow(
      "NEXT_REDIRECT:/cessoes/12345?error=Falha%20no%20backend"
    );

    expect(revalidatePathMock).not.toHaveBeenCalled();
    expect(redirectMock).toHaveBeenCalledTimes(1);
    expect(redirectMock).toHaveBeenCalledWith("/cessoes/12345?error=Falha%20no%20backend");
  });

  it("redirects to the created cessao after a successful create", async () => {
    apiFetchMock.mockResolvedValueOnce({} as never);

    const formData = new FormData();
    formData.set("businessKey", "BK-001");
    formData.set("cedenteId", "ced-1");
    formData.set("cessionariaId", "ces-1");

    await expect(createCessaoAction(formData)).rejects.toThrow("NEXT_REDIRECT:/cessoes/BK-001");

    expect(apiFetchMock).toHaveBeenCalledWith("/cessoes", {
      method: "POST",
      body: JSON.stringify({
        businessKey: "BK-001",
        cedenteId: "ced-1",
        cessionariaId: "ces-1"
      })
    });
    expect(revalidatePathMock).toHaveBeenCalledWith("/cessoes");
    expect(redirectMock).toHaveBeenCalledTimes(1);
    expect(redirectMock).toHaveBeenCalledWith("/cessoes/BK-001");
  });
});
