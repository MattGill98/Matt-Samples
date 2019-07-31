package uk.me.mattgill.samples.client.auth;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.sun.enterprise.config.serverbeans.Domain;

import org.glassfish.internal.api.Globals;

import uk.me.mattgill.samples.client.auth.util.KeystoreManager;
import uk.me.mattgill.samples.client.auth.util.PayaraCommand;

@Startup
@Singleton
public class DomainConfiguration {

    private static final Logger LOGGER = Logger.getLogger(DomainConfiguration.class.getName());

    @PostConstruct
    protected void configureCertificates() {
        PayaraCommand command = new PayaraCommand("list-jvm-options").execute();
        if (!command.isSuccess()) {
            LOGGER.severe("Failed to fetch JVM options.");
            return;
        }

        String domainDir = Globals.getDefaultHabitat().getService(Domain.class).getLogRoot().replace("/logs", "");

        String trustStoreLocation = command.getOutput()
                .split("-Djavax.net.ssl.trustStore=")[1]
                .split("\\n")[0]
                .replace("${com.sun.aas.instanceRoot}", domainDir);

        try {
            KeystoreManager keystore = new KeystoreManager(new File(trustStoreLocation), "changeit");
            keystore.add("clientcert", getClass().getClassLoader().getResourceAsStream("mycert.crt"));
            keystore.save();
		} catch (NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException ex) {
			sneakyThrow(ex);
		}
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }
}