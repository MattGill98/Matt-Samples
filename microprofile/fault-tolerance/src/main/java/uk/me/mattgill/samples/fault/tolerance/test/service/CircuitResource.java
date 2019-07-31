package uk.me.mattgill.samples.fault.tolerance.test.service;

import java.time.temporal.ChronoUnit;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;

@Path("/circuit")
public class CircuitResource {

    @GET
    @Retry(maxRetries = 3, retryOn = RuntimeException.class)
    @CircuitBreaker(
        delay = 5,
        delayUnit = ChronoUnit.SECONDS,
        failOn = RuntimeException.class,
        failureRatio = 0.5,
        requestVolumeThreshold = 3,
        successThreshold = 1
    )
    public String helloWorld() {
        int fail = new Random().nextInt(10);
        if (fail < 5) {
            throw new RuntimeException("Failed to return.");
        }
        return "Hello World!";
    }

}