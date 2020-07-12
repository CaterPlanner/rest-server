package com.downfall.caterplanner.model.enumerate;

public enum  Scope {

    PUBLIC(0),
    PRIVATE(1),
    GROUP(2);

    private int value;

    Scope(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Scope findScope(int value){
        for(Scope scope : Scope.values()){
            if(scope.value == value)
                return scope;
        }
        return null;
    }
}
