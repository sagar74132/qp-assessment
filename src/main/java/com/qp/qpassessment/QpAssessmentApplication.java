package com.qp.qpassessment;

import com.qp.qpassessment.constant.Enums;
import com.qp.qpassessment.entity.Users;
import com.qp.qpassessment.repository.UsersRepository;
import com.qp.qpassessment.utils.PasswordEncoderUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QpAssessmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(QpAssessmentApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(UsersRepository userRepository) {
        return args -> {
            // Check if users exist
            if (userRepository.count() == 0) {
                // Create admin user
                Users adminUser = new Users();
                adminUser.setName("Admin User");
                adminUser.setEmail("admin@qp.com");
                adminUser.setPassword(PasswordEncoderUtils.encode("1234"));
                adminUser.setRole(Enums.Role.ADMIN);
                userRepository.save(adminUser);

                // Create regular user
                Users regularUser = new Users();
                regularUser.setName("Customer");
                regularUser.setEmail("user@qp.com");
                regularUser.setPassword(PasswordEncoderUtils.encode("1234"));
                regularUser.setRole(Enums.Role.USER);
                userRepository.save(regularUser);
            }
        };
    }

}
