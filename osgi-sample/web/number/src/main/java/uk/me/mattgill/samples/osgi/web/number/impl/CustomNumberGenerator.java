package uk.me.mattgill.samples.osgi.web.number.impl;

import javax.enterprise.context.RequestScoped;

import uk.me.mattgill.samples.osgi.web.number.api.NumberGenerator;

@RequestScoped
public class CustomNumberGenerator implements NumberGenerator {

    @Override
    public int getNumber() {
        return 1;
    }

}