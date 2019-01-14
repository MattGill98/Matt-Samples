package uk.me.mattgill.samples.grizzly.addon;

import org.glassfish.grizzly.http.server.HttpServer;

public interface GrizzlyAddon {
    void register(HttpServer httpServer);
}