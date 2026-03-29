package com.fidc.cdc.kogito.process;

import com.fidc.cdc.kogito.application.cessao.CessaoProcessService;
import com.fidc.cdc.kogito.observability.ProcessMetricsService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expoe endpoints HTTP para jobs callback.
 *
 * <p>Este tipo pertence a camada de exposicao e suporte ao runtime de processos. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@RestController
@RequestMapping("/api/v1/process/jobs/callbacks")
public class JobsCallbackController {

    private final CessaoProcessService cessaoProcessService;
    private final ProcessMetricsService processMetricsService;

    public JobsCallbackController(CessaoProcessService cessaoProcessService, ProcessMetricsService processMetricsService) {
        this.cessaoProcessService = cessaoProcessService;
        this.processMetricsService = processMetricsService;
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<Map<String, Object>> receiveCallback(
            @PathVariable String jobId,
            @RequestBody(required = false) Map<String, Object> payload
    ) {
        processMetricsService.incrementProcessEvent("timer-callback", "received");
        String businessKey = valueOf(payload, "businessKey");
        String processInstanceId = valueOf(payload, "processInstanceId");
        boolean resumed = false;

        if (businessKey != null && processInstanceId != null) {
            cessaoProcessService.resumeAwaitingConfirmation(businessKey, processInstanceId, "jobs-service");
            resumed = true;
        }

        return ResponseEntity.accepted().body(Map.of(
                "jobId", jobId,
                "accepted", true,
                "payloadReceived", payload != null,
                "resumed", resumed,
                "businessKey", businessKey == null ? "" : businessKey,
                "processInstanceId", processInstanceId == null ? "" : processInstanceId
        ));
    }

    private String valueOf(Map<String, Object> payload, String key) {
        if (payload == null) {
            return null;
        }
        Object value = payload.get(key);
        if (value == null) {
            return null;
        }
        String stringValue = value.toString().trim();
        return stringValue.isEmpty() ? null : stringValue;
    }
}
