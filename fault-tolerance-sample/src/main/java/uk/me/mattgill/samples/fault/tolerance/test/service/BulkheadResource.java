package uk.me.mattgill.samples.fault.tolerance.test.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.faulttolerance.Bulkhead;

@Path("/bulkhead")
@RequestScoped
public class BulkheadResource {

    @GET
    @Path("/semaphore")
    @Bulkhead(value = 1)
    public String semaphore() {
        return "Hello World!";
    }

    @GET
    @Path("/thread")
    @Asynchronous
    @Bulkhead(value = 1, waitingTaskQueue = 5)
    public Future<String> thread() {
        return CompletableFuture.completedFuture("Hello World!");
    }

}