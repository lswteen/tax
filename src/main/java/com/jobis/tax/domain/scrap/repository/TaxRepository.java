package com.jobis.tax.domain.scrap.repository;


import com.jobis.tax.domain.scrap.entity.TaxInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRepository extends JpaRepository<TaxInformation, Long> {
}
