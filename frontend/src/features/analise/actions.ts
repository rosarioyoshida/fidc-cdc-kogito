"use server";

import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";
import { ApiError, apiFetch } from "@/lib/api-client";
import type {
  AnaliseSnapshot,
  ContratoAnalise,
  LastroAnalise,
  PagamentoAnalise,
  ParcelaAnalise,
  RegraElegibilidade,
  RegistradoraOperation
} from "@/features/analise/types";

function toMessage(error: unknown) {
  if (error instanceof ApiError) {
    return error.message;
  }

  if (error instanceof Error) {
    return error.message;
  }

  return "Falha operacional inesperada.";
}

function redirectWithState(businessKey: string, params: Record<string, string>) {
  const search = new URLSearchParams(params);
  redirect(`/cessoes/${businessKey}/analise?${search.toString()}`);
}

function parseOptionalUuid(value: FormDataEntryValue | null) {
  const parsed = String(value ?? "").trim();
  return parsed.length > 0 ? parsed : null;
}

export async function getAnaliseSnapshot(businessKey: string): Promise<AnaliseSnapshot> {
  const [regras, contratos, parcelas, pagamentos, lastros] = await Promise.all([
    apiFetch<RegraElegibilidade[]>(`/cessoes/${businessKey}/analise/regras`),
    apiFetch<ContratoAnalise[]>(`/cessoes/${businessKey}/analise/contratos`),
    apiFetch<ParcelaAnalise[]>(`/cessoes/${businessKey}/analise/parcelas`),
    apiFetch<PagamentoAnalise[]>(`/cessoes/${businessKey}/analise/pagamentos`),
    apiFetch<LastroAnalise[]>(`/cessoes/${businessKey}/analise/lastros`)
  ]);

  return { regras, contratos, parcelas, pagamentos, lastros };
}

export async function avaliarElegibilidadeAction(formData: FormData) {
  const businessKey = String(formData.get("businessKey") ?? "");

  try {
    await apiFetch(`/cessoes/${businessKey}/analise/elegibilidade/avaliar`, {
      method: "POST"
    });
    revalidatePath(`/cessoes/${businessKey}/analise`);
    redirectWithState(businessKey, { message: "Elegibilidade atualizada com sucesso." });
  } catch (error) {
    redirectWithState(businessKey, { error: toMessage(error) });
  }
}

export async function apurarValorAction(formData: FormData) {
  const businessKey = String(formData.get("businessKey") ?? "");

  try {
    await apiFetch(`/cessoes/${businessKey}/analise/calculo/apurar`, {
      method: "POST"
    });
    revalidatePath(`/cessoes/${businessKey}/analise`);
    redirectWithState(businessKey, { message: "Calculo financeiro reprocessado." });
  } catch (error) {
    redirectWithState(businessKey, { error: toMessage(error) });
  }
}

export async function registrarLastroAction(formData: FormData) {
  const businessKey = String(formData.get("businessKey") ?? "");

  try {
    await apiFetch(`/cessoes/${businessKey}/analise/lastros`, {
      method: "POST",
      body: JSON.stringify({
        contratoId: parseOptionalUuid(formData.get("contratoId")),
        parcelaId: parseOptionalUuid(formData.get("parcelaId")),
        tipoDocumento: String(formData.get("tipoDocumento") ?? ""),
        origem: String(formData.get("origem") ?? "")
      })
    });
    revalidatePath(`/cessoes/${businessKey}/analise`);
    redirectWithState(businessKey, { message: "Lastro recebido para validacao." });
  } catch (error) {
    redirectWithState(businessKey, { error: toMessage(error) });
  }
}

export async function validarLastrosAction(formData: FormData) {
  const businessKey = String(formData.get("businessKey") ?? "");

  try {
    await apiFetch(`/cessoes/${businessKey}/analise/lastros/validar`, {
      method: "POST"
    });
    revalidatePath(`/cessoes/${businessKey}/analise`);
    redirectWithState(businessKey, { message: "Validacao documental concluida." });
  } catch (error) {
    redirectWithState(businessKey, { error: toMessage(error) });
  }
}

export async function executarRegistradoraAction(formData: FormData) {
  const businessKey = String(formData.get("businessKey") ?? "");
  const tipoOperacao = String(formData.get("tipoOperacao") ?? "") as RegistradoraOperation;

  try {
    await apiFetch(`/cessoes/${businessKey}/analise/registradora/${tipoOperacao}`, {
      method: "POST"
    });
    revalidatePath(`/cessoes/${businessKey}/analise`);
    redirectWithState(businessKey, {
      message: `Operacao ${tipoOperacao} enviada a registradora com retry automatico controlado.`
    });
  } catch (error) {
    redirectWithState(businessKey, { error: toMessage(error) });
  }
}

export async function refreshAnaliseAction(formData: FormData) {
  const businessKey = String(formData.get("businessKey") ?? "");
  revalidatePath(`/cessoes/${businessKey}/analise`);
  redirect(`/cessoes/${businessKey}/analise`);
}
