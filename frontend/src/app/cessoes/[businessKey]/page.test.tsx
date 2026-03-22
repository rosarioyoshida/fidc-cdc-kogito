import { notFound } from "next/navigation";
import CessaoDetailPage from "@/app/cessoes/[businessKey]/page";
import { ApiError } from "@/lib/api-client";
import { getPermissionContext } from "@/features/security/actions";
import { getCessao } from "@/features/cessao/actions";

vi.mock("next/navigation", () => ({
  notFound: vi.fn(() => {
    throw new Error("NEXT_NOT_FOUND");
  })
}));

vi.mock("@/features/cessao/actions", () => ({
  getCessao: vi.fn(),
  advanceEtapaAction: vi.fn(),
  refreshCessaoAction: vi.fn()
}));

vi.mock("@/features/security/actions", () => ({
  getPermissionContext: vi.fn()
}));

describe("CessaoDetailPage", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("calls notFound when the backend returns 404 for the requested cessao", async () => {
    vi.mocked(getCessao).mockRejectedValueOnce(
      new ApiError("Cessao nao encontrada para o businessKey informado.", 404, {
        detail: "Cessao nao encontrada para o businessKey informado."
      })
    );
    vi.mocked(getPermissionContext).mockResolvedValue({} as never);

    await expect(
      CessaoDetailPage({
        params: Promise.resolve({ businessKey: "12345" }),
        searchParams: Promise.resolve({})
      })
    ).rejects.toThrow("NEXT_NOT_FOUND");

    expect(notFound).toHaveBeenCalledTimes(1);
  });
});
