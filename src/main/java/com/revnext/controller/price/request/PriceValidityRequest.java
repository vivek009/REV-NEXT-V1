package com.revnext.controller.price.request;



import java.util.Date;
import lombok.Data;

@Data
public class PriceValidityRequest {
    private String location;
    private String currency;
    private String country;
    private Date validFrom;
    private Date validTill;
}

