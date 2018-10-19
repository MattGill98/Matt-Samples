package uk.me.mattgill.samples.realm;

import static java.util.Arrays.asList;
import static java.util.Collections.enumeration;
import static java.util.logging.Level.INFO;

import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

import com.sun.enterprise.security.BaseRealm;
import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.InvalidOperationException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchUserException;
import com.sun.enterprise.security.util.IASSecurityException;

public class CustomRealm extends BaseRealm {

    private static Logger LOGGER = Logger.getLogger(CustomRealm.class.getName());

    private CustomStore userStore;

    public CustomRealm() {
        this.userStore = new CustomStore();
        userStore.addUser("username", "password".toCharArray(), asList("testgroup"));
    }

    public String[] authenticate(String user, char[] password) {
        LOGGER.log(INFO, "Authenticating user with username: {0}, password: {1}.",
                new Object[] { user, new String(password) });

        if (!userStore.containsUser(user)) {
            return null;
        }
        try {
            return addAssignGroups((String[]) userStore.getGroups(user).toArray());
        } catch (NoSuchUserException e) {
            return null;
        }
    }

    @Override
    protected void init(Properties props) throws BadRealmException, NoSuchRealmException {
        LOGGER.log(INFO, "Initialising realm.");

        // Set the JAAS context
        setProperty(JAAS_CONTEXT_PARAM, "customRealm");

        super.init(props);
    }

    @Override
    public String getAuthType() {
        return "CustomRealm";
    }

    @Override
    public Enumeration getUserNames() throws BadRealmException {
        return enumeration(userStore.getUsers());
    }

    @Override
    public Enumeration getGroupNames() throws BadRealmException {
        return enumeration(userStore.getGroups());
    }

    @Override
    public Enumeration getGroupNames(String username) throws InvalidOperationException, NoSuchUserException {
        return enumeration(userStore.getGroups(username));
    }

    @Override
    public void addUser(String name, char[] password, String[] groupList)
            throws BadRealmException, IASSecurityException {
        userStore.addUser(name, password, asList(groupList));
    }

    @Override
    public void removeUser(String name) throws NoSuchUserException, BadRealmException {
        userStore.removeUser(name);
    }

    @Override
    public void updateUser(String name, String newName, char[] password, String[] groups)
            throws NoSuchUserException, BadRealmException, IASSecurityException {
        removeUser(name);
        addUser(newName, password, groups);
    }

    @Override
    public boolean supportsUserManagement() {
        return true;
    }

}