export const PROFILE_LABELS = {
  OPERADOR: "Operador",
  ANALISTA: "Analista",
  APROVADOR: "Aprovador",
  AUDITOR: "Auditor",
  INTEGRACAO: "Integracao"
} as const;

export type SeededProfile = keyof typeof PROFILE_LABELS;

export type CurrentUserAccount = {
  username: string;
  nomeExibicao: string;
  email: string;
  perfilAtivo: SeededProfile;
};

export function toProfileLabel(profile: string) {
  return PROFILE_LABELS[profile as SeededProfile] ?? profile;
}
