package uk.me.mattgill.samples.jacc;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

public class TestRoleMapper {

    private Map<String, List<String>> groupToRoles = new HashMap<>();

    private boolean oneToOneMapping;
    private boolean anyAuthenticatedUserRoleMapped = false;

    public TestRoleMapper(String contextID, Collection<String> allDeclaredRoles) {
        if (!tryGlassFish(contextID, allDeclaredRoles)) {
            oneToOneMapping = true;
        }
    }

    public List<String> getMappedRoles(Principal[] principals, Subject subject) {
        return getMappedRoles(asList(principals), subject);
    }

    public boolean isAnyAuthenticatedUserRoleMapped() {
        return anyAuthenticatedUserRoleMapped;
    }

    /**
     * Tries to get the roles from the principals list and only if it fails, falls
     * back to looking at the Subject.
     *
     * Liberty is the only known server that falls back.
     *
     * @param principals the primary entities to look in for roles
     * @param subject    the fall back to use if looking at principals fails
     * @return a list of mapped roles
     */
    public List<String> getMappedRoles(Iterable<Principal> principals, Subject subject) {

        // Extract the list of groups from the principals. These principals typically
        // contain
        // different kind of principals, some groups, some others. The groups are
        // unfortunately vendor
        // specific.
        List<String> groups = getGroups(principals, subject);

        // Map the groups to roles. E.g. map "admin" to "administrator". Some servers
        // require this.
        return mapGroupsToRoles(groups);
    }

    private List<String> mapGroupsToRoles(List<String> groups) {

        if (oneToOneMapping) {
            // There is no mapping used, groups directly represent roles.
            return groups;
        }

        List<String> roles = new ArrayList<>();

        for (String group : groups) {
            if (groupToRoles.containsKey(group)) {
                roles.addAll(groupToRoles.get(group));
            } else {
                // Default to 1:1 mapping when group is not explicitly mapped
                roles.add(group);
            }
        }

        return roles;
    }

    private boolean tryGlassFish(String contextID, Collection<String> allDeclaredRoles) {

        try {
            Class<?> SecurityRoleMapperFactoryClass = Class
                    .forName("org.glassfish.deployment.common.SecurityRoleMapperFactory");

            Object factoryInstance = Class.forName("org.glassfish.internal.api.Globals")
                    .getMethod("get", SecurityRoleMapperFactoryClass.getClass())
                    .invoke(null, SecurityRoleMapperFactoryClass);

            Object securityRoleMapperInstance = SecurityRoleMapperFactoryClass.getMethod("getRoleMapper", String.class)
                    .invoke(factoryInstance, contextID);

            @SuppressWarnings("unchecked")
            Map<String, Subject> roleToSubjectMap = (Map<String, Subject>) Class
                    .forName("org.glassfish.deployment.common.SecurityRoleMapper").getMethod("getRoleToSubjectMapping")
                    .invoke(securityRoleMapperInstance);

            for (String role : allDeclaredRoles) {
                if (roleToSubjectMap.containsKey(role)) {
                    Set<Principal> principals = roleToSubjectMap.get(role).getPrincipals();

                    List<String> groups = getGroups(principals, null);
                    for (String group : groups) {
                        if (!groupToRoles.containsKey(group)) {
                            groupToRoles.put(group, new ArrayList<String>());
                        }
                        groupToRoles.get(group).add(role);
                    }

                    if ("**".equals(role) && !groups.isEmpty()) {
                        // JACC spec 3.2 states:
                        //
                        // "For the any "authenticated user role", "**", and unless an application
                        // specific mapping has
                        // been established for this role,
                        // the provider must ensure that all permissions added to the role are granted
                        // to any
                        // authenticated user."
                        //
                        // Here we check for the "unless" part mentioned above. If we're dealing with
                        // the "**" role here
                        // and groups is not
                        // empty, then there's an application specific mapping and "**" maps only to
                        // those groups, not
                        // to any authenticated user.
                        anyAuthenticatedUserRoleMapped = true;
                    }
                }
            }

            return true;

        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            return false;
        }
    }

    /**
     * Extracts the groups from the vendor specific principals.
     *
     * @param principals the primary entities to look in for groups
     * @param subject    the fall back to use for finding groups, may be null
     * @return a list of (non-mapped) groups
     */
    @SuppressWarnings("unchecked")
    public List<String> getGroups(Iterable<Principal> principals, Subject subject) {
        List<String> groups = new ArrayList<>();

        for (Principal principal : principals) {
            groups.add(principal.getName());
        }

        if (subject == null) {
            return groups;
        }

        @SuppressWarnings("rawtypes")
        Set<Hashtable> tables = subject.getPrivateCredentials(Hashtable.class);
        if (tables != null && !tables.isEmpty()) {
            @SuppressWarnings("rawtypes")
            Hashtable table = tables.iterator().next();

            groups = (List<String>) table.get("com.ibm.wsspi.security.cred.groups");

            return groups != null ? groups : emptyList();
        }

        return groups;
    }

    public List<String> principalToGroups(Principal principal) {
        List<String> groups = new ArrayList<>();
        groups.add(principal.getName());
        return groups;
    }

}