package uk.me.mattgill.samples.ejb.remote;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class JndiListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                System.out.println("Message received: " + TextMessage.class.cast(message).getText());
            } catch (JMSException e) {
                e.printStackTrace();
			}
        }
	}

}