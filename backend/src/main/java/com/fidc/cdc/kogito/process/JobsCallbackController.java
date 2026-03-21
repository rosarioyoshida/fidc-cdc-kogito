package com.fidc.cdc.kogito.process;

import com.fidc.cdc.kogito.observability.ProcessMetricsService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/process/jobs/callbacks")
public class JobsCallbackController {

    private final ProcessMetricsService processMetricsService;

    public JobsCallbackController(ProcessMetricsService processMetricsService) {
        this.processMetricsService = processMetricsService;
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<Map<String, Object>> receiveCallback(
            @PathVariable String jobId,
            @RequestBody(required = false) Map<String, Object> payload
    ) {
        processMetricsService.incrementProcessEvent("timer-callback", "received");
        return ResponseEntity.accepted().body(Map.of(
                "jobId", jobId,
                "accepted", true,
                "payloadReceived", payload != null
        ));
    }
}
