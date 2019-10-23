package uk.me.mattgill.samples.arquillian;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CdiBeanTest {

    @Deployment
    public static WebArchive createDeployment() {
        return create(WebArchive.class, "arquillian-test.war")
                .addPackage(CdiBean.class.getPackage())
                .addAsWebInfResource("web.xml");
    }

    @Inject
    private CdiBean bean;

    @Test
    public void testCdiBeanInjection() {
        assertNotNull(bean);
    }

}