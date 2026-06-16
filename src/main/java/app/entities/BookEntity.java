package app.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
@DiscriminatorValue("BOOK")
public class BookEntity extends PublicationEntity {
    private String author;

    public BookEntity() {}

    public BookEntity(String author, String title, Double price, Integer copies) { // Changed to Wrappers
        this.author = author;
        this.setTitle(title);
        this.setPrice(price);
        this.setCopies(copies);
    }

    // Getters and Setters
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    @Override
    public void sellItem() {
        System.out.println("Selling Book");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookEntity that = (BookEntity) o;
        return Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), author);
    }

    @Override
    public String toString() {
        return "BookEntity{" +
                "author='" + author + '\'' +
                "} " + super.toString();
    }
}