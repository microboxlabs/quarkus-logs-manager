package com.microboxlabs.service.datasource;

import com.microboxlabs.service.datasource.domain.Log;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogRepository implements PanacheRepository<Log> {
}
