package uk.me.mattgill.samples.message.board.core.definitions;

import javax.jms.JMSConnectionFactoryDefinition;
import javax.jms.JMSDestinationDefinition;

@JMSConnectionFactoryDefinition(
        name = "java:global/jms/MessageBoardConnectionFactory",
        interfaceName = "javax.jms.ConnectionFactory",
        minPoolSize = 1,
        maxPoolSize = 5
)
@JMSDestinationDefinition(
        name = "java:global/jms/MessageBoardPostQueue",
        interfaceName = "javax.jms.Queue",
        destinationName = "MessageBoardPostQueue"
)
public class MessageDefinitions {

}
