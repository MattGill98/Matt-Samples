package uk.me.mattgill.samples.jaxrs.hello.world;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
@RequestScoped
public class TestResource {

    @Inject
    private UnserializableBean bean;

    @GET
    public String helloWorld() {
        return bean.getUuid().toString();
    }

}