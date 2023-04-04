package com.jobis.tax.domain.scrap.external;

import lombok.*;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Results {
    private String status;
    private JSONObject errors;

}
