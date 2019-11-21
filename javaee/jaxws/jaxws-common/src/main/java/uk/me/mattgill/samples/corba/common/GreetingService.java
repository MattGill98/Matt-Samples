package uk.me.mattgill.samples.corba.common;

import javax.jws.WebService;

@WebService(
    targetNamespace = "http://localhost:8080/GreetingService/"
)
public interface GreetingService {
    public String greet(String name);
}