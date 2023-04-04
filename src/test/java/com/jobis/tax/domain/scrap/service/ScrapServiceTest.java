package com.jobis.tax.domain.scrap.service;

import com.jobis.tax.domain.scrap.external.SzsApiClient;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ScrapServiceTest {

    //@Autowired
    private SzsApiClient szsApiClient;

    //@Test
    public void szsTest(){
        Map<String, String> map = new HashMap<>();
        map.put("name","홍길동");
        map.put("regNo","860824-1655068");
        var results = szsApiClient.scrap(map);
        System.out.println(results.toString());
    }

}