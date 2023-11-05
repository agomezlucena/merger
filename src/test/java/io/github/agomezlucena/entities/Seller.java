package io.github.agomezlucena.entities;

import java.util.UUID;

public class Seller {
    private final UUID sellerId;
    private final String name;
    private final String location;

    public Seller(UUID sellerId, String name, String location) {
        this.sellerId = sellerId;
        this.name = name;
        this.location = location;
    }

    public UUID getSellerId() {
        return sellerId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

}
