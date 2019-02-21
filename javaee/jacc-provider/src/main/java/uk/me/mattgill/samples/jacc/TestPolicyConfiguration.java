package uk.me.mattgill.samples.jacc;

import javax.security.jacc.PolicyContextException;

public class TestPolicyConfiguration extends TestPolicyConfigurationPermissions {

    private TestRoleMapper roleMapper;

    public TestPolicyConfiguration(String contextID) {
        super(contextID);
    }

    @Override
    public void commit() throws PolicyContextException {
        roleMapper = new TestRoleMapper(getContextID(), getPerRolePermissions().keySet());
    }

    public TestRoleMapper getRoleMapper() {
        return roleMapper;
    }

}