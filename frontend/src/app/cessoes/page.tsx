import { redirect } from "next/navigation";
import { AppShell } from "@/components/layout/app-shell";
import { CessaoList } from "@/features/cessao/cessao-list";
import { createCessaoAction, listCessoes } from "@/features/cessao/actions";
import type { Cessao } from "@/features/cessao/types";
import {
  buildCessoesSecondaryNavigation,
  buildPrimaryAction,
  primaryNavigation
} from "@/features/navigation/shell-config";
import { getCurrentUser } from "@/features/security/actions";
import { ApiError } from "@/lib/api-client";

type CessoesPageProps = {
  searchParams?: Promise<Record<string, string | string[] | undefined>>;
};

export default async function CessoesPage({ searchParams }: CessoesPageProps) {
  const params = (await searchParams) ?? {};
  const errorMessage =
    typeof params.error === "string" ? decodeURIComponent(params.error) : undefined;

  let items: Cessao[] = [];
  let queryError = errorMessage;
  let user;

  try {
    [user, items] = await Promise.all([getCurrentUser(), listCessoes()]);
  } catch (error) {
    if (error instanceof ApiError && error.status === 401) {
      redirect(`/?error=${encodeURIComponent(error.message)}`);
    }
    if (!queryError) {
      queryError = error instanceof Error ? error.message : "Falha ao consultar cessoes.";
    }
    user = await getCurrentUser();
  }

  return (
    <AppShell
      user={user}
      navigation={primaryNavigation}
      primaryAction={buildPrimaryAction()}
      secondaryNav={buildCessoesSecondaryNavigation()}
    >
      <CessaoList items={items} errorMessage={queryError} createAction={createCessaoAction} />
    </AppShell>
  );
}
