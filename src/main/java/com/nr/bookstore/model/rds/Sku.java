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
@Table(name = "sku")
public class Sku {

    @Id
    @Column(name = "skuId")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sku_id_seq")
    @SequenceGenerator(name = "sku_id_seq", sequenceName = "sku_id_seq", allocationSize = 1)
    private Long skuId;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "createdAt")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updatedAt")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogItemId", nullable = false)
    private CatalogItem catalogItem;

    @Column(name = "price", nullable = false)
    @Min(0)
    private Double price;

    @Column(name = "quantity", nullable = false)
    @Min(0)
    private Integer quantity;

    public Sku(CatalogItem catalogItem, @Min(0) Double price, @Min(0) Integer quantity) {
        this.catalogItem = catalogItem;
        this.price = price;
        this.quantity = quantity;
    }
}
