package com.dawnfall.caterplanner.common.entity.enumerate;

import lombok.Getter;

@Getter
public enum Stat {

    PROCEED(0),
    WAIT(1),
    SUCCESS(2),
    FAIL(3);


    private int value;

    Stat(int value) {this.value = value;}

    public static Stat findStat(int value){
        for(Stat stat : Stat.values()){
            if(stat.value == value)
                return stat;
        }
        return null;
    }
}
