package com.qp.qpassessment.utils;

import com.qp.qpassessment.constant.Enums;
import com.qp.qpassessment.model.IdentityContext;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class Util {

    public static IdentityContext getIdentity() {
        Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());

        String email = null;
        Enums.Role role = null;

        if (auth.isPresent()) {
            Optional<?> roleFromContext = auth.get().getAuthorities().stream().findFirst();
            Optional<String> emailFromContext = Optional.ofNullable(auth.get().getName());

            if (roleFromContext.isPresent() && emailFromContext.isPresent()) {
                role = Enums.Role.valueOf(roleFromContext.get().toString().substring(5));
                email = emailFromContext.get();
            }
        }

        return IdentityContext.builder()
                .email(email)
                .role(role)
                .build();
    }
}
