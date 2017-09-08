package uk.me.mattgill.samples.rest.request;

import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Named
@Dependent
public class HelloWorldBackingBean {
    
    @Inject
    private ApiRequester api;

    public String getMessage() {
        return api.get("hello", String.class);
    }
    
}
