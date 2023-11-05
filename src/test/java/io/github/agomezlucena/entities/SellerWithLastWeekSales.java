package io.github.agomezlucena.entities;

import java.util.UUID;

public class SellerWithLastWeekSales {
    private final UUID sellerId;
    private final String name;
    private final String location;
    private final Double amountInEuros;

    public SellerWithLastWeekSales(UUID sellerId, String name, String location, Double amountInEuros) {
        this.sellerId = sellerId;
        this.name = name;
        this.location = location;
        this.amountInEuros = amountInEuros;
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

    public Double getAmountInEuros() {
        return amountInEuros;
    }
}
