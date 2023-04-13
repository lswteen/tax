package com.jobis.tax.domain.scrap.service;

import com.jobis.tax.domain.scrap.entity.TaxInformation;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class RefundCalculation {
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
     * jsonList > 산출세액
     * @param calculatedTax
     * 소득공제 > 보험료
     * @param insurancePremium
     * 소득공제 > 교육비
     * @param educationExpense
     * 소득공제 > 기부금
     * @param donation
     * 소득공제 > 의료비
     * @param medicalExpense
     * 소득공제 > 퇴직연금
     * @param retirementPension
     * 급여 > 총지급액
     * @param totalSalary
     *
     * @return
     */
    public static double calculateFinalTaxAmount(double calculatedTax, double insurancePremium,
                                                 double educationExpense, double donation, double medicalExpense,
                                                 double retirementPension, double totalSalary) {
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

    /**
     * 결정세액
     * @param taxInformation
     * @return
     */
    public static String finalTaxAmount(TaxInformation taxInformation){
        return new DecimalFormat("#,###").format(
                calculateFinalTaxAmount(
                    taxInformation.getCalculatedTax()
                    ,taxInformation.getInsurancePremium()
                    ,taxInformation.getEducationExpense()
                    ,taxInformation.getDonation()
                    ,taxInformation.getMedicalExpense()
                    ,taxInformation.getRetirementPension()
                    ,taxInformation.getTotalSalary())
        );
    }

    /**
     * 퇴직연금세액공제
     * @param taxInformation
     * @return
     */
    public static String retirementPensionTaxDeduction(TaxInformation taxInformation){
        return new DecimalFormat("#,###")
                .format(taxInformation.getRetirementPension() * 0.15);
    }

    public IncomeTaxInfo refundCalculation(TaxInformation taxInformation){
        return IncomeTaxInfo.builder()
                .finalTaxAmount(finalTaxAmount(taxInformation))
                .retirementPensionTaxDeduction(retirementPensionTaxDeduction(taxInformation))
                .build();
    }
}
