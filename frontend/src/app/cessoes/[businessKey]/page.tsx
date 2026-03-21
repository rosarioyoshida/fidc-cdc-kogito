type CessaoDetailPageProps = {
  params: Promise<{ businessKey: string }>;
  searchParams?: Promise<Record<string, string | string[] | undefined>>;
};

import { CessaoDetail } from "@/features/cessao/cessao-detail";
import {
  advanceEtapaAction,
  getCessao,
  refreshCessaoAction
} from "@/features/cessao/actions";

export default async function CessaoDetailPage({
  params,
  searchParams
}: CessaoDetailPageProps) {
  const { businessKey } = await params;
  const query = (await searchParams) ?? {};
  const errorMessage =
    typeof query.error === "string" ? decodeURIComponent(query.error) : undefined;
  const cessao = await getCessao(businessKey);

  return (
    <main>
      <CessaoDetail
        cessao={cessao}
        errorMessage={errorMessage}
        advanceAction={advanceEtapaAction}
        refreshAction={refreshCessaoAction}
      />
    </main>
  );
}
