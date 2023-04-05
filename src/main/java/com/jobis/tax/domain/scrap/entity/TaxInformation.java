package com.jobis.tax.domain.scrap.entity;

import com.jobis.tax.domain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "tax_information")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaxInformation {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "calculated_tax")
    private Double calculatedTax;

    @Column(name = "insurance_premium")
    private Double insurancePremium;

    @Column(name = "education_expense")
    private Double educationExpense;

    @Column(name = "donation")
    private Double donation;

    @Column(name = "medical_expense")
    private Double medicalExpense;

    @Column(name = "retirement_pension")
    private Double retirementPension;

    @Column(name = "total_salary")
    private Double totalSalary;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
