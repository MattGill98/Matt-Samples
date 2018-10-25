package uk.me.mattgill.samples.ejb.remote.sample.service;

import java.util.Hashtable;
import java.util.Properties;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import uk.me.mattgill.samples.ejb.remote.sample.ejb.Bean;

@Path("/")
@Stateless
@RunAs(JaxrsApplication.SECURE_ROLE)
public class MessageEndpoint {

    private static String SECURE_JNDI_NAME = "secure-jndi";
    private static String INSECURE_JNDI_NAME = "insecure-jndi";
    private static String SECURE_EJB_NAME = "secure-ejb";
    private static String INSECURE_EJB_NAME = "insecure-ejb";

    @EJB(beanName = "SecureBean")
    private Bean secureBean;

    @EJB(beanName = "InsecureBean")
    private Bean insecureBean;

    @GET
    public JsonObject getMessage() throws NamingException {
        JsonObjectBuilder result = Json.createObjectBuilder();

        // Attempt to lookup secure EJB using JNDI
        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.enterprise.naming.impl.SerialInitContextFactory");
            props.setProperty(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
            props.setProperty(Context.PROVIDER_URL, "ormi://127.0.0.1/");
            props.setProperty(Context.SECURITY_AUTHENTICATION, "default");
            props.setProperty(Context.SECURITY_PRINCIPAL, "username");
            props.setProperty(Context.SECURITY_CREDENTIALS, "password");
            Bean.class.cast(new InitialContext(props).lookup("java:module/SecureBean")).doSomething();
            result.add(SECURE_JNDI_NAME, true);
        } catch (Throwable t) {
            result.add(SECURE_JNDI_NAME, t.getCause().getMessage());
        }

        // Attempt to lookup insecure EJB using JNDI
        try {
            Hashtable<String, String> props = new Hashtable<>(2);
            Bean.class.cast(new InitialContext(props).lookup("java:module/InsecureBean")).doSomething();
            result.add(INSECURE_JNDI_NAME, true);
        } catch (Throwable t) {
            result.add(INSECURE_JNDI_NAME, t.getCause().getMessage());
        }

        // Attempt to use injected secure EJB
        try {
            secureBean.doSomething();
            result.add(SECURE_EJB_NAME, true);
        } catch (Throwable t) {
            result.add(SECURE_EJB_NAME, t.getCause().getMessage());
        }

        // Attempt to use injected insecure EJB
        try {
            insecureBean.doSomething();
            result.add(INSECURE_EJB_NAME, true);
        } catch (Throwable t) {
            result.add(INSECURE_EJB_NAME, t.getCause().getMessage());
        }

        return result.build();
    }

}