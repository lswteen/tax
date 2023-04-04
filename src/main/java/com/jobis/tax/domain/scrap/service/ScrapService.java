package com.jobis.tax.domain.scrap.service;

import com.jobis.tax.domain.scrap.external.HttpClient;
import com.jobis.tax.domain.scrap.external.Results;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final HttpClient httpClient;

    public Results userInfoScrap(){
        //스크랩 post call
        String reponseBody = "";
        JSONObject jsonObject = null;

        try {
            reponseBody = httpClient.sendPost("홍길동","860824-1655068");

            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(reponseBody);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //기존에 있던 데이터는 삭제 보다는 캐쉬하는방법도 고려
        //전달받은 Response 데이터 가공
        //data 영역
        jsonObject.get("appVer");
        jsonObject.get("hostNm");
        jsonObject.get("workerResDt");
        jsonObject.get("workerReqDt");
        //data > jsonList 영역
        JSONObject jsonListObj = (JSONObject)jsonObject.get("jsonList");
        jsonListObj.get("errMsg");
        jsonListObj.get("company");
        jsonListObj.get("svcCd");
        //db저장 호출

        //비동기로 무조건 OK
        return null;

    }

}
