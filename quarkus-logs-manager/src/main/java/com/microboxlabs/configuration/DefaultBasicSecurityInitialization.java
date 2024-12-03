package com.microboxlabs.configuration;

import com.microboxlabs.service.datasource.domain.User;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.microboxlabs.service.contract.SecurityBinder.SECURITY_BINDER;

@Singleton
public class DefaultBasicSecurityInitialization {
private final Logger logger = LoggerFactory.getLogger(DefaultBasicSecurityInitialization.class);
    @Transactional
    public void loadUsers(@Observes StartupEvent evt, @ConfigProperty(name = "application.security.default.admin.password") String defaultAdminPassword, @ConfigProperty(name = "application.security.default.user.password") String defaultUserPassword) {
        User.deleteAll();
        logger.info("Loading users password {} {}", defaultAdminPassword, defaultUserPassword);
        final var users = List.of(
                SECURITY_BINDER.bind("admin", "admin", defaultAdminPassword),
                SECURITY_BINDER.bind("user", "user", defaultUserPassword)
        );
        User.persist(users);
    }
}
