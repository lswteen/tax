package com.jobis.tax.domain.scrap.service;

import com.jobis.tax.core.exception.ApiException;
import com.jobis.tax.core.type.ServiceErrorType;
import com.jobis.tax.domain.scrap.entity.TaxInformation;
import com.jobis.tax.domain.scrap.external.Data;
import com.jobis.tax.domain.scrap.external.HttpClient;
import com.jobis.tax.domain.scrap.repository.TaxRepository;
import com.jobis.tax.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final HttpClient httpClient;
    private final TaxRepository taxRepository;
    private final CaffeineCacheManager caffeineCacheManager;

    public static double stringToDouble(String numberWithCommasAndDecimal) {
        var splitNumber = numberWithCommasAndDecimal.split("\\.");
        var numberWithCommas = splitNumber[0];
        var cleanNumber = numberWithCommas.chars()
                .mapToObj(c -> (char) c)
                .filter(c -> c != ',')
                .map(String::valueOf)
                .collect(Collectors.joining());
        return Double.parseDouble(cleanNumber);
    }

    public static String formatString(String input) {
        return input.substring(0, 6) + "-" + input.substring(6);
    }

    public TaxInformation getTaxInfoFromCache(User user) {
        Cache cache = caffeineCacheManager.getCache("taxInformatioinCache");
        if (cache != null) {
            String key = user.getRegNo();
            Cache.ValueWrapper wrapper = cache.get(key);
            if (wrapper != null) {
                return (TaxInformation) wrapper.get();
            }
        }
        return null;
    }

    @Cacheable(value="taxInformatioinCache", key="#user.regNo")
    public TaxInformation userInfoScrap(User user){
        TaxInformation cachedTaxInfo = getTaxInfoFromCache(user);
        if (cachedTaxInfo != null) {
            return cachedTaxInfo;
        }

        String jsonString = "";
        JSONObject jsonObject = null;
        try {
            jsonString = httpClient.sendPost(user.getName(),formatString(user.getRegNo()));
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (IOException | ParseException e ) {
            throw new ApiException(ServiceErrorType.FAILED_STATUS);
        }

        var isStatus = jsonObject.get("status").equals("success") ? true : false;
        if(isStatus == false){
            throw new ApiException(ServiceErrorType.FAILED_STATUS);
        }
        JSONObject data= (JSONObject) jsonObject.get("data");

        Data mapperData = Data.builder()
                .jsonList((JSONObject) data.get("jsonList"))
                .appVer((String) data.get("appVer"))
                .errMsg((String) data.get("errMsg"))
                .company((String) data.get("company"))
                .svcCd((String) data.get("svcCd"))
                .hostNm((String) data.get("hostNm"))
                .workerReqDt((String) data.get("workerResDt"))
                .workerReqDt((String) data.get("workerReqDt"))
                .build();

        var jsonList = mapperData.getJsonList();
        var salarys = (List<Map<String, String>>) jsonList.get("급여");
        var incomeInfos = (List<Map<String,String>>) jsonList.get("소득공제");

        var insuranceAndDonationAmounts = incomeInfos.stream()
                .filter(incomeInfo -> "보험료".equals(incomeInfo.get("소득구분"))
                        || "교육비".equals(incomeInfo.get("소득구분"))
                        || "기부금".equals(incomeInfo.get("소득구분"))
                        || "의료비".equals(incomeInfo.get("소득구분"))
                        || "퇴직연금".equals(incomeInfo.get("소득구분")))
                .map(incomeInfo -> incomeInfo.get("금액") != null ? incomeInfo.get("금액") : incomeInfo.get("총납임금액"))
                .collect(Collectors.toList());

        var calculatedTax = (String) jsonList.get("산출세액");
        var insurancePremium = insuranceAndDonationAmounts.get(0);
        var educationExpense = insuranceAndDonationAmounts.get(1);
        var donation = insuranceAndDonationAmounts.get(2);
        var medicalExpense = insuranceAndDonationAmounts.get(3);
        var retirementPension = insuranceAndDonationAmounts.get(4);
        var totalSalary = salarys.stream().map(
                v->v.get("총지급액")
        ).collect(Collectors.joining(", "));

        var taxInformation = TaxInformation.builder()
                .user(user)
                .calculatedTax(stringToDouble(calculatedTax))
                .insurancePremium(stringToDouble(insurancePremium))
                .educationExpense(stringToDouble(educationExpense))
                .donation(stringToDouble(donation))
                .medicalExpense(stringToDouble(medicalExpense))
                .retirementPension(stringToDouble(retirementPension))
                .totalSalary(stringToDouble(totalSalary))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        if (cachedTaxInfo == null) {
            taxRepository.save(taxInformation);
        }

        return taxInformation;
    }

}
