package mycleancode.com.backend.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    private final Counter requestCounter;
    private final Timer requestTimer;

    public CustomMetrics(MeterRegistry registry) {
        this.requestCounter = Counter.builder("custom_requests_total")
                .description("Total custom requests")
                .register(registry);

        this.requestTimer = Timer.builder("custom_request_duration_seconds")
                .description("Request processing duration in seconds")
                .publishPercentileHistogram()
                .register(registry);
    }

    public void countRequest() {
        requestCounter.increment();
    }

    public void recordExecution(Runnable runnable) {
        requestTimer.record(runnable);
    }

    public <T> T recordExecution(CallableWithException<T> callable) throws Exception {
        return requestTimer.recordCallable(callable::call);
    }

    @FunctionalInterface
    public interface CallableWithException<T> {
        T call() throws Exception;
    }
}
