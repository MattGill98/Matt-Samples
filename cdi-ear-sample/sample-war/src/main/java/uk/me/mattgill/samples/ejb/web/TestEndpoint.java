package uk.me.mattgill.samples.ejb.web;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uk.me.mattgill.samples.ejb.service.SchedulingService;

@Path("")
public class TestEndpoint {

    @Inject
    private SchedulingService bean;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public int getMessage() {
        return bean.getTime();
    }
}
