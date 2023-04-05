package com.jobis.tax.domain.scrap.service;

import com.jobis.tax.domain.scrap.entity.TaxInformation;
import com.jobis.tax.domain.scrap.external.Data;
import com.jobis.tax.domain.scrap.external.HttpClient;
import com.jobis.tax.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final HttpClient httpClient;

    /**
     * String 3,000,000.00 to double Type
     * @param numberWithCommasAndDecimal
     * @return
     */
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

    /**
     * 유저 정보 세액계산기
     *
     * 결정세액 = 산츨세액 - 근로소득세액공제금액 - 특별세액공제금액 - 표준세액공제금액 - 퇴직연금세액공제금액
     * 근로소득세액공제금액 = 산출세액 * 0.55
     *
     * 특별세액공제금액 = 보험료공제금액 + 의료비공제금액 + 교육비공제금액 + 기부금공제금액
         * 보험료공제금액 = 보험료 * 12%
         * 의료비공제금액 = (의료비 - 총급여 * 3%) * 15%
         * 단 의료비 공제금액 < 0일경우, 의료비 공제금액 = 0 처리한다.
         * 교육비공제금액 = 교육비 * 15%
         * 기부금공제금액 = 기부금 * 15%
     *
     * 표준세액공제금액 조건
         * 특별세액공제금액 합산금액 < 130000 이면 표준세액공제금액 = 130000
         * 특별세액공제금액 합산금액 >= 13000 이면 표준세액공제금액 = 0
         * 단 표준세액공제금액 = 130000 이면 특별세액공제금액 = 0 처리한다.
     *
     * 퇴직연금세액공제금액 = 퇴직연금 * 0.15
     *
     * 단 결정세액 < 0인경우 결정세액 0 처리 한다.
     *
     * @param calculatedTax
     * @param insurancePremium
     * @param educationExpense
     * @param donation
     * @param medicalExpense
     * @param retirementPension
     * @param totalSalary
     * @return
     */
    public static double calculateFinalTaxAmount(double calculatedTax, double insurancePremium, double educationExpense, double donation, double medicalExpense, double retirementPension, double totalSalary) {
        double laborIncomeTaxDeduction = calculatedTax * 0.55;
        double insuranceDeduction = insurancePremium * 0.12;
        double medicalExpenseDeduction = Math.max((medicalExpense - totalSalary * 0.03) * 0.15, 0);
        double educationExpenseDeduction = educationExpense * 0.15;
        double donationDeduction = donation * 0.15;
        double specialTaxDeduction = insuranceDeduction + medicalExpenseDeduction + educationExpenseDeduction + donationDeduction;
        double standardTaxDeduction = specialTaxDeduction < 130000 ? 130000 : 0;
        double retirementPensionTaxDeduction = retirementPension * 0.15;

        double finalTaxAmount = calculatedTax - laborIncomeTaxDeduction - specialTaxDeduction - standardTaxDeduction - retirementPensionTaxDeduction;
        finalTaxAmount = finalTaxAmount < 0 ? 0 : finalTaxAmount;
        return finalTaxAmount;
    }

    public TaxInformation userInfoScrap(User user){
        //스크랩 post call
        String jsonString = "";
        JSONObject jsonObject = null;
        JSONParser jsonParser = new JSONParser();
        try {
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
            return;
        }
        JSONObject data= (JSONObject) jsonObject.get("data");
        //================Data
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

        JSONObject jsonList = mapperData.getJsonList();
        List<Map<String,String>> salarys = (List<Map<String, String>>) jsonList.get("급여");
        String sancheulSeAmount = (String) jsonList.get("산출세액");            //산출세액에 소수점이 있어서 이거 삭제필요
        List<Map<String,String>> incomeInfos = (List<Map<String,String>>) jsonList.get("소득공제");


        List<String> insuranceAndDonationAmounts = incomeInfos.stream()
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

//        System.out.println("=================" + "\r\n" +
//                "calculatedTax: " + stringToDouble(calculatedTax) + "\r\n" +
//                "insurancePremium: " + stringToDouble(insurancePremium) + "\r\n" +
//                "educationExpense: " + stringToDouble(educationExpense) + "\r\n" +
//                "donation: " + stringToDouble(donation) + "\r\n" +
//                "medicalExpense : "+ stringToDouble(medicalExpense) + "\r\n" +
//                "retirementPension : "+ stringToDouble(retirementPension) + "\r\n" +
//                "totalSalary : " + stringToDouble(totalSalary) + "\r\n"
//        );

        double finalTaxAmount = calculateFinalTaxAmount(
                stringToDouble(calculatedTax), stringToDouble(insurancePremium),
                stringToDouble(educationExpense), stringToDouble(donation), stringToDouble(medicalExpense),
                stringToDouble(retirementPension), stringToDouble(totalSalary)
        );
        System.out.println("Final Tax Amount: " + new DecimalFormat("#,###").format(finalTaxAmount));
        double retirementPensionTaxDeduction = stringToDouble(retirementPension) * 0.15;
        System.out.println(String.format("retirementPensionTaxDeduction : " + new DecimalFormat("#,###").format(retirementPensionTaxDeduction) ));
        //db저장 호출
        return null;

    }

}
