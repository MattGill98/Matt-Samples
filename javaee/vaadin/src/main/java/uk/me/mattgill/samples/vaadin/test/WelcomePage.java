package uk.me.mattgill.samples.vaadin.test;

import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.inject.Inject;

@CDIUI("")
public class WelcomePage extends UI {
    
    @Inject
    private TestBean bean;

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();
        Label label = new Label(bean.getMessage());
        setContent(new VerticalLayout(label));
    }

}
