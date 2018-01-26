package uk.me.mattgill.samples.log.test;

import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import uk.me.mattgill.samples.log.test.beans.PrintingBean;

@ApplicationScoped
public class PrintService {

    @Inject
    private Instance<PrintingBean> printingBeans;

    @Resource
    private ManagedScheduledExecutorService executor;

    /**
     * On startup, start calling the print method on all registered beans every
     * 2 seconds.
     *
     * @param init the context object creating the bean.
     */
    public void startup(@Observes @Initialized(ApplicationScoped.class) Object init) {
        executor.scheduleAtFixedRate(() -> printingBeans.forEach(bean -> bean.printToLog()), 2, 2, TimeUnit.SECONDS);
    }

}
