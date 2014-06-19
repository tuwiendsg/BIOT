package galaxy.billing;

import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name = "gbot")
public class Gbot {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new Gbot().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countGbots() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Gbot o", Long.class).getSingleResult();
    }

	public static List<Gbot> findAllGbots() {
        return entityManager().createQuery("SELECT o FROM Gbot o", Gbot.class).getResultList();
    }

	public static Gbot findGbot(Integer id) {
        if (id == null) return null;
        return entityManager().find(Gbot.class, id);
    }

	public static List<Gbot> findGbotEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Gbot o", Gbot.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Gbot attached = Gbot.findGbot(this.id);
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
    public Gbot merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Gbot merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
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

	@OneToMany(mappedBy = "gbotsId")
    private Set<BillLine> billLines;

	@OneToMany(mappedBy = "gbotsId")
    private Set<EventLog> eventLogs;

	@ManyToOne
    @JoinColumn(name = "Gbot_price_id", referencedColumnName = "id")
    private GbotPrice gbotPriceId;

	@Column(name = "Name", length = 50)
    private String name;

	@Column(name = "Type", length = 50)
    @NotNull
    private String type;

	@Column(name = "MeteringUnit", length = 50)
    private String meteringUnit;

	public Set<BillLine> getBillLines() {
        return billLines;
    }

	public void setBillLines(Set<BillLine> billLines) {
        this.billLines = billLines;
    }

	public Set<EventLog> getEventLogs() {
        return eventLogs;
    }

	public void setEventLogs(Set<EventLog> eventLogs) {
        this.eventLogs = eventLogs;
    }

	public GbotPrice getGbotPriceId() {
        return gbotPriceId;
    }

	public void setGbotPriceId(GbotPrice gbotPriceId) {
        this.gbotPriceId = gbotPriceId;
    }

	public String getName() {
        return name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getType() {
        return type;
    }

	public void setType(String type) {
        this.type = type;
    }

	public String getMeteringUnit() {
        return meteringUnit;
    }

	public void setMeteringUnit(String meteringUnit) {
        this.meteringUnit = meteringUnit;
    }
}
