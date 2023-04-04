package com.jobis.tax.domain.scrap.service;

import com.jobis.tax.domain.scrap.external.Data;
import com.jobis.tax.domain.scrap.external.HttpClient;
import com.jobis.tax.domain.scrap.external.Results;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final HttpClient httpClient;

    public Results userInfoScrap(){
        //스크랩 post call
        var jsonString = "";
        JSONObject jsonObject = null;
        var jsonParser = new JSONParser();
        try {
            //홍길동 |  860824-1655068
            //김둘리 |  921108-1582816
            //마징가 |  880601-2455116
            //베지터 |  910411-1656116
            //손오공 |  820326-2715702

            //jsonString = httpClient.sendPost("홍길동","860824-1655068");
            //jsonString = httpClient.sendPost("김둘리","921108-1582816");
            //jsonString = httpClient.sendPost("마징가","880601-2455116");
            //jsonString = httpClient.sendPost("베지터","910411-1656116");
            jsonString = httpClient.sendPost("손오공","820326-2715702");

            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        //기존에 있던 데이터는 삭제 보다는 캐쉬하는방법도 고려
        //전달받은 Response 데이터 가공
        var isStatus = jsonObject.get("status").equals("success") ? true : false;
        if(isStatus == false){
            System.out.println("상태값 실패로 종료합니다.");
            return null;
        }
        var data= (JSONObject) jsonObject.get("data");
        //================Data
        var mapperData = Data.builder()
                .jsonList((JSONObject) data.get("jsonList"))
                .appVer((String) data.get("appVer"))
                .errMsg((String) data.get("errMsg"))
                .company((String) data.get("company"))
                .svcCd((String) data.get("svcCd"))
                .hostNm((String) data.get("hostNm"))
                .workerReqDt((String) data.get("workerResDt"))
                .workerReqDt((String) data.get("workerReqDt"))
                .build();
        System.out.println("======> data : "+mapperData.toString());
        //================json List
        var jsonList = mapperData.getJsonList();
        var salarys = (List<Map<String, String>>) jsonList.get("급여");
        var sancheulSeAmount = (String) jsonList.get("산출세액");            //산출세액에 소수점이 있어서 이거 삭제필요
        var incomeInfos = (List<Map<String,String>>) jsonList.get("소득공제");

        System.out.println("=======> salarys : "+salarys.toString());
        System.out.println("=========>"+sancheulSeAmount);
        System.out.println("=======> incomeInfos : "+incomeInfos.toString());

        //================error
        var error = (String) jsonObject.get("error");
        System.out.println("============> error : "+error);

        //계산식으로 계산 ...


        //db저장 호출

        //비동기로 무조건 OK
        return null;

    }

}
