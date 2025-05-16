package com.fatepet.petrest.funeralproduct;

public enum ProductCategory {
    BASIC("기본 사항"),
    OPTIONAL("선택 사항"),
    PACKAGE("패키지");

    private final String displayName;

    ProductCategory(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
