type CessaoDetailPageProps = {
  params: Promise<{ businessKey: string }>;
  searchParams?: Promise<Record<string, string | string[] | undefined>>;
};

import { notFound, redirect } from "next/navigation";
import { CessaoDetail } from "@/features/cessao/cessao-detail";
import {
  advanceEtapaAction,
  getCessao,
  refreshCessaoAction
} from "@/features/cessao/actions";
import { getPermissionContext } from "@/features/security/actions";
import { ApiError } from "@/lib/api-client";

export default async function CessaoDetailPage({
  params,
  searchParams
}: CessaoDetailPageProps) {
  const { businessKey } = await params;
  const query = (await searchParams) ?? {};
  const errorMessage =
    typeof query.error === "string" ? decodeURIComponent(query.error) : undefined;
  try {
    const [cessao, permissionContext] = await Promise.all([
      getCessao(businessKey),
      getPermissionContext(businessKey)
    ]);

    return (
      <main>
        <CessaoDetail
          cessao={cessao}
          permissionContext={permissionContext}
          errorMessage={errorMessage}
          advanceAction={advanceEtapaAction}
          refreshAction={refreshCessaoAction}
        />
      </main>
    );
  } catch (error) {
    if (error instanceof ApiError && error.status === 401) {
      redirect(`/?error=${encodeURIComponent(error.message)}`);
    }
    if (error instanceof ApiError && error.status === 404) {
      notFound();
    }
    throw error;
  }
}
