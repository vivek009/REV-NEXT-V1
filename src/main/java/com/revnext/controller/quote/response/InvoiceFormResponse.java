package com.revnext.controller.quote.response;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceFormResponse {
    private String bookNo;
    private String invoiceNo;
    private String transferredTo;
    private String date;
    private String through;
    private String by;
    private String orderNo;
    private List<Item> items;
    private String issuedBy;
    private String mbNo;
    private String pageNo;

    // Getters and Setters

    @Data
    public static class Item {
        private String name;
        private String unit;
        private String quantity;
        private String headOfAccount;
        private double amount;

        // Getters and Setters
    }
}
