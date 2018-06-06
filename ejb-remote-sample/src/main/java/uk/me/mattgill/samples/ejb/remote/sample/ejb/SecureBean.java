package uk.me.mattgill.samples.ejb.remote.sample.ejb;

import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

@Stateless
@DeclareRoles("messenger")
public class SecureBean implements MessageBean {

    private static final Logger LOGGER = Logger.getLogger(SecureBean.class.getName());

    @Override
    @RolesAllowed("messenger")
    public String getMessage() {
        LOGGER.info("Returning message from secure bean!");
        return "This is a secure message!";
    }

}