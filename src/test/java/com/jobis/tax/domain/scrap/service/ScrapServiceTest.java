package com.jobis.tax.domain.scrap.service;

import com.jobis.tax.domain.scrap.external.HttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DecimalFormat;

@SpringBootTest
class ScrapServiceTest {

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private RefundCalculation refundCalculation;

    @Test
    public void szsTest(){
            //jsonString = httpClient.sendPost("홍길동","860824-1655068");
            //jsonString = httpClient.sendPost("김둘리","921108-1582816");
            //jsonString = httpClient.sendPost("마징가","880601-2455116");
            //jsonString = httpClient.sendPost("베지터","910411-1656116");
            //jsonString = httpClient.sendPost("손오공","820326-2715702");
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

        double finalTaxAmount = refundCalculation.calculateFinalTaxAmount(calculatedTax, insurancePremium, educationExpense,
                donation, medicalExpense, retirementPension, totalSalary);
        System.out.println("Final Tax Amount: " + new DecimalFormat("#,###").format(finalTaxAmount));

    }

}