package com.developerjang.til.application;

import com.developerjang.til.TilApplication;
import com.developerjang.til.entity.Company;
import com.developerjang.til.entity.CompanySupport;
import com.developerjang.til.entity.User;
import com.developerjang.til.infra.repository.CompanyRepository;
import com.developerjang.til.infra.repository.CompanySupportRepository;
import com.developerjang.til.infra.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ObjectUtils;

@Slf4j
@ActiveProfiles("local")
@SpringBootTest(classes = TilApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApplicationImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanySupportRepository companySupportRepository;

    @Test
    void 회사_생성_테스트_케이스() {
        Company company = Company.builder()
                .build();

        companyRepository.save(company);
    }

    @Test
    void 유저_생성_테스트_케이스() {
        User user = User.builder()
                .userId("wkdwnsghks")
                .password("111111")
                .build();

        userRepository.save(user);
    }

    @Test
    @Rollback(false)
    void 회사_유저_생성_테스트_케이스() {

        Company company = Company.builder()
                .build();

        Company createCompany = companyRepository.save(company);

        User user = User.builder()
                .userId("wkdwnsghks")
                .password("111111")
                .company(createCompany)
                .build();

        userRepository.save(user);
    }

    @Test
    @Rollback(false)
    void 회사_유저_지원이력_생성_테스트_케이스() {

        Company company = Company.builder()
                .build();

        Company createCompany = companyRepository.save(company);

        User user = User.builder()
                .userId("wkdwnsghks")
                .password("111111")
                .company(createCompany)
                .build();

        userRepository.save(user);

        CompanySupport companySupport = CompanySupport.builder()
                .user(user)
                .company(company)
                .build();
        companySupportRepository.save(companySupport);
    }

    @Test
    @Rollback(false)
    void 회사_삭제시_cascade로_user_삭제_확인_테스트_케이스() {
        User user = userRepository.findByUserId("wkdwnsghks").orElse(null);
        if (!ObjectUtils.isEmpty(user)) {
            Company company = user.getCompany();
            log.debug(String.format("company_id : %s", company.getId()));
            log.debug(String.format("user_id : %s", user.getId()));
            companyRepository.delete(company);
        }
    }
}