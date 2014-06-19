package at.ac.tuwien.infosys.event;

/**
 * Interface to publish to a topic.
 * 
 * @author michael
 *
 */
public interface EventPublisher {
	
	/**
	 * Method to start the publisher.
	 * 
	 * @param connectionString
	 * @param topicName
	 * @throws EventPublisherException
	 */
	public void start(String connectionString, String topicName) throws EventPublisherException;

	/**
	 * Publishes an event to the topic.
	 * 
	 * @param event
	 * @throws EventPublisherException
	 */
	public void publishEvent(Event event) throws EventPublisherException;
	
	/**
	 * Method to stop the publisher.
	 */
	public void stop();

}
