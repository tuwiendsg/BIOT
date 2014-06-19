package at.ac.tuwien.infosys.event;

public class EventPublisherException extends Exception {

	private static final long serialVersionUID = 3015235813114570649L;

	public EventPublisherException() {
		super();
	}

	public EventPublisherException(String message) {
		super(message);
	}

	public EventPublisherException(String message, Throwable cause) {
		super(message, cause);
	}

	public EventPublisherException(Throwable cause) {
		super(cause);
	}

}
