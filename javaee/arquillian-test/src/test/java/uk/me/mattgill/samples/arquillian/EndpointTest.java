package uk.me.mattgill.samples.arquillian;

import static javax.ws.rs.core.Response.Status.OK;
import static org.eclipse.microprofile.rest.client.RestClientBuilder.newBuilder;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class EndpointTest {

    @Deployment
    public static WebArchive createDeployment() {
        return create(WebArchive.class, "arquillian-test.war")
                .addPackage(CdiBean.class.getPackage())
                .addAsWebInfResource("web.xml");
    }

    @ArquillianResource
    private URL baseUrl;

    private Endpoint endpoint;

    @Before
    public void configureConnection() {
        endpoint = newBuilder()
                .baseUrl(baseUrl)
                .build(Endpoint.class);
    }

    @Test
    public void testCdiBeanInjection() {
        assertEquals(OK.getStatusCode(), endpoint.getResponse().getStatus());
    }

}