package com.fidc.cdc.kogito.infrastructure.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("kafkaTopicsProperties")
@ConfigurationProperties(prefix = "fidc.messaging.topics")
public class KafkaTopicsProperties {

    private String process = "fidc.process.events";
    private String tasks = "fidc.task.events";
    private String audit = "fidc.audit.events";

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
}
