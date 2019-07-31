package uk.me.mattgill.samples.corba.common;

import javax.ejb.Remote;

@Remote
public interface Logger {
    LogResult log(String message);
}