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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        Optional<?> roleFromContext = auth.getAuthorities().stream().findFirst();
        if (roleFromContext.isEmpty()) {
            return null;
        }

        Enums.Role role = Enums.Role.valueOf(roleFromContext.get().toString().substring(5));

        return IdentityContext.builder()
                .email(auth.getName())
                .role(role)
                .build();
    }
}
