"use server";

import { cookies } from "next/headers";
import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";
import { apiFetch } from "@/lib/api-client";
import type { PermissionContext } from "@/features/security/types";
import type { AccountActionState } from "@/features/security/account-action-state";
import {
  AUTH_COOKIE_NAME,
  buildBasicAuthHeader,
  deserializeAuthSession,
  serializeAuthSession
} from "@/lib/auth";
import type { CurrentUserAccount } from "@/features/security/user-account-types";
import { ApiError } from "@/lib/api-client";

export async function getPermissionContext(businessKey: string) {
  return apiFetch<PermissionContext>(`/cessoes/${businessKey}/permissoes`);
}

export async function getCurrentUser() {
  return apiFetch<CurrentUserAccount>("/usuarios/me");
}

export async function getCurrentUserOptional() {
  const cookieStore = await cookies();
  const session = deserializeAuthSession(cookieStore.get(AUTH_COOKIE_NAME)?.value);
  if (!session) {
    return null;
  }

  try {
    return await apiFetch<CurrentUserAccount>("/usuarios/me");
  } catch {
    return null;
  }
}

export async function signInAction(formData: FormData) {
  const username = String(formData.get("username") ?? "").trim();
  const password = String(formData.get("password") ?? "");

  try {
    await apiFetch<CurrentUserAccount>("/usuarios/me", {
      headers: {
        Authorization: buildBasicAuthHeader(username, password),
        "X-Auth-Bootstrap": "true"
      }
    });
  } catch (error) {
    const message = error instanceof ApiError ? error.message : "Falha ao autenticar.";
    redirect(`/?error=${encodeURIComponent(message)}&username=${encodeURIComponent(username)}`);
  }

  const cookieStore = await cookies();
  cookieStore.set(AUTH_COOKIE_NAME, serializeAuthSession({ username, password }), {
    httpOnly: true,
    sameSite: "lax",
    path: "/"
  });

  revalidatePath("/", "layout");
  redirect("/cessoes");
}

export async function updateOwnEmailAction(
  _: AccountActionState,
  formData: FormData
): Promise<AccountActionState> {
  const email = String(formData.get("email") ?? "").trim();
  const currentPath = normalizePath(String(formData.get("currentPath") ?? "/cessoes"));

  try {
    await apiFetch("/usuarios/me", {
      method: "PATCH",
      body: JSON.stringify({ email })
    });
  } catch (error) {
    const message = error instanceof ApiError ? error.message : "Falha ao atualizar email.";
    return {
      status: "error",
      message
    };
  }

  revalidatePath("/", "layout");
  revalidatePath(currentPath);
  return {
    status: "success",
    message: "Email atualizado com sucesso.",
    updatedEmail: email
  };
}

export async function changeOwnPasswordAction(
  _: AccountActionState,
  formData: FormData
): Promise<AccountActionState> {
  const currentPassword = String(formData.get("currentPassword") ?? "");
  const newPassword = String(formData.get("newPassword") ?? "");
  const currentPath = normalizePath(String(formData.get("currentPath") ?? "/cessoes"));

  try {
    await apiFetch("/usuarios/me/alteracao-senha", {
      method: "POST",
      body: JSON.stringify({
        senhaAtual: currentPassword,
        novaSenha: newPassword
      })
    });
  } catch (error) {
    const message = error instanceof ApiError ? error.message : "Falha ao alterar senha.";
    return {
      status: "error",
      message
    };
  }

  const cookieStore = await cookies();
  const session = deserializeAuthSession(cookieStore.get(AUTH_COOKIE_NAME)?.value);
  if (session) {
    cookieStore.set(AUTH_COOKIE_NAME, serializeAuthSession({
      username: session.username,
      password: newPassword
    }), {
      httpOnly: true,
      sameSite: "lax",
      path: "/"
    });
  }

  revalidatePath("/", "layout");
  revalidatePath(currentPath);
  return {
    status: "success",
    message: "Senha alterada com sucesso."
  };
}

export async function logoutAction() {
  try {
    await apiFetch("/usuarios/me/sessao", { method: "DELETE" });
  } catch {
    // Ignore backend logout errors and always clear the UI session.
  }

  const cookieStore = await cookies();
  cookieStore.delete(AUTH_COOKIE_NAME);
  revalidatePath("/", "layout");
  redirect("/");
}

function normalizePath(pathname: string) {
  return pathname.startsWith("/") ? pathname : "/cessoes";
}
