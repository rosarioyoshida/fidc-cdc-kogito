import React from "react";
import { ForbiddenState } from "@/components/feedback/forbidden-state";

type PermissionGuardProps = {
  allowed: boolean;
  children: React.ReactNode;
  fallbackTitle?: string;
  fallbackDescription?: string;
};

export function PermissionGuard({
  allowed,
  children,
  fallbackTitle = "Acao indisponivel",
  fallbackDescription = "O perfil autenticado nao pode executar a etapa operacional atual."
}: PermissionGuardProps) {
  if (allowed) {
    return <>{children}</>;
  }

  return (
    <ForbiddenState
      compact
      title={fallbackTitle}
      description={fallbackDescription}
    />
  );
}
