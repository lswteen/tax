package com.jobis.tax.domain.scrap.service;

import com.jobis.tax.domain.scrap.entity.TaxInformation;
import com.jobis.tax.domain.scrap.external.HttpClient;
import com.jobis.tax.domain.scrap.repository.TaxRepository;
import com.jobis.tax.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.text.DecimalFormat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ScrapServiceTest {

    @Autowired
    private ScrapService scrapService;

    @MockBean
    private HttpClient httpClient;

    @MockBean
    private TaxRepository taxRepository;

    private User user;
    private String responseBody;
    @BeforeEach
    public void setup() {
        user = new User();
        user.setName("김둘리");
        user.setRegNo("9211081582816");
        user.setUserId("kim@gmail.com");
        ;

        responseBody = "{\"status\":\"success\",\"data\":{\"jsonList\":{\"급여\":[{\"소득내역\":\"급여\",\"총지급액\":\"60,000,000\",\"업무시작일\":\"2020.10.02\",\"기업명\":\"(주)활빈당\",\"이름\":\"홍길동\",\"지급일\":\"2020.11.02\",\"업무종료일\":\"2021.11.02\",\"주민등록번호\":\"860824-1655068\",\"소득구분\":\"근로소득(연간)\",\"사업자등록번호\":\"012-34-56789\"}],\"산출세액\":\"3,000,000\",\"소득공제\":[{\"금액\":\"100,000\",\"소득구분\":\"보험료\"},{\"금액\":\"200,000\",\"소득구분\":\"교육비\"},{\"금액\":\"150,000\",\"소득구분\":\"기부금\"},{\"금액\":\"4,400,000\",\"소득구분\":\"의료비\"},{\"총납임금액\":\"6,000,000\",\"소득구분\":\"퇴직연금\"}]},\"appVer\":\"2021112501\",\"errMsg\":\"\",\"company\":\"삼쩜삼\",\"svcCd\":\"test01\",\"hostNm\":\"jobis-codetest\",\"workerResDt\":\"2022-08-16T06:27:35.160789\",\"workerReqDt\":\"2022-08-16T06:27:35.160851\"},\"errors\":{}}";
    }

    @Test
    public void testUserInfoScrap() throws Exception {

        when(httpClient.sendPost(user.getName(), ScrapService.formatString(user.getRegNo()))).thenReturn(responseBody);
        when(taxRepository.save(any(TaxInformation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaxInformation taxInformation = scrapService.userInfoScrap(user);

        // Add assertions for the expected values
        assertEquals(3000000.0, taxInformation.getCalculatedTax());
        assertEquals(100000.0, taxInformation.getInsurancePremium());
        // ... (add more assertions for other fields)

        verify(httpClient, times(1)).sendPost(user.getName(), ScrapService.formatString(user.getRegNo()));
        verify(taxRepository, times(1)).save(any(TaxInformation.class));
    }




}