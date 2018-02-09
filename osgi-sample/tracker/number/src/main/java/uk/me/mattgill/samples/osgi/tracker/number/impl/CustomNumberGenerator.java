package uk.me.mattgill.samples.osgi.tracker.number.impl;

import uk.me.mattgill.samples.osgi.tracker.number.api.NumberGenerator;

public class CustomNumberGenerator implements NumberGenerator {

    @Override
    public int getNumber() {
        return 2;
    }

}