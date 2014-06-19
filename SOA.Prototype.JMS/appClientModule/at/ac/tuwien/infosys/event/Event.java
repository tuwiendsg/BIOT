package at.ac.tuwien.infosys.event;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {

	private static final long serialVersionUID = -5731304309080820450L;

	private Date timestamp;
	private String message;

	public Event() {
		super();
	}

	public Event(Date timestamp, String message) {
		super();
		this.timestamp = timestamp;
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Event [timestamp=" + timestamp + ", message=" + message + "]";
	}

}
