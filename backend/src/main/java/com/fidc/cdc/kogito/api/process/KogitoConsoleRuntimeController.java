package com.fidc.cdc.kogito.api.process;

import com.fidc.cdc.kogito.application.process.ConsoleRuntimeEndpointService;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(
        origins = {"http://localhost:3000", "http://localhost:8280", "http://localhost:8380"},
        allowedHeaders = {"*"},
        methods = {}
)
@RequestMapping
public class KogitoConsoleRuntimeController {

    private final ConsoleRuntimeEndpointService consoleRuntimeEndpointService;

    public KogitoConsoleRuntimeController(ConsoleRuntimeEndpointService consoleRuntimeEndpointService) {
        this.consoleRuntimeEndpointService = consoleRuntimeEndpointService;
    }

    @GetMapping(path = "/svg/processes/{processId}", produces = "image/svg+xml")
    public ResponseEntity<String> processDiagram(@PathVariable String processId) {
        return ResponseEntity.ok(consoleRuntimeEndpointService.processDiagram(processId));
    }

    @GetMapping(path = "/svg/processes/{processId}/instances/{processInstanceId}", produces = "image/svg+xml")
    public ResponseEntity<String> processInstanceDiagram(
            @PathVariable String processId,
            @PathVariable String processInstanceId
    ) {
        return ResponseEntity.ok(consoleRuntimeEndpointService.processInstanceDiagram(processId, processInstanceId));
    }

    @GetMapping(path = "/management/processes/{processId}/source", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> processSource(@PathVariable String processId) {
        return ResponseEntity.ok(consoleRuntimeEndpointService.processSource(processId));
    }

    @GetMapping(path = {
            "/api/v1/cessoes/{businessKey}/{processInstanceId}/{taskName}/{externalReferenceId}/schema",
            "/controle-cessao/{businessKey}/{processInstanceId}/{taskName}/{externalReferenceId}/schema"
    },
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> taskSchema(
            @PathVariable String businessKey,
            @PathVariable String processInstanceId,
            @PathVariable String taskName,
            @PathVariable String externalReferenceId,
            @RequestParam(name = "user", required = false) String user,
            @RequestParam(name = "group", required = false) List<String> groups
    ) {
        return ResponseEntity.ok(
                consoleRuntimeEndpointService.taskSchema(
                        businessKey,
                        processInstanceId,
                        taskName,
                        externalReferenceId,
                        user,
                        groups
                )
        );
    }

    @PostMapping(path = {
            "/api/v1/cessoes/{businessKey}/{processInstanceId}/{taskName}/{externalReferenceId}",
            "/controle-cessao/{businessKey}/{processInstanceId}/{taskName}/{externalReferenceId}"
    },
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> submitTask(
            @PathVariable String businessKey,
            @PathVariable String processInstanceId,
            @PathVariable String taskName,
            @PathVariable String externalReferenceId,
            @RequestParam(name = "phase", required = false) String phase,
            @RequestParam(name = "user", required = false) String user,
            @RequestParam(name = "group", required = false) List<String> groups,
            @RequestBody(required = false) Map<String, Object> payload
    ) {
        return ResponseEntity.ok(
                consoleRuntimeEndpointService.submitTask(
                        businessKey,
                        processInstanceId,
                        taskName,
                        externalReferenceId,
                        phase,
                        user,
                        groups,
                        payload
                )
        );
    }
}
