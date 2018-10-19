package uk.me.mattgill.samples.realm;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import com.sun.enterprise.security.auth.realm.NoSuchUserException;

public class CustomStore {

    private static Logger LOGGER = Logger.getLogger(CustomRealm.class.getName());

    private Map<String, char[]> passwordMap;
    private Map<String, Collection<String>> groupMap;
    private HashSet<String> groups;

    public CustomStore() {
        this.passwordMap = new HashMap<>();
        this.groupMap = new HashMap<>();
        this.groups = new HashSet<>();
    }

    public synchronized Collection<String> getUsers() {
        return passwordMap.keySet();
    }

    public synchronized Collection<String> getGroups() {
        return groups;
    }

    public synchronized char[] getPassword(String username) throws NoSuchUserException {
        if (!containsUser(username)) {
            LOGGER.log(WARNING, "No password found for user {0}.", username);
            throw new NoSuchUserException("User " + username + " not found.");
        }

        char[] foundPassword = passwordMap.get(username);
        LOGGER.log(INFO, "Found password for user {0}: {1}.",
                new Object[] { username,
                    new String(foundPassword) });
        return foundPassword;
    }

    public synchronized Collection<String> getGroups(String username) throws NoSuchUserException {
        if (!containsUser(username)) {
            LOGGER.log(WARNING, "No groups found for user {0}.", username);
            throw new NoSuchUserException("User " + username + " not found.");
        }

        Collection<String> foundGroups = groupMap.get(username);
        LOGGER.log(INFO, "Found groups for user {0}: {1}.",
                new Object[] { username, Arrays.toString(foundGroups.toArray()) });
        return foundGroups;
    }

    public boolean containsUser(String username) {
        return passwordMap.containsKey(username);
    }

    public synchronized void addUser(String username, char[] password, Collection<String> groups) {
        LOGGER.log(INFO, "Adding user {0}.", username);
        passwordMap.put(username, password);
        groupMap.put(username, groups);
        this.groups.addAll(groups);
    }

    public synchronized void removeUser(String username) throws NoSuchUserException {
        LOGGER.log(INFO, "Removing user {0}.", username);
        if (!containsUser(username)) {
            throw new NoSuchUserException("User " + username + " not found.");
        }
        groups.removeAll(groupMap.get(username));
        passwordMap.remove(username);
        groupMap.remove(username);
    }

    public synchronized void updateUser(String username, String newUsername, char[] password, Collection<String> groups)
            throws NoSuchUserException {
        LOGGER.log(INFO, "Updating user {0}.", username);
        removeUser(username);
        addUser(newUsername, password, groups);
    }

}