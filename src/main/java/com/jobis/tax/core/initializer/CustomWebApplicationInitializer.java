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
public class CustomWebApplicationInitializer {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            private UserRepository userRepository;

            @Override
            public void run(ApplicationArguments args){
                User user = User.signUpBuilder()
                        .email("user@gmail.com")
                        .password(passwordEncoder.encode("abcde가A1!"))
                        .name("이순우")
                        .regNo("8012261266767")
                        .phoneNumber("01012345678")
                        .nickname("renzo")
                        .build();

                userRepository.save(user);
            }
        };
    }
}
