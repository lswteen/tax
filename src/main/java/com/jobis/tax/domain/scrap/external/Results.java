package com.jobis.tax.domain.scrap.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Results {
    private String status;
    private Object data;
    private Object errors;
}
