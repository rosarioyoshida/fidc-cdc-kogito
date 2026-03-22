import type {
  ContratoAnalise,
  LastroAnalise,
  PagamentoAnalise,
  RegraElegibilidade
} from "@/features/analise/types";
import type { Cessao } from "@/features/cessao/types";
import type { PermissionContext } from "@/features/security/types";
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

export const permissionContextFixture: PermissionContext = {
  actorId: "operador",
  perfis: ["OPERADOR"],
  etapasPermitidas: ["ANALISE"],
  etapaAtual: "ANALISE",
  podeExecutarEtapaAtual: true,
  taskContext: {
    businessKey: "CESSAO-001",
    actorId: "operador",
    currentStage: "ANALISE",
    humanTaskPending: true,
    actorAuthorizedForTask: true,
    candidateGroups: ["operadores"],
    candidateUsers: [],
    businessAdministratorGroups: ["admins"],
    taskConsoleUrl: "http://localhost:8080/task-console"
  },
  managementContext: {
    businessKey: "CESSAO-001",
    processStatus: "ACTIVE",
    currentStage: "ANALISE",
    humanTaskPending: true,
    waitingForTimerJob: false,
    readModelAvailable: true,
    availableAdminActions: ["cancel"],
    managementConsoleUrl: "http://localhost:8080/management-console",
    dataIndexUrl: "http://localhost:8180/graphql",
    jobsServiceUrl: "http://localhost:8280/jobs"
  }
};

export const cessaoFixture: Cessao = {
  businessKey: "CESSAO-001",
  status: "EM_ANALISE",
  cedenteId: "CED-1",
  cessionariaId: "CES-1",
  etapas: [
    {
      nomeEtapa: "ANALISE",
      statusEtapa: "EM_EXECUCAO",
      ordem: 1,
      responsavelId: "operador"
    },
    {
      nomeEtapa: "PAGAMENTO",
      statusEtapa: "PENDENTE",
      ordem: 2
    }
  ]
};

export const pagamentosFixture: PagamentoAnalise[] = [
  {
    id: "pag-1",
    valor: 1520.55,
    statusPagamento: "AUTORIZADO",
    autorizadoPor: "analista",
    autorizadoEm: "2026-03-22T10:00:00Z"
  }
];

export const contratosFixture: ContratoAnalise[] = [
  {
    id: "contrato-1",
    identificadorExterno: "CTR-001",
    sacadoId: "SAC-123",
    valorNominal: 20500,
    statusRegistro: "ATIVO",
    parcelas: [
      {
        id: "parcela-1",
        identificadorExterno: "PAR-001",
        numeroParcela: 1,
        valor: 10250,
        statusRegistro: "ATIVA"
      }
    ]
  }
];

export const regrasFixture: RegraElegibilidade[] = [
  {
    id: "regra-1",
    codigoRegra: "ELG-01",
    descricao: "Cedente ativo",
    resultado: "APROVADA",
    severidade: "INFO",
    mensagem: "Cedente apto"
  },
  {
    id: "regra-2",
    codigoRegra: "ELG-02",
    descricao: "Limite operacional",
    resultado: "REPROVADA",
    severidade: "ALTA",
    mensagem: "Limite excedido"
  }
];

export const lastrosFixture: LastroAnalise[] = [
  {
    id: "lastro-1",
    tipoDocumento: "NF-e",
    origem: "Cedente",
    statusValidacao: "VALIDADO",
    recebidoEm: "2026-03-22T10:00:00Z"
  },
  {
    id: "lastro-2",
    tipoDocumento: "Duplicata",
    origem: "Registradora",
    statusValidacao: "REJEITADO",
    recebidoEm: "2026-03-22T10:05:00Z"
  }
];
