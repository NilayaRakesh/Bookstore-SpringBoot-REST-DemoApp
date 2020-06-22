package com.nr.bookstore.model.rds;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "purchase")
public class Purchase {

    @Id
    @Column(name = "purchaseId")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_id_seq")
    @SequenceGenerator(name = "purchase_id_seq", sequenceName = "purchase_id_seq", allocationSize = 1)
    private Long purchaseId;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "createdAt")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updatedAt")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skuId", nullable = false)
    private Sku sku;

    @Column(name = "purchasePrice", nullable = false)
    @Min(0)
    private Double purchasePrice;

    @Column(name = "purchasedQuantity", nullable = false)
    @Min(0)
    private Integer purchasedQuantity;

    public Purchase(Sku sku, @Min(0) Double purchasePrice, @Min(0) Integer purchasedQuantity) {
        this.sku = sku;
        this.purchasePrice = purchasePrice;
        this.purchasedQuantity = purchasedQuantity;
    }
}
