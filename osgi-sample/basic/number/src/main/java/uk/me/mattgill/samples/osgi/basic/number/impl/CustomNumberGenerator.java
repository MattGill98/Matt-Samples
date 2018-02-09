package uk.me.mattgill.samples.osgi.basic.number.impl;

import uk.me.mattgill.samples.osgi.basic.number.api.NumberGenerator;

public class CustomNumberGenerator implements NumberGenerator {

    @Override
    public int getNumber() {
        return 1;
    }

}