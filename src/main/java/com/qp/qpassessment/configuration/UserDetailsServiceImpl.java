package com.qp.qpassessment.configuration;

import com.qp.qpassessment.entity.Users;
import com.qp.qpassessment.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl {

    private final UsersRepository usersRepository;

    @Autowired
    public UserDetailsServiceImpl(final UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {

        // Implementation for loadUsername method.
        return email -> {
            Users user = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

            return User
                    .builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .disabled(user.getIsDeleted() == 1)
                    .roles(user.getRole().name())
                    .build();
        };
    }
}
