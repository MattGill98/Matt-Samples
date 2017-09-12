package uk.me.mattgill.samples.ear.ejb;

import javax.ejb.Stateless;

@Stateless
public class EjbBeanImpl implements EjbBean {

    public String getMessage() {
        return "Fetched from an EJB bean.";
    }
}
