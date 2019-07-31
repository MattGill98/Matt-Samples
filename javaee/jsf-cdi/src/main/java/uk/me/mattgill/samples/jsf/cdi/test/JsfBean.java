package uk.me.mattgill.samples.jsf.cdi.test;

import java.io.Serializable;
import java.util.Random;

import javax.faces.annotation.FacesConfig;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@FacesConfig(version = FacesConfig.Version.JSF_2_3)
@Named
@ViewScoped
public class JsfBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Random random = new Random();

    private boolean imageRendered;
    private boolean messageRendered;

    @Inject
    private FacesContext facesContext;

    public void toggleMessage() {
        messageRendered = !messageRendered;
        if (messageRendered) {
            facesContext.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    String.format("New random number: %d.", random.nextInt()), "A generated random number."));
        }
    }

    public void toggleImage() {
        imageRendered = !imageRendered;
    }

    public boolean isMessageRendered() {
        return messageRendered;
    }

    public boolean isImageRendered() {
        return imageRendered;
    }

}