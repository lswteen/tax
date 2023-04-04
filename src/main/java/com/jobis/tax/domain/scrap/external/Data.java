package com.jobis.tax.domain.scrap.external;

import lombok.*;
import org.json.simple.JSONObject;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    private JSONObject jsonList;
    private String appVer;
    private String errMsg;
    private String company;
    private String svcCd;
    private String hostNm;
    private String workerResDt;
    private String workerReqDt;
}
