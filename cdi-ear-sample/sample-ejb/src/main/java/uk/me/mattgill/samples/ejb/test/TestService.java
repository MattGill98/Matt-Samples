package uk.me.mattgill.samples.ejb.test;

import javax.ejb.Singleton;

@Singleton
public class TestService {

    public int getVersion() {
        return 1;
    }
}
