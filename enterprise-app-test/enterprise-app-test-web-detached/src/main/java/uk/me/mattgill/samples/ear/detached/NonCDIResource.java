package uk.me.mattgill.samples.ear.detached;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import uk.me.mattgill.samples.ear.ejb.EjbBean;

@Path("/")
public class NonCDIResource {
    
    private final EjbBean ejbBean;
    
    private final String uriPath;

    private static final Logger logger = Logger.getLogger(NonCDIResource.class.getName());
    
    public NonCDIResource(@Context final UriInfo info) {
        this.uriPath = info.getPath();
        logger.log(Level.INFO, "URI Info Path for {0}: {1}", new Object[]{getClass().getName(), this.uriPath});
        this.ejbBean = findEjb();
    }
    
    @GET
    @Path("/message")
    @Produces(MediaType.TEXT_PLAIN)
    public String getEjb() {
        return ejbBean.getMessage();
    }
    
    @GET
    @Path("/info")
    @Produces(MediaType.TEXT_PLAIN)
    public String getInfo() {
        return uriPath;
    }
    
    private EjbBean findEjb() {
        try {
            InitialContext ctx = new InitialContext();
            return (EjbBean) ctx.lookup("java:global/enterprise-app-test-ear-1.0-SNAPSHOT/enterprise-app-test-ejb-1.0-SNAPSHOT/EjbBeanImpl");
        } catch (NamingException ex) {
            Logger.getLogger(NonCDIResource.class.getName()).log(Level.SEVERE, "Error finding EJB.", ex);
            return null;
        }
    }
    
}
