package uk.me.mattgill.samples.vaadin.test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class TestBean {

    public String getMessage() {
        return "Hello World! The time is: " + LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).split("\\.")[0];
    }
    
}
