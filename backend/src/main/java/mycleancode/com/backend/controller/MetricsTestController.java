package mycleancode.com.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mycleancode.com.backend.model.ResponseData;
import mycleancode.com.backend.service.CustomMetrics;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
class MetricsTestController {

    private final CustomMetrics customMetrics;


    @GetMapping("/ping")
    public ResponseEntity<ResponseData> ping() {
        log.info("Ping request received");
        customMetrics.countRequest();

// Measure the execution time
        customMetrics.recordExecution(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100); // Simulation of work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        return ResponseEntity.ok(new ResponseData("pong", 200));
    }

    @GetMapping("/status/{code}")
    public ResponseEntity<ResponseData> status(
            @PathVariable int code,
            @RequestParam(required = false) Integer secondsSleep
    ) throws Exception {
        log.info("Status request received with code={}, sleep={}", code, secondsSleep);
        customMetrics.countRequest();

        return customMetrics.recordExecution(() -> {
            if (secondsSleep != null) {
                TimeUnit.SECONDS.sleep(secondsSleep);
            }
            if (code != 200) {
                return ResponseEntity.status(code).body(new ResponseData("error", code));
            }
            return ResponseEntity.ok(new ResponseData("ok", code));
        });
    }
}
