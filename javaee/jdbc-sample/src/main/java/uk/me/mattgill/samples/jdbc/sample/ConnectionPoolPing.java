package uk.me.mattgill.samples.jdbc.sample;

import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.sql.DataSource;

@ApplicationScoped
public class ConnectionPoolPing {

    private Logger logger = Logger.getLogger(ConnectionPoolPing.class.getName());

    private ScheduledExecutorService executor;

    @Resource(lookup = "jdbc/__default")
    private DataSource dataSource;

    @SuppressWarnings("unused")
    private void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleWithFixedDelay(() -> {
            try (Connection conn = dataSource.getConnection()) {
                logger.info("Got connection. Catalog name: " + conn.getCatalog() + ".");
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }, 1, 3, TimeUnit.SECONDS);
    }

    @PreDestroy
    private void close() {
        executor.shutdown();
    }

}