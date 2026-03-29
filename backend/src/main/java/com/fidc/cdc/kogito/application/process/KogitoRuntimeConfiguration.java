package com.fidc.cdc.kogito.application.process;

import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import org.kie.internal.io.ResourceFactory;
import org.kie.kogito.Addons;
import org.kie.kogito.Application;
import org.kie.kogito.StaticApplication;
import org.kie.kogito.StaticConfig;
import org.kie.kogito.auth.IdentityProviders;
import org.kie.kogito.jobs.JobDescription;
import org.kie.kogito.jobs.JobsService;
import org.kie.kogito.process.Processes;
import org.kie.kogito.process.bpmn2.BpmnProcess;
import org.kie.kogito.process.bpmn2.BpmnProcesses;
import org.kie.kogito.process.impl.StaticProcessConfig;
import org.kie.kogito.usertask.UserTask;
import org.kie.kogito.usertask.impl.DefaultUserTask;
import org.kie.kogito.usertask.impl.DefaultUserTaskAssignmentStrategyConfig;
import org.kie.kogito.usertask.impl.DefaultUserTaskConfig;
import org.kie.kogito.usertask.impl.DefaultUserTaskEventListenerConfig;
import org.kie.kogito.usertask.impl.DefaultUserTasks;
import org.kie.kogito.usertask.impl.InMemoryUserTaskInstances;
import org.kie.kogito.usertask.impl.lifecycle.DefaultUserTaskLifeCycle;
import org.kie.kogito.services.uow.CollectingUnitOfWorkFactory;
import org.kie.kogito.services.uow.DefaultUnitOfWorkManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Representa kogito runtime configuration no backend de cessao.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@Configuration
public class KogitoRuntimeConfiguration {

    @Bean
    RuntimeComponents runtimeComponents() {
        StaticProcessConfig processConfig = new StaticProcessConfig();
        RuntimeApplication application = new RuntimeApplication(buildConfig(processConfig));
        BpmnProcesses processes = new BpmnProcesses();
        application.register(processes);

        DefaultUserTasks userTasks = new DefaultUserTasks(application, buildUserTasks(application));
        application.register(userTasks);

        BpmnProcess process = BpmnProcess.from(ResourceFactory.newClassPathResource("processes/controle-cessao.bpmn"))
                .stream()
                .filter(candidate -> KogitoWorkflowRuntimeService.PROCESS_ID.equals(candidate.id()))
                .findFirst()
                .map(candidate -> new BpmnProcess(candidate.process(), processConfig, application))
                .orElseThrow(() -> new IllegalStateException("Processo BPMN controle-cessao nao encontrado."));

        processes.addProcess(process);
        process.activate();
        return new RuntimeComponents(application, processes, process);
    }

    @Bean
    @Primary
    Application kogitoApplication(RuntimeComponents runtimeComponents) {
        return runtimeComponents.application();
    }

    @Bean
    @Primary
    Processes kogitoProcesses(RuntimeComponents runtimeComponents) {
        return runtimeComponents.processes();
    }

    @Bean
    BpmnProcess controleCessaoProcess(RuntimeComponents runtimeComponents) {
        return runtimeComponents.process();
    }

    @Bean
    ConsoleProcessInstanceManagementController processInstanceManagementRestController(
            Processes processes,
            Application application,
            KogitoWorkflowRuntimeService workflowRuntimeService,
            com.fidc.cdc.kogito.domain.cessao.CessaoRepository cessaoRepository,
            com.fidc.cdc.kogito.application.cessao.CessaoEventPublisher cessaoEventPublisher
    ) {
        return new ConsoleProcessInstanceManagementController(
                processes,
                application,
                workflowRuntimeService,
                cessaoRepository,
                cessaoEventPublisher
        );
    }

    private StaticConfig buildConfig(StaticProcessConfig processConfig) {
        DefaultUserTaskConfig userTaskConfig = new DefaultUserTaskConfig(
                new DefaultUserTaskEventListenerConfig(),
                new DefaultUnitOfWorkManager(new CollectingUnitOfWorkFactory()),
                new NoOpJobsService(),
                IdentityProviders.of("workflow-engine", "SYSTEM"),
                new DefaultUserTaskLifeCycle(),
                new DefaultUserTaskAssignmentStrategyConfig(),
                new InMemoryUserTaskInstances()
        );
        return new StaticConfig(Addons.EMTPY, userTaskConfig, processConfig);
    }

    private UserTask[] buildUserTasks(Application application) {
        return new UserTask[] {
                createUserTask(application, "task-01", EtapaCessaoNome.IMPORTAR_CARTEIRA),
                createUserTask(application, "task-02", EtapaCessaoNome.VALIDAR_CEDENTE),
                createUserTask(application, "task-03", EtapaCessaoNome.ANALISAR_ELEGIBILIDADE),
                createUserTask(application, "task-10", EtapaCessaoNome.COLETAR_TERMO_ACEITE),
                createUserTask(application, "task-11", EtapaCessaoNome.VALIDAR_LASTROS),
                createUserTask(application, "task-12", EtapaCessaoNome.AUTORIZAR_PAGAMENTO)
        };
    }

    private UserTask createUserTask(Application application, String id, EtapaCessaoNome etapa) {
        DefaultUserTask task = new DefaultUserTask(application, id, etapa.name());
        task.setTaskName(etapa.name());
        task.setReferenceName(etapa.name());
        task.setPotentialGroups(KogitoWorkflowRuntimeService.potentialGroupFor(etapa));
        task.setAdminGroups(KogitoWorkflowRuntimeService.adminGroupFor(etapa));
        return task;
    }

    /**
     * Representa os dados de runtime components.
     *
     * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
     */
    private record RuntimeComponents(
            Application application,
            Processes processes,
            BpmnProcess process
    ) {
    }

    /**
     * Representa runtime application no backend de cessao.
     *
     * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
     */
    private static final class RuntimeApplication extends StaticApplication {

        private RuntimeApplication(StaticConfig config) {
            super(config);
        }

        private void register(org.kie.kogito.KogitoEngine engine) {
            loadEngine(engine);
        }
    }

    /**
     * Coordena no op jobs na camada de aplicacao.
     *
     * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
     */
    private static final class NoOpJobsService implements JobsService {

        @Override
        public String scheduleJob(JobDescription jobDescription) {
            return java.util.UUID.randomUUID().toString();
        }

        @Override
        public boolean cancelJob(String id) {
            return true;
        }
    }
}
