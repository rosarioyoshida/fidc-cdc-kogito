import React from "react";
import type { PermissionContext } from "@/features/security/types";

type TaskContextPanelProps = {
  permissionContext: PermissionContext;
};

function renderList(items: string[]) {
  if (items.length === 0) {
    return "Sem itens";
  }

  return items.join(", ");
}

export function TaskContextPanel({ permissionContext }: TaskContextPanelProps) {
  const { taskContext, managementContext } = permissionContext;

  return (
    <section className="grid gap-6 xl:grid-cols-[1.05fr,0.95fr]">
      <article className="rounded-lg border bg-surface-raised p-6 shadow-soft">
        <div className="mb-4">
          <h2 className="text-xl font-semibold">Contexto de tarefa humana</h2>
          <p className="text-sm leading-6 text-text-subtle">
            Metadados e atribuicoes disponiveis para o Task Console oficial.
          </p>
        </div>

        <dl className="grid gap-3 text-sm">
          <div>
            <dt className="font-semibold text-text">Ator autenticado</dt>
            <dd className="text-text-subtle">{permissionContext.actorId}</dd>
          </div>
          <div>
            <dt className="font-semibold text-text">Etapa atual</dt>
            <dd className="text-text-subtle">{taskContext.currentStage}</dd>
          </div>
          <div>
            <dt className="font-semibold text-text">Tarefa humana pendente</dt>
            <dd className="text-text-subtle">
              {taskContext.humanTaskPending ? taskContext.taskDescription : "Nao"}
            </dd>
          </div>
          <div>
            <dt className="font-semibold text-text">Grupos candidatos</dt>
            <dd className="text-text-subtle">{renderList(taskContext.candidateGroups)}</dd>
          </div>
          <div>
            <dt className="font-semibold text-text">Usuarios candidatos</dt>
            <dd className="text-text-subtle">{renderList(taskContext.candidateUsers)}</dd>
          </div>
          <div>
            <dt className="font-semibold text-text">Administradores</dt>
            <dd className="text-text-subtle">
              {renderList(taskContext.businessAdministratorGroups)}
            </dd>
          </div>
          <div>
            <dt className="font-semibold text-text">Pode executar etapa atual</dt>
            <dd className="text-text-subtle">
              {taskContext.actorAuthorizedForTask ? "Sim" : "Nao"}
            </dd>
          </div>
        </dl>

        <a
          href={taskContext.taskConsoleUrl}
          target="_blank"
          rel="noreferrer"
          className="mt-5 inline-flex text-sm font-semibold text-brand hover:underline"
        >
          Abrir Task Console
        </a>
      </article>

      <article className="rounded-lg border bg-surface-raised p-6 shadow-soft">
        <div className="mb-4">
          <h2 className="text-xl font-semibold">Contexto de monitoramento</h2>
          <p className="text-sm leading-6 text-text-subtle">
            Visibilidade derivada do Data Index, Jobs Service e Management Console.
          </p>
        </div>

        <dl className="grid gap-3 text-sm">
          <div>
            <dt className="font-semibold text-text">Workflow instance</dt>
            <dd className="text-text-subtle">
              {managementContext.workflowInstanceId ?? "Nao iniciado"}
            </dd>
          </div>
          <div>
            <dt className="font-semibold text-text">Status do processo</dt>
            <dd className="text-text-subtle">{managementContext.processStatus}</dd>
          </div>
          <div>
            <dt className="font-semibold text-text">Read model disponivel</dt>
            <dd className="text-text-subtle">
              {managementContext.readModelAvailable ? "Sim" : "Nao"}
            </dd>
          </div>
          <div>
            <dt className="font-semibold text-text">Ultima projecao</dt>
            <dd className="text-text-subtle">
              {managementContext.lastProjectionEvent ?? "Sem evento indexado"}
            </dd>
          </div>
          <div>
            <dt className="font-semibold text-text">Acoes administrativas</dt>
            <dd className="text-text-subtle">
              {renderList(managementContext.availableAdminActions)}
            </dd>
          </div>
          <div>
            <dt className="font-semibold text-text">Jobs Service</dt>
            <dd className="text-text-subtle">
              {managementContext.waitingForTimerJob ? "Timer pendente" : "Sem timer pendente"}
            </dd>
          </div>
        </dl>

        <div className="mt-5 flex flex-wrap gap-4 text-sm font-semibold">
          <a
            href={managementContext.managementConsoleUrl}
            target="_blank"
            rel="noreferrer"
            className="text-brand hover:underline"
          >
            Abrir Management Console
          </a>
          <a
            href={managementContext.dataIndexUrl}
            target="_blank"
            rel="noreferrer"
            className="text-brand hover:underline"
          >
            Abrir Data Index
          </a>
        </div>
      </article>
    </section>
  );
}
