package com.jobis.tax.domain.scrap.repository;


import com.jobis.tax.domain.scrap.entity.TaxInformation;
import com.jobis.tax.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRepository extends JpaRepository<TaxInformation, Long> {
    void deleteById(Long id);
    //void deleteByUser(User user);
}
