export type AuditEvent = {
  id: string;
  atorId: string;
  perfil: string;
  tipoEvento: string;
  resultado: string;
  correlationId: string;
  ocorridoEm: string;
  detalheRef?: string | null;
  etapa?: string | null;
};
