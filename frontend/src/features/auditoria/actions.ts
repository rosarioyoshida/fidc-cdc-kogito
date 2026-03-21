"use server";

import { apiFetch } from "@/lib/api-client";
import type { AuditEvent } from "@/features/auditoria/types";

export async function getAuditTrail(businessKey: string) {
  return apiFetch<AuditEvent[]>(`/cessoes/${businessKey}/auditoria`);
}
