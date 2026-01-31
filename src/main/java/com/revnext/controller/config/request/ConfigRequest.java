package com.revnext.controller.config.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfigRequest {
    private String configurationName;
    private LocalDate validFrom;
    private LocalDate validTill;
    private String value;
}
