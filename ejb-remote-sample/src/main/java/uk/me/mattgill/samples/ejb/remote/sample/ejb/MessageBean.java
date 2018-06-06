package uk.me.mattgill.samples.ejb.remote.sample.ejb;

import javax.ejb.Local;

@Local
public interface MessageBean {

    String getMessage();

}