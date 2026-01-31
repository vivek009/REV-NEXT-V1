package com.revnext.controller.discount.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {
    private String name;
    private String rule;
    private Date startDate;
    private Date endDate;

    // Optional associations
    private UUID customerId;
    private UUID segmentId;
    private UUID productId;
    private UUID suiteId;
}
