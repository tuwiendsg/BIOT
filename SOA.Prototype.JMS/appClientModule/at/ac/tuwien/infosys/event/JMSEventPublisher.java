package at.ac.tuwien.infosys.event;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * JMS implementation for an EventPublisher
 * 
 * @author michael
 * 
 */
public class JMSEventPublisher implements EventPublisher {

	private TopicConnection connection;
	private TopicSession session;
	private TopicPublisher publisher;
	private Topic topic;

	public JMSEventPublisher() {
	}

	@Override
	public void start(String connectionString, String topicName)
			throws EventPublisherException {
		Properties initialContextProperties = new Properties();
		initialContextProperties.put("java.naming.factory.initial",
				"org.apache.qpid.jndi.PropertiesFileInitialContextFactory");
		initialContextProperties.put("connectionfactory.qpidConnectionfactory",
				connectionString);

		try {
			InitialContext initialContext = new InitialContext(
					initialContextProperties);
			TopicConnectionFactory connectionFactory = (TopicConnectionFactory) initialContext
					.lookup("qpidConnectionfactory");

			connection = connectionFactory.createTopicConnection();
			connection.start();

			session = connection.createTopicSession(false,
					Session.AUTO_ACKNOWLEDGE);

			topic = session.createTopic(topicName);
			publisher = session.createPublisher(topic);
		} catch (NamingException e) {
			throw new EventPublisherException(
					"Error while creating connection", e);
		} catch (JMSException e) {
			throw new EventPublisherException(
					"Error while creating connection", e);
		}
	}

	@Override
	public void publishEvent(Event event) throws EventPublisherException {
		ObjectMessage message;
		try {
			message = session.createObjectMessage(event);
			publisher.publish(message);
		} catch (JMSException e) {
			throw new EventPublisherException(
					"Could not send event to JMS Topic!", e);
		}
	}

	@Override
	public void stop() {

		try {
			if (publisher != null)
				publisher.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}

		try {
			if (session != null)
				session.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}

		try {
			if (connection != null) {
				connection.stop();
				connection.close();
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("Starting Publisher .....");
		EventPublisher publisher = new JMSEventPublisher();
		try {
			publisher.start(JMSUtil.connectionString, JMSUtil.topicName);

			publisher.publishEvent(new GbotEvent());

		} catch (EventPublisherException e) {
			e.printStackTrace();
		} finally {
			publisher.stop();
		}

		System.out.println("Publisher stopped .....");
	}

}
