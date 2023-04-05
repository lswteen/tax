package com.jobis.tax.domain.scrap.service;

import com.jobis.tax.domain.scrap.external.Data;
import com.jobis.tax.domain.scrap.external.HttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class ScrapServiceTest {

    @Autowired
    private HttpClient httpClient;

    @Test
    public void szsTest(){
    //스크랩 post call
        String jsonString = "";
        JSONObject jsonObject = null;
        JSONParser jsonParser = new JSONParser();
        try {
            //홍길동 |  860824-1655068
            //김둘리 |  921108-1582816
            //마징가 |  880601-2455116
            //베지터 |  910411-1656116
            //손오공 |  820326-2715702

            //jsonString = httpClient.sendPost("홍길동","860824-1655068");
            //jsonString = httpClient.sendPost("김둘리","921108-1582816");
            //jsonString = httpClient.sendPost("마징가","880601-2455116");
            jsonString = httpClient.sendPost("베지터","910411-1656116");
            //jsonString = httpClient.sendPost("손오공","820326-2715702");

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
//        System.out.println("======> data : "+mapperData.toString());
        //================json List
        JSONObject jsonList = mapperData.getJsonList();
        List<Map<String,String>> salarys = (List<Map<String, String>>) jsonList.get("급여");
        String sancheulSeAmount = (String) jsonList.get("산출세액");            //산출세액에 소수점이 있어서 이거 삭제필요
        List<Map<String,String>> incomeInfos = (List<Map<String,String>>) jsonList.get("소득공제");
//        System.out.println("=======> salarys : "+salarys.toString());
//        System.out.println("=========>"+sancheulSeAmount);
//        System.out.println("=======> incomeInfos : "+incomeInfos.toString());
//================error
//        String error = (String) jsonObject.get("error");
//        System.out.println("============> error : "+error);

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
    }

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

    @Test
    void 결정세액(){
        double calculatedTax = 3000000;     //jsonList > 산출세액
        double insurancePremium = 100000;   //소득공제 > 보험료
        double educationExpense = 200000;   //소득공제 > 교육비
        double donation = 150000;           //소득공제 > 기부금
        double medicalExpense = 4400000;    //소득공제 > 의료비
        double retirementPension = 6000000; //소득공제 > 퇴직연금
        double totalSalary = 60000000;      //급여 > 총지급액

        double finalTaxAmount = calculateFinalTaxAmount(calculatedTax, insurancePremium, educationExpense, donation, medicalExpense, retirementPension, totalSalary);
        System.out.println("Final Tax Amount: " + new DecimalFormat("#,###").format(finalTaxAmount));

    }

}