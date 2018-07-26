package uk.me.mattgill.samples.jaxrs.hello.world;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

@SessionScoped
public class UnserializableBean {

    private UUID uuid;

    @PostConstruct
    void init() {
        uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

}