package uk.me.mattgill.samples.payara.embedded;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;

public class Main {

    private static Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            GlassFishRuntime runtime = GlassFishRuntime.bootstrap();
            GlassFishProperties glassfishProperties = new GlassFishProperties();
            glassfishProperties.setPort("http-listener", 8080);
            glassfishProperties.setPort("https-listener", 8081);
            GlassFish glassfish = runtime.newGlassFish(glassfishProperties);
            glassfish.start();
            glassfish.getDeployer().deploy(new URI("https://community.jboss.org/servlet/JiveServlet/download/588259-27006/clusterjsp.war"), "--name", "clusterjsp", "--contextroot", "/");
            glassfish.stop();
        } catch (GlassFishException ex) {
            LOGGER.log(Level.SEVERE, "Error starting server.", ex);
        } catch (URISyntaxException ex) {
            LOGGER.log(Level.SEVERE, "Error fetching clusterjsp.", ex);
		}
    }
}
