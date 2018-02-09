package uk.me.mattgill.samples.osgi.dynamic.number.impl;

import uk.me.mattgill.samples.osgi.dynamic.number.api.NumberGenerator;

public class CustomNumberGenerator implements NumberGenerator {

    @Override
    public int getNumber() {
        return 1;
    }

}