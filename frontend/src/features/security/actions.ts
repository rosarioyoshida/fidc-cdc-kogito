"use server";

import { apiFetch } from "@/lib/api-client";
import type { PermissionContext } from "@/features/security/types";

export async function getPermissionContext(businessKey: string) {
  return apiFetch<PermissionContext>(`/cessoes/${businessKey}/permissoes`);
}
