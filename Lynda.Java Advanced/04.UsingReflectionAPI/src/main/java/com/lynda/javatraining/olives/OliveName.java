package com.lynda.javatraining.olives;

public enum OliveName {

    KALAMATA("Kalamata"), LIGURIO("Ligurio"),
    PICHOLINE("Picholine"), GOLDEN("Golden");

    private final String nameAsString;

    private OliveName(String nameAsString) {
        this.nameAsString = nameAsString;
    }

    @Override
    public String toString() {
        return this.nameAsString;
    }
}
