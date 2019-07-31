package uk.me.mattgill.samples.ejb.remote.sample.service;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
@DeclareRoles(JaxrsApplication.SECURE_ROLE)
public class JaxrsApplication extends Application {

    public static final String SECURE_ROLE = "privileged";

}