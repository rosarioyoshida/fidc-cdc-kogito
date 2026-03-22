package com.fidc.cdc.kogito.infrastructure.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("kafkaTopicsProperties")
@ConfigurationProperties(prefix = "fidc.messaging.topics")
public class KafkaTopicsProperties {

    private String process = "fidc.process.events";
    private String tasks = "fidc.task.events";
    private String audit = "fidc.audit.events";
    private String kogitoProcessDefinitions = "kogito-processdefinitions-events";
    private String kogitoProcessInstances = "kogito-processinstances-events";
    private String kogitoUserTaskInstances = "kogito-usertaskinstances-events";
    private String kogitoJobs = "kogito-jobs-events";

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    public String getKogitoProcessDefinitions() {
        return kogitoProcessDefinitions;
    }

    public void setKogitoProcessDefinitions(String kogitoProcessDefinitions) {
        this.kogitoProcessDefinitions = kogitoProcessDefinitions;
    }

    public String getKogitoProcessInstances() {
        return kogitoProcessInstances;
    }

    public void setKogitoProcessInstances(String kogitoProcessInstances) {
        this.kogitoProcessInstances = kogitoProcessInstances;
    }

    public String getKogitoUserTaskInstances() {
        return kogitoUserTaskInstances;
    }

    public void setKogitoUserTaskInstances(String kogitoUserTaskInstances) {
        this.kogitoUserTaskInstances = kogitoUserTaskInstances;
    }

    public String getKogitoJobs() {
        return kogitoJobs;
    }

    public void setKogitoJobs(String kogitoJobs) {
        this.kogitoJobs = kogitoJobs;
    }
}
