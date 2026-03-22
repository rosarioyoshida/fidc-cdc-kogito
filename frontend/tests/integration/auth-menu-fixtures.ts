import type { CurrentUserAccount } from "@/features/security/user-account-types";

export const seededAccounts: Record<string, CurrentUserAccount> = {
  operador: {
    username: "operador",
    nomeExibicao: "Operador Padrao",
    email: "operador@fidc.local",
    perfilAtivo: "OPERADOR"
  },
  analista: {
    username: "analista",
    nomeExibicao: "Analista Padrao",
    email: "analista@fidc.local",
    perfilAtivo: "ANALISTA"
  },
  aprovador: {
    username: "aprovador",
    nomeExibicao: "Aprovador Padrao",
    email: "aprovador@fidc.local",
    perfilAtivo: "APROVADOR"
  }
};
