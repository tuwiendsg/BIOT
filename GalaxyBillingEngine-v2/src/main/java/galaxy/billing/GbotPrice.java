package galaxy.billing;

import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "gbot_price")
@Configurable
public class GbotPrice {

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new GbotPrice().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countGbotPrices() {
        return entityManager().createQuery("SELECT COUNT(o) FROM GbotPrice o", Long.class).getSingleResult();
    }

	public static List<GbotPrice> findAllGbotPrices() {
        return entityManager().createQuery("SELECT o FROM GbotPrice o", GbotPrice.class).getResultList();
    }

	public static GbotPrice findGbotPrice(Integer id) {
        if (id == null) return null;
        return entityManager().find(GbotPrice.class, id);
    }

	public static List<GbotPrice> findGbotPriceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM GbotPrice o", GbotPrice.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            GbotPrice attached = GbotPrice.findGbotPrice(this.id);
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
    public GbotPrice merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        GbotPrice merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@OneToMany(mappedBy = "gbotPriceId")
    private Set<Gbot> gbots;

	

	@Column(name = "price")
    @NotNull
    private Double price;
	
	@Column(name = "currency", length = 10)
    private String currency;

	@Column(name = "PriceList", length = 45)
    private String priceList;

	public Set<Gbot> getGbots() {
        return gbots;
    }

	public void setGbots(Set<Gbot> gbots) {
        this.gbots = gbots;
    }

	public String getCurrency() {
        return currency;
    }

	public void setCurrency(String currency) {
        this.currency = currency;
    }

	public Double getPrice() {
        return price;
    }

	public void setPrice(Double price) {
        this.price = price;
    }

	public String getPriceList() {
        return priceList;
    }

	public void setPriceList(String priceList) {
        this.priceList = priceList;
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
}
