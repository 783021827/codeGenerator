package com.myGenerator.aest;

public enum PageSize {
    SIZE10(15),SIZE20(20),SIZE30(30), SIZE40(40), SIZE50(50);
    Integer size;

    private PageSize(int size) {
        this.size = size;
    }
    public Integer getSize(){
        return this.size;
    }
}
