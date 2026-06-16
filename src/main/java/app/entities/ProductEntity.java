package app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore; // VITAL IMPORT
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ProductEntity implements Serializable, SaleableItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "products")
    @JsonIgnore // FIX: Prevents infinite recursion with CartEntity
    private Set<CartEntity> carts = new java.util.HashSet<>();

    @JsonIgnore // Optional: Hide UUID internal field from JSON output to keep it clean
    private String productId;

    public ProductEntity() {
        setProductId(UUID.randomUUID().toString());
    }

    // ... Keep Getters and Setters ...

    public Set<CartEntity> getCarts() {
        return carts;
    }

    public void setCarts(Set<CartEntity> carts) {
        this.carts = carts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId);
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                '}';
    }

    // Allows Thymeleaf/JSON to see simple class name
    public String getProductType() {
        return this.getClass().getSimpleName();
    }
}