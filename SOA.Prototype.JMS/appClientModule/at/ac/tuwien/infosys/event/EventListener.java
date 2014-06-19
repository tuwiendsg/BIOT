package at.ac.tuwien.infosys.event;

/**
 * Interface which has to implemented to process the received Event.
 * 
 * @author michael
 * 
 */
public interface EventListener {

	/**
	 * Method gets invoked when an event was received.
	 * 
	 * @param event
	 */
	public void processEvent(Event event);

}
