package com.microboxlabs.service.contract;

import com.microboxlabs.service.datasource.domain.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.function.UnaryOperator;

@Mapper
public interface SecurityBinder {
    SecurityBinder SECURITY_BINDER = Mappers.getMapper(SecurityBinder.class);

    User bind(String username, String role);

    default User bind(String username, String role, String password) {
        final UnaryOperator<String> hashPassword = BcryptUtil::bcryptHash;
        var target = bind(username, role);
        target.setPassword(hashPassword.apply(password));
        return target;
    }
}
