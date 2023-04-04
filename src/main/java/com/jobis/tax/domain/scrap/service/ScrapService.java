package com.jobis.tax.domain.scrap.service;

import com.jobis.tax.domain.scrap.external.SzsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final SzsApiClient szsApiClient;

    public String userInfoScrap(Map<String,String> map){
        //스크랩 post call
        var results = szsApiClient.scrap(map);
        //전달받은 Response 데이터 가공

        //db저장 호출

        //비동기로 무조건 OK
        return results.toString();

    }

}
