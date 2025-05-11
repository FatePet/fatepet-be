package com.fatepet.petrest.consultrequest;

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
}
