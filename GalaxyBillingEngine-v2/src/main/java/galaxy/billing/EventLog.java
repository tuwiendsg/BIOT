package galaxy.billing;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name = "event_log")
public class EventLog {

	@ManyToOne
    @JoinColumn(name = "Gbots_id", referencedColumnName = "id")
    private Gbot gbotsId;

	@Column(name = "InstanceId", length = 200)
    private String instanceId;

	@Column(name = "PublishDate")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date publishDate;

	@Column(name = "CustomerId", length = 100)
    private String customerId;

	@Column(name = "ProviderId", length = 100)
    private String providerId;

	@Column(name = "GbotType", length = 50)
    private String gbotType;

	@Column(name = "GbotAction", length = 30)
    private String gbotAction;

	public Gbot getGbotsId() {
        return gbotsId;
    }

	public void setGbotsId(Gbot gbotsId) {
        this.gbotsId = gbotsId;
    }

	public String getInstanceId() {
        return instanceId;
    }

	public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

	public Date getPublishDate() {
        return publishDate;
    }

	public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
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

	public String getGbotType() {
        return gbotType;
    }

	public void setGbotType(String gbotType) {
        this.gbotType = gbotType;
    }

	public String getGbotAction() {
        return gbotAction;
    }

	public void setGbotAction(String gbotAction) {
        this.gbotAction = gbotAction;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Integer getId() {
        return this.id;
    }

	public void setId(Integer id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new EventLog().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countEventLogs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EventLog o", Long.class).getSingleResult();
    }

	public static List<EventLog> findAllEventLogs() {
        return entityManager().createQuery("SELECT o FROM EventLog o", EventLog.class).getResultList();
    }

	public static EventLog findEventLog(Integer id) {
        if (id == null) return null;
        return entityManager().find(EventLog.class, id);
    }

	public static List<EventLog> findEventLogEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EventLog o", EventLog.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            EventLog attached = EventLog.findEventLog(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public EventLog merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EventLog merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
