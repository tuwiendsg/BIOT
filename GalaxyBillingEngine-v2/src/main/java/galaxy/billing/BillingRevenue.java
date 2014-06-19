package galaxy.billing;

import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name = "billing_revenue")
public class BillingRevenue {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new BillingRevenue().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countBillingRevenues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BillingRevenue o", Long.class).getSingleResult();
    }

	public static List<BillingRevenue> findAllBillingRevenues() {
        return entityManager().createQuery("SELECT o FROM BillingRevenue o", BillingRevenue.class).getResultList();
    }

	public static BillingRevenue findBillingRevenue(Integer id) {
        if (id == null) return null;
        return entityManager().find(BillingRevenue.class, id);
    }

	public static List<BillingRevenue> findBillingRevenueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BillingRevenue o", BillingRevenue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            BillingRevenue attached = BillingRevenue.findBillingRevenue(this.id);
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
    public BillingRevenue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BillingRevenue merged = this.entityManager.merge(this);
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

	@ManyToOne
    @JoinColumn(name = "Bill_id", referencedColumnName = "id")
    private Bill billId;

	@Column(name = "billing_date")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date billingDate;

	@Column(name = "BillingAmount" , precision = 22, scale = 2)
    @NotNull
    private BigDecimal billingAmount;

	@Column(name = "RevenueAmount" , precision = 22, scale = 2)
    private BigDecimal revenueAmount;

	public Bill getBillId() {
        return billId;
    }

	public void setBillId(Bill billId) {
        this.billId = billId;
    }

	public Date getBillingDate() {
        return billingDate;
    }

	public void setBillingDate(Date billingDate) {
        this.billingDate = billingDate;
    }

	public BigDecimal getBillingAmount() {
        return billingAmount;
    }

	public void setBillingAmount(BigDecimal billingAmount) {
        this.billingAmount = billingAmount;
    }

	public BigDecimal getRevenueAmount() {
        return revenueAmount;
    }

	public void setRevenueAmount(BigDecimal revenueAmount) {
        this.revenueAmount = revenueAmount;
    }
}
