package com.example.crm.sequence;

public enum SequenceType {

    INVOICE("INV"),
    ORDER("ORD");

    private final String prefix;

    SequenceType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDbName() {
        return name() + "_SEQ"; // INVOICE_SEQ, ORDER_SEQ
    }
}

