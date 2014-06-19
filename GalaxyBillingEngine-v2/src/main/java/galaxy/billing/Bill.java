package galaxy.billing;

import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "bill")
@Configurable
public class Bill {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new Bill().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countBills() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Bill o", Long.class).getSingleResult();
    }

	public static List<Bill> findAllBills() {
        return entityManager().createQuery("SELECT o FROM Bill o", Bill.class).getResultList();
    }

	public static Bill findBill(Integer id) {
        if (id == null) return null;
        return entityManager().find(Bill.class, id);
    }

	public static List<Bill> findBillEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Bill o", Bill.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Bill attached = Bill.findBill(this.id);
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
    public Bill merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Bill merged = this.entityManager.merge(this);
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

	@OneToMany(mappedBy = "billId")
    private Set<BillLine> billLines;

	@OneToMany(mappedBy = "billId")
    private Set<BillingRevenue> billingRevenues;

	@Column(name = "create_datetime")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date createDatetime;

	@Column(name = "Customer", length = 100)
    private String customer;

	@Column(name = "status", length = 100)
    @NotNull
    private String status;

	@Column(name = "total", precision = 22, scale = 4)
    @NotNull
    private BigDecimal total;

	@Column(name = "currency", length = 10)
    @NotNull
    private String currency;

	@Column(name = "description", length = 1000)
    private String description;

	public Set<BillLine> getBillLines() {
        return billLines;
    }

	public void setBillLines(Set<BillLine> billLines) {
        this.billLines = billLines;
    }

	public Set<BillingRevenue> getBillingRevenues() {
        return billingRevenues;
    }

	public void setBillingRevenues(Set<BillingRevenue> billingRevenues) {
        this.billingRevenues = billingRevenues;
    }

	public Date getCreateDatetime() {
        return createDatetime;
    }

	public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

	public String getCustomer() {
        return customer;
    }

	public void setCustomer(String customer) {
        this.customer = customer;
    }

	public String getStatus() {
        return status;
    }

	public void setStatus(String status) {
        this.status = status;
    }

	public BigDecimal getTotal() {
        return total;
    }

	public void setTotal(BigDecimal total) {
        this.total = total;
    }

	public String getCurrency() {
        return currency;
    }

	public void setCurrency(String currency) {
        this.currency = currency;
    }

	public String getDescription() {
        return description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
