package com.ajanssaglik.model;

public enum ProductCategory {
    HASTANE("Hastane"),
    HASTANE_KADROSU("Hastane Kadrosu"),
    DEVIRLI_POLIKLINIK("Devirli Poliklinik"),
    TIP_MERKEZI_RUHSATI("Tıp Merkezi Ruhsatı"),
    COCUK_HASTALIKLARI("Çocuk Hastalıkları"),
    IC_HASTALIKLARI("İç Hastalıkları"),
    GENEL_CERRAHI("Genel Cerrahi"),
    KARDIYOLOJI("Kardiyoloji"),
    DIGER("Diğer");

    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
