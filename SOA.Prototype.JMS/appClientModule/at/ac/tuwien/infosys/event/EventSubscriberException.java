package at.ac.tuwien.infosys.event;

public class EventSubscriberException extends Exception {

	private static final long serialVersionUID = 3015235813114570649L;

	public EventSubscriberException() {
		super();
	}

	public EventSubscriberException(String message) {
		super(message);
	}

	public EventSubscriberException(String message, Throwable cause) {
		super(message, cause);
	}

	public EventSubscriberException(Throwable cause) {
		super(cause);
	}

}
