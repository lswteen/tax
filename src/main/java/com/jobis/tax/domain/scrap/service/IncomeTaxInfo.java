package com.jobis.tax.domain.scrap.service;

import com.jobis.tax.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomeTaxInfo {
    private String finalTaxAmount;
    private String retirementPensionTaxDeduction;
    private User user;
}
