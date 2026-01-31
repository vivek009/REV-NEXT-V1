package com.revnext.controller.quote.response;

import lombok.Data;

import java.util.List;

@Data
public class GatePassFormResponse {
    private String slNo;
    private String bookNo;
    private String vehicleNo;
    private String date;
    private List<Item> items;
    private String issuedBy;

    // Getters and Setters

    @Data
    public static class Item {
        private String name;
        private String quantity;
        private String indentNo;
        private String toWhomIssued;
        private String through;

        // Getters and Setters
    }
}
