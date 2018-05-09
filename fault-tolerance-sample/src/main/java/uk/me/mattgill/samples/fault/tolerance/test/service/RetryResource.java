package uk.me.mattgill.samples.fault.tolerance.test.service;

import java.time.temporal.ChronoUnit;
import java.util.Random;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.faulttolerance.Retry;

@Path("/retry")
@RequestScoped
public class RetryResource {

    @GET
    @Retry(maxRetries = -1, retryOn = RuntimeException.class, maxDuration = 10, durationUnit = ChronoUnit.SECONDS)
    public String helloWorld() {
        int fail = new Random().nextInt(10);
        if (fail < 9) {
            throw new RuntimeException("Failed to return.");
        }
        return "Hello World!";
    }

}