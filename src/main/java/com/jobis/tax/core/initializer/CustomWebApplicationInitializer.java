package com.jobis.tax.core.initializer;

import com.jobis.tax.domain.user.entity.User;
import com.jobis.tax.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class CustomWebApplicationInitializer {
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            private UserRepository userRepository;

            @Override
            public void run(ApplicationArguments args){


                User hong = User.signUpBuilder()
                        .userId("hong@gmail.com")
                        .password(passwordEncoder.encode("abcde가A1!"))
                        .name("홍길동")
                        .regNo("8608241655068")
                        .phoneNumber("01012343378")
                        .nickname("hong")
                        .gender("MALE")
                        .build();

                userRepository.save(hong);

                User kim = User.signUpBuilder()
                        .userId("kim@gmail.com")
                        .password(passwordEncoder.encode("abcde가A1!"))
                        .name("김둘리")
                        .regNo("9211081582816")
                        .phoneNumber("01012344478")
                        .nickname("kim")
                        .gender("MALE")
                        .build();

                userRepository.save(kim);
            }
        };
    }
}
