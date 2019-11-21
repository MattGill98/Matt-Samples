package uk.me.mattgill.samples.corba.client;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import uk.me.mattgill.samples.corba.common.GreetingService;

public class Main {

    public static void main(String[] args) throws Exception {
        final String endpoint = "http://localhost:8080/GreetingService";

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(GreetingService.class);
        factory.setWsdlLocation(endpoint + "?wsdl");
        factory.setAddress(endpoint);
        GreetingService service = (GreetingService) factory.create();

        System.out.println(service.greet("Matt"));
    }
}
