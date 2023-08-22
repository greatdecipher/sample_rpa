package com.albertsons.argus.woc.enums;

public enum StatusNameEnum {
    OK("OK"),
    BEFORE("Implemented Before the Approved Schedule"),
    AFTER("Implemented After the Approved Schedule"),
    BEFOREAFTER("Implemented Before and After the Approved Schedule");


    private String value;
    /**

     *

     * @param value

     */

    StatusNameEnum(String value){
        this.value = value;
    }




    public String getValue() {
        return value;
    }
}