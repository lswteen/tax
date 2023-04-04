package com.jobis.tax.domain.scrap.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import static com.jobis.tax.core.config.SZS_Global_Constants.DEFAULT_SZS;

@FeignClient(
        name="szsApiClient",
        url="${openApi.szs.baseUri}",
        contextId = DEFAULT_SZS
)
interface SzsApiClient {
    @PostMapping(path="")
    Results scrap();
}
