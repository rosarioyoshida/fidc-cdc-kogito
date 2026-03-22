export type TaskAssignmentContext = {
  businessKey: string;
  workflowInstanceId?: string | null;
  actorId: string;
  currentStage: string;
  humanTaskPending: boolean;
  actorAuthorizedForTask: boolean;
  taskName?: string | null;
  taskDescription?: string | null;
  assignedActor?: string | null;
  candidateGroups: string[];
  candidateUsers: string[];
  businessAdministratorGroups: string[];
  taskConsoleUrl: string;
};

export type ManagementConsoleContext = {
  businessKey: string;
  workflowInstanceId?: string | null;
  processStatus: string;
  currentStage: string;
  humanTaskPending: boolean;
  waitingForTimerJob: boolean;
  readModelAvailable: boolean;
  lastProjectionEvent?: string | null;
  lastProjectionUpdate?: string | null;
  availableAdminActions: string[];
  managementConsoleUrl: string;
  dataIndexUrl: string;
  jobsServiceUrl: string;
};

export type PermissionContext = {
  actorId: string;
  perfis: string[];
  etapasPermitidas: string[];
  etapaAtual: string;
  podeExecutarEtapaAtual: boolean;
  taskContext: TaskAssignmentContext;
  managementContext: ManagementConsoleContext;
};

export { PROFILE_LABELS, toProfileLabel } from "@/features/security/user-account-types";
