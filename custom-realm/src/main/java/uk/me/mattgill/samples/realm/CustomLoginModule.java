package uk.me.mattgill.samples.realm;

import javax.security.auth.login.LoginException;

import com.sun.enterprise.security.BasePasswordLoginModule;

import org.glassfish.security.common.PrincipalImpl;

public class CustomLoginModule extends BasePasswordLoginModule {

    @Override
    protected void authenticateUser() throws LoginException {
        String[] groups = new CustomRealm().authenticate(_username, _passwd);

        if (groups == null) {
            throw new LoginException("Authentication failed for user " + _username);
        }

        _subject.getPrincipals().add(new PrincipalImpl(_username));
        commitUserAuthentication(groups);
    }

}