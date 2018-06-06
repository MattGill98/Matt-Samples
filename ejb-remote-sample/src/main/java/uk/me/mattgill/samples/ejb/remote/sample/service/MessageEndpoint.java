package uk.me.mattgill.samples.ejb.remote.sample.service;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBAccessException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import uk.me.mattgill.samples.ejb.remote.sample.ejb.MessageBean;

@Path("/")
public class MessageEndpoint {

    private static final Logger LOGGER = Logger.getLogger(MessageEndpoint.class.getName());

    @EJB(beanName = "InsecureBean")
    private MessageBean bean;

    @GET
    public String getMessage() throws NamingException {
        try {
            Hashtable<String, String> props = new Hashtable<>(2);
            props.put(Context.SECURITY_PRINCIPAL, "username");
            props.put(Context.SECURITY_CREDENTIALS, "password");
            return MessageBean.class
                    .cast(new InitialContext(props).lookup("java:global/ejb-remote-sample-1.0-SNAPSHOT/SecureBean"))
                    .getMessage();
        } catch (EJBAccessException ex) {
            LOGGER.log(Level.WARNING, "Unable to get secure message, fetching insecure message.");
            return bean.getMessage();
        }
    }

}