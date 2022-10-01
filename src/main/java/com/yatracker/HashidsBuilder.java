package com.yatracker;

/**
 * @author ya
 */
public class HashidsBuilder {

    private String salt;

    private int minLength;

    private String alphabet;

    private String seps;

    public HashidsBuilder() {
        this.salt = HashidsUnicodeTable.EMPTY_STRING;
        this.minLength = 0;
        this.alphabet = HashidsUnicodeTable.DEFAULT_ALPHABET;
        this.seps = HashidsUnicodeTable.DEFAULT_SEPS;
    }

    public HashidsBuilder salt(String salt) {
        this.salt = salt;
        return this;
    }

    public HashidsBuilder minLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public HashidsBuilder alphabet(String alphabet) {
        this.alphabet = alphabet;
        return this;
    }

    public HashidsBuilder seps(String seps) {
        this.seps = seps;
        return this;
    }

    public Hashids build() {
        HashidsUnicodeTable table = new HashidsUnicodeTable(this.salt, this.minLength, this.alphabet, this.seps);
        return new Hashids(table);
    }
}
