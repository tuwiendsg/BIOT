package at.ac.tuwien.infosys.event;

/**
 * Interface to subscribe to a topic
 * 
 * @author michael
 *
 */
public interface EventSubscriber {
	
	/**
	 * Method to subscribe to a topic.
	 * 
	 * @param connectionString
	 * @param topicName
	 * @throws EventSubscriberException
	 */
	public void subscribe(String connectionString, String topicName) throws EventSubscriberException;
	
	/**
	 * Method to remove the subscription.
	 * 
	 * @throws EventSubscriberException
	 */
	public void unsubsribe() throws EventSubscriberException;

}
