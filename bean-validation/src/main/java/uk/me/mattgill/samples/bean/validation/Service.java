package uk.me.mattgill.samples.bean.validation;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

@ApplicationScoped
public class Service {

    private Logger logger = Logger.getLogger(Service.class.getName());

    @Inject
    private Bean bean;

    @Inject
    private Validator validator;

    @Resource
    private ManagedScheduledExecutorService executor;

    @SuppressWarnings("unused")
    private void validateBean(@Observes @Initialized(ApplicationScoped.class) Object init) {
        bean.setName("testing!");

        // Validate the bean after a second
        executor.schedule(() -> {
            Set<ConstraintViolation<Bean>> violations = validator.validate(bean);
            if (violations.size() > 0) {
                logger.log(Level.SEVERE, "The bean wasn't valid.", new ConstraintViolationException(violations));
            } else {
                logger.info("The bean was valid!");
            }
        }, 1, TimeUnit.SECONDS);
    }

}