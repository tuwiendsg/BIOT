package at.ac.tuwien.infosys.event;

/**
 * Utility/Helper class to store used topic and the connection-string to connect
 * to the JMS provider
 * 
 * @author michael
 * 
 */
public class JMSUtil {

	public static String connectionString = "amqp://admin:admin@clientID/carbon?brokerlist='tcp://128.131.172.236:5680'";
	public static String topicName = "Infosys_SOA_Event_Topic";

}
