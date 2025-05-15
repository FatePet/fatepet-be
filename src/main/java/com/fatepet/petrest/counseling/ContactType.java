package com.fatepet.petrest.counseling;

public enum ContactType {
    CALL("전화"),
    SMS("문자");

    private final String displayName;

    ContactType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }

    public static String getDisplayNameByKey(String key) {
        for (ContactType type : values()) {
            if (type.name().equalsIgnoreCase(key)) {
                return type.getDisplayName();
            }
        }
        throw new IllegalArgumentException("잘못된 연락 방식입니다: " + key);
    }
}
