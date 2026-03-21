export type RegraElegibilidade = {
  id: string;
  codigoRegra: string;
  descricao: string;
  resultado: string;
  severidade: string;
  mensagem: string;
  avaliadaEm?: string | null;
};

export type ParcelaAnalise = {
  id: string;
  identificadorExterno: string;
  numeroParcela: number;
  vencimento?: string | null;
  valor: number;
  statusRegistro: string;
};

export type ContratoAnalise = {
  id: string;
  identificadorExterno: string;
  sacadoId: string;
  valorNominal: number;
  dataOrigem?: string | null;
  statusRegistro: string;
  parcelas: ParcelaAnalise[];
};

export type PagamentoAnalise = {
  id: string;
  valor: number;
  statusPagamento: string;
  autorizadoPor?: string | null;
  autorizadoEm?: string | null;
  confirmadoEm?: string | null;
  comprovanteReferencia?: string | null;
};

export type LastroAnalise = {
  id: string;
  tipoDocumento: string;
  origem: string;
  statusValidacao: string;
  recebidoEm?: string | null;
  validadoEm?: string | null;
  contratoId?: string | null;
  parcelaId?: string | null;
};

export type AnaliseSnapshot = {
  regras: RegraElegibilidade[];
  contratos: ContratoAnalise[];
  parcelas: ParcelaAnalise[];
  pagamentos: PagamentoAnalise[];
  lastros: LastroAnalise[];
};

export const registradoraOperations = [
  "CARTEIRA",
  "CONTRATO",
  "PARCELA",
  "OFERTA",
  "ACEITE",
  "PAGAMENTO",
  "LASTRO"
] as const;

export type RegistradoraOperation = (typeof registradoraOperations)[number];
