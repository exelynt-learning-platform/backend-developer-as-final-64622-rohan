package com.rohan.booking.dto.resource;

import java.math.BigDecimal;

public class ResourceResponse {

    private Long id;
    private String name;
    private String description;
    private String location;
    private BigDecimal price;
    private boolean available;

    public ResourceResponse() {
    }

    public ResourceResponse(
            Long id,
            String name,
            String description,
            String location,
            BigDecimal price,
            boolean available) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.price = price;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }
}