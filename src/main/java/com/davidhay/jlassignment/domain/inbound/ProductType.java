package com.davidhay.jlassignment.domain.inbound;

public enum ProductType {
    DRESSES("dresses");

    private final String type;

    ProductType(@SuppressWarnings("SameParameterValue") String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
