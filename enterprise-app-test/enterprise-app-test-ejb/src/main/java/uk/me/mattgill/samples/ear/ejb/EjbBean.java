package uk.me.mattgill.samples.ear.ejb;

import javax.ejb.Remote;

@Remote
public interface EjbBean {
    
    public String getMessage();

}
