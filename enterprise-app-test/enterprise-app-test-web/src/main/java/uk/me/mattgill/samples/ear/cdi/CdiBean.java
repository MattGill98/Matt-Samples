package uk.me.mattgill.samples.ear.cdi;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class CdiBean {
    
    public String getMessage() {
        return "Fetched from a CDI bean.";
    }
    
}
