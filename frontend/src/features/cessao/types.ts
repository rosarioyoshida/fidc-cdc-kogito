export type EtapaCessao = {
  nomeEtapa: string;
  statusEtapa: string;
  ordem: number;
  responsavelId?: string | null;
  inicioEm?: string | null;
  concluidaEm?: string | null;
  resultado?: string | null;
  justificativa?: string | null;
};

export type Cessao = {
  businessKey: string;
  status: string;
  workflowInstanceId?: string | null;
  cedenteId: string;
  cessionariaId: string;
  dataImportacao?: string | null;
  dataEncerramento?: string | null;
  etapas?: EtapaCessao[];
};
