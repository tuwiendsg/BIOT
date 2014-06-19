package galaxy.billing;

import java.math.BigDecimal;
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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "bill_line")
@Configurable
public class BillLine {

	@ManyToOne
    @JoinColumn(name = "Gbots_id", referencedColumnName = "id")
    private Gbot gbotsId;

	@ManyToOne
    @JoinColumn(name = "Bill_id", referencedColumnName = "id")
    private Bill billId;

	@Column(name = "amount", precision = 22, scale = 2)
    @NotNull
    private BigDecimal amount;

	@Column(name = "quantity")
    private int quantity;

	public Gbot getGbotsId() {
        return gbotsId;
    }

	public void setGbotsId(Gbot gbotsId) {
        this.gbotsId = gbotsId;
    }

	public Bill getBillId() {
        return billId;
    }

	public void setBillId(Bill billId) {
        this.billId = billId;
    }

	public BigDecimal getAmount() {
        return amount;
    }

	public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

	public int getQuantity() {
        return quantity;
    }

	public void setQuantity(int quantity) {
        this.quantity = quantity;
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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new BillLine().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countBillLines() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BillLine o", Long.class).getSingleResult();
    }

	public static List<BillLine> findAllBillLines() {
        return entityManager().createQuery("SELECT o FROM BillLine o", BillLine.class).getResultList();
    }

	public static BillLine findBillLine(Integer id) {
        if (id == null) return null;
        return entityManager().find(BillLine.class, id);
    }

	public static List<BillLine> findBillLineEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BillLine o", BillLine.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            BillLine attached = BillLine.findBillLine(this.id);
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
    public BillLine merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BillLine merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
