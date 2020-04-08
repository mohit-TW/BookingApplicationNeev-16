package com.booking.shows;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 30, message = "The show name must be {min} to {max} characters in length.")
    private String name;

    @Size(min = 5, max = 200, message = "The description must be {min} to {max} characters in length.")
    private String description;

    @DecimalMin(value = "1", message = "Price must at least be {value}")
    private double price;

    @Enumerated(EnumType.STRING)
    private ShowStatus status;

    public Show() {
    }

    public Show(String name, String description, double price, ShowStatus status) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public ShowStatus getStatus() {
        return status;
    }

    public void setStatus(ShowStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return Double.compare(show.price, price) == 0 &&
            Objects.equals(id, show.id) &&
            Objects.equals(name, show.name) &&
            Objects.equals(description, show.description) &&
            Objects.equals(status, status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price);
    }

    @Override
    public String toString() {
        return "Show{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", price=" + price +
            ", status=" + status +
            '}';
    }
}
