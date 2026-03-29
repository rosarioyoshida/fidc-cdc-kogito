type CessaoDetailPageProps = {
  params: Promise<{ businessKey: string }>;
  searchParams?: Promise<Record<string, string | string[] | undefined>>;
};

import { notFound, redirect } from "next/navigation";
import { AppShell } from "@/components/layout/app-shell";
import { Button } from "@/components/ui/button";
import { CessaoDetail } from "@/features/cessao/cessao-detail";
import {
  advanceEtapaAction,
  getCessao,
  refreshCessaoAction
} from "@/features/cessao/actions";
import {
  buildCessaoContextNavigation,
  buildPrimaryAction,
  primaryNavigation
} from "@/features/navigation/shell-config";
import { getCurrentUser, getPermissionContext } from "@/features/security/actions";
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
    const [user, cessao, permissionContext] = await Promise.all([
      getCurrentUser(),
      getCessao(businessKey),
      getPermissionContext(businessKey)
    ]);

    return (
      <AppShell
        user={user}
        navigation={primaryNavigation}
        primaryAction={buildPrimaryAction()}
        secondaryNav={buildCessaoContextNavigation(businessKey)}
        secondaryControls={
          <form action={refreshCessaoAction}>
            <input type="hidden" name="businessKey" value={businessKey} />
            <Button type="submit" variant="secondary">
              Atualizar status
            </Button>
          </form>
        }
      >
        <CessaoDetail
          cessao={cessao}
          permissionContext={permissionContext}
          errorMessage={errorMessage}
          advanceAction={advanceEtapaAction}
        />
      </AppShell>
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
