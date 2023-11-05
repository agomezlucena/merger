package io.github.agomezlucena.entities;

import java.util.UUID;

public class SellerLastWeekSales {
    private final UUID sellerId;
    private final Double amountInEuros;

    public SellerLastWeekSales(UUID sellerId, Double amountInEuros) {
        this.sellerId = sellerId;
        this.amountInEuros = amountInEuros;
    }

    public UUID getSellerId() {
        return sellerId;
    }

    public Double getAmountInEuros() {
        return amountInEuros;
    }
}
