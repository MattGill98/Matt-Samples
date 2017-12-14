package uk.me.mattgill.samples.jsf.cdi.test;

import java.io.Serializable;

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

    @Inject
    private FacesContext facesContext;

    public void helloWorld() {
        facesContext.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Hello From JSF!", "Hello From JSF in more detail!"));
    }

}