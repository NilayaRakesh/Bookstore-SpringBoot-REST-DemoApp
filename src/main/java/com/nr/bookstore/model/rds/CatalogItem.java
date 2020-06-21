package com.nr.bookstore.model.rds;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "catalogItem")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "catalogItemType")
public class CatalogItem {

    @Id
    @Column(name = "catalogItemId")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="catalog_item_id_seq")
    @SequenceGenerator(name="catalog_item_id_seq", sequenceName="catalog_item_id_seq", allocationSize=1)
    private Long catalogItemId;


    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "createdAt")
    private Date createdAt;


    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updatedAt")
    private Date updatedAt;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "catalogItem")
    private List<Sku> skus;


    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}
