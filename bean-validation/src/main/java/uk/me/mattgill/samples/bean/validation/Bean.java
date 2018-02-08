package uk.me.mattgill.samples.bean.validation;

import javax.enterprise.context.Dependent;
import javax.validation.constraints.NotNull;

import uk.me.mattgill.samples.bean.validation.constraints.Alphanumeric;

@Dependent
public class Bean {

    @NotNull
    @Alphanumeric
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}