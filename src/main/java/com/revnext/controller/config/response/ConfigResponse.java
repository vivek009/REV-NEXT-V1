package com.revnext.controller.config.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ConfigResponse {
    private String id;
    private String configurationName;
    private String configurationType;
    private LocalDate validFrom;
    private LocalDate validTill;
    private String value;
}
