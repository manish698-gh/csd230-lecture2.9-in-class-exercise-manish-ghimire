package app.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
@DiscriminatorValue("TICKET")
public class TicketEntity extends ProductEntity {
    private String description;
    private Double price; // Changed to Double


    public TicketEntity() {}

    public TicketEntity(String description, Double price) {
        this.description = description;
        this.price = price;
    }

    // Getters and Setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    @Override
    public void sellItem() {
        System.out.println("Selling Ticket: " + description);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TicketEntity that = (TicketEntity) o;
        return Objects.equals(description, that.description) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description, price);
    }

    @Override
    public String toString() {
        return "TicketEntity{" +
                "description='" + description + '\'' +
                ", price=" + price +
                "} " + super.toString();
    }
}