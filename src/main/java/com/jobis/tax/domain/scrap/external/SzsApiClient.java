package com.jobis.tax.domain.scrap.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

import static com.jobis.tax.core.config.SZS_Global_Constants.DEFAULT_SZS;

@FeignClient(
        name="szsApiClient",
        url="${openApi.szs.baseUri}",
        contextId = DEFAULT_SZS
)
public interface SzsApiClient {
    @PostMapping(path="${openApi.szs.path")
    Results scrap(@SpringQueryMap Map<String,String> param);
}
