package at.ac.tuwien.infosys.event;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * JMS implementation for an EventSubscriber
 * 
 * @author michael
 * 
 */
public class JMSEventSubscriber implements EventSubscriber, MessageListener {

	private TopicConnection connection;
	private TopicSession session;
	private TopicSubscriber subscriber;
	private Topic topic;

	private EventListener eventListener;

	public JMSEventSubscriber(EventListener eventListener) {
		this.eventListener = eventListener;
	}

	@Override
	public void subscribe(String connectionString, String topicName)
			throws EventSubscriberException {
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
			session = connection.createTopicSession(false,
					Session.AUTO_ACKNOWLEDGE);

			topic = session.createTopic(topicName);
			subscriber = session.createSubscriber(topic);

			subscriber.setMessageListener(this);
			connection.start();
		} catch (NamingException e) {
			throw new EventSubscriberException(
					"Error while creating connection", e);
		} catch (JMSException e) {
			throw new EventSubscriberException(
					"Error while creating connection", e);
		}
	}

	@Override
	public void unsubsribe() throws EventSubscriberException {
		try {
			if (subscriber != null)
				subscriber.close();
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

	@Override
	public void onMessage(Message message) {

		System.out.println("Received message");

		try {
			if (message instanceof ObjectMessage) {
				Object object = ((ObjectMessage) message).getObject();

				if (object instanceof GbotEvent) {
					eventListener.processEvent((GbotEvent) object);
				} else if (object instanceof Event) {
					eventListener.processEvent((Event) object);
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
