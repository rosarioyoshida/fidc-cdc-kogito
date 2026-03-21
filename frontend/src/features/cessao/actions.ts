"use server";

import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";
import { apiFetch, ApiError } from "@/lib/api-client";
import type { Cessao } from "@/features/cessao/types";

function toMessage(error: unknown) {
  if (error instanceof ApiError) {
    return error.message;
  }

  if (error instanceof Error) {
    return error.message;
  }

  return "Falha operacional inesperada.";
}

export async function listCessoes() {
  return apiFetch<Cessao[]>("/cessoes");
}

export async function getCessao(businessKey: string) {
  return apiFetch<Cessao>(`/cessoes/${businessKey}`);
}

export async function createCessaoAction(formData: FormData) {
  const businessKey = String(formData.get("businessKey") ?? "");
  const cedenteId = String(formData.get("cedenteId") ?? "");
  const cessionariaId = String(formData.get("cessionariaId") ?? "");

  try {
    await apiFetch<Cessao>("/cessoes", {
      method: "POST",
      body: JSON.stringify({ businessKey, cedenteId, cessionariaId })
    });
    revalidatePath("/cessoes");
    redirect(`/cessoes/${businessKey}`);
  } catch (error) {
    redirect(`/cessoes?error=${encodeURIComponent(toMessage(error))}`);
  }
}

export async function advanceEtapaAction(formData: FormData) {
  const businessKey = String(formData.get("businessKey") ?? "");
  const etapaNome = String(formData.get("etapaNome") ?? "");
  const responsavelId = String(formData.get("responsavelId") ?? "operador");

  try {
    await apiFetch<Cessao>(`/cessoes/${businessKey}/etapas/${etapaNome}/acoes/avancar`, {
      method: "POST",
      body: JSON.stringify({ responsavelId })
    });
    revalidatePath(`/cessoes/${businessKey}`);
    redirect(`/cessoes/${businessKey}`);
  } catch (error) {
    redirect(`/cessoes/${businessKey}?error=${encodeURIComponent(toMessage(error))}`);
  }
}

export async function refreshCessaoAction(formData: FormData) {
  const businessKey = String(formData.get("businessKey") ?? "");
  revalidatePath(`/cessoes/${businessKey}`);
  redirect(`/cessoes/${businessKey}`);
}
