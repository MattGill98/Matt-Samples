package uk.me.mattgill.samples.corba;

import static java.util.logging.Level.INFO;

import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.jws.WebService;

import uk.me.mattgill.samples.corba.common.GreetingService;

@WebService(
    targetNamespace = "http://localhost:8080/GreetingService/",
    endpointInterface = "uk.me.mattgill.samples.corba.common.GreetingService",
    serviceName = "GreetingService"
)
public class Greeting implements GreetingService {

    private static final Logger LOGGER = Logger.getLogger(Greeting.class.getName());

    @Override
    @WebMethod
    public String greet(String name) {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.log(INFO, "Greeting {0}.", new Object[]{name});
        return "Hello " + name;
    }

}