package at.ac.tuwien.infosys.event;

import java.util.Date;

public class GbotEvent extends Event {

	private static final long serialVersionUID = -3879300340206929949L;

	private String id;
	private String instanceId;
	private String customerId;
	private String providerId;
	private GbotType type;
	private GbotAction action;

	public GbotEvent() {
		super();
	}

	public GbotEvent(String id, String instanceId, String customerId,
			String providerId, GbotType type, GbotAction action) {
		super();
		this.id = id;
		this.instanceId = instanceId;
		this.customerId = customerId;
		this.providerId = providerId;
		this.type = type;
		this.action = action;
	}

	public GbotEvent(Date timestamp, String message, String id,
			String instanceId, String customerId, String providerId,
			GbotType type, GbotAction action) {
		super(timestamp, message);
		this.id = id;
		this.instanceId = instanceId;
		this.customerId = customerId;
		this.providerId = providerId;
		this.type = type;
		this.action = action;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public GbotType getType() {
		return type;
	}

	public void setType(GbotType type) {
		this.type = type;
	}

	public GbotAction getAction() {
		return action;
	}

	public void setAction(GbotAction action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "GbotEvent [super()=" + super.toString() + ", id=" + id
				+ ", instanceId=" + instanceId + ", customerId=" + customerId
				+ ", providerId=" + providerId + ", type=" + type + ", action="
				+ action + "]";
	}

	public enum GbotType {
		Maintenance, Management
	}

	public enum GbotAction {
		subscription_added, subscription_removed, starting, stopping, consuming_resources
	}

}
