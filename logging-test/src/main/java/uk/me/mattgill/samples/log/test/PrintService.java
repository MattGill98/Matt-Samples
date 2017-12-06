package uk.me.mattgill.samples.log.test;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import uk.me.mattgill.samples.log.test.beans.PrintingBean;

@ApplicationScoped
public class PrintService {

    @Inject
    Instance<PrintingBean> printingBeans;

    public void startup(@Observes @Initialized(ApplicationScoped.class) Object init) {
        for (PrintingBean bean : printingBeans) {
            bean.printToLog();
        }
    }

}
