package uk.me.mattgill.samples.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class Hello {

    @WebMethod
    public String helloWorld(int num) {
        return "Number: " + num;
    }

}