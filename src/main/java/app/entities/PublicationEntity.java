package app.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
@DiscriminatorValue("PUBLICATION")
public abstract class PublicationEntity extends ProductEntity {
    private String title;
    private Double price;  // Changed to Double
    private Integer copies; // Changed to Integer

    public PublicationEntity() {
    }

    public PublicationEntity(String title, Double price, Integer copies) {
        this.title = title;
        this.price = price;
        this.copies = copies;
    }

    @Override
    public String toString() {
        return "PublicationEntity{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", copies=" + copies +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PublicationEntity that = (PublicationEntity) o;
        return Objects.equals(title, that.title) && Objects.equals(price, that.price) && Objects.equals(copies, that.copies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, price, copies);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

}