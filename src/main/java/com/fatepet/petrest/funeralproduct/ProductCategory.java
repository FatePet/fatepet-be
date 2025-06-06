package com.fatepet.petrest.funeralproduct;

public enum ProductCategory {
    BASIC("기본항목"),
    OPTIONAL("선택항목"),
    PACKAGE("패키지");

    private final String displayName;

    ProductCategory(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ProductCategory fromDisplayName(String displayName) {
        for (ProductCategory category : ProductCategory.values()) {
            if (category.getDisplayName().equals(displayName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("serviceType 형식 오류 " + displayName);
    }
}
