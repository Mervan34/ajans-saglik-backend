package com.ajanssaglik.model;

public enum RequestType {
    BUY("Satın Alma"),
    SELL("Satış"),
    INFO("Bilgi Talebi");

    private final String displayName;

    RequestType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
