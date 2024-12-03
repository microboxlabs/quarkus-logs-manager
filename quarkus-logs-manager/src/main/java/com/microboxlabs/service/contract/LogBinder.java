package com.microboxlabs.service.contract;

import com.microboxlabs.service.contract.to.LogTO;
import com.microboxlabs.service.contract.to.PaginatedTO;
import com.microboxlabs.service.contract.to.criteria.AdvanceCriteriaTO;
import com.microboxlabs.service.datasource.criteria.LogCriteria;
import com.microboxlabs.service.datasource.domain.Log;
import com.microboxlabs.util.DateUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.regex.Matcher;

@Mapper(imports = {
        DateUtil.class,
        LocalDateTime.class
})
public interface LogBinder {
    LogBinder LOG_BINDER = Mappers.getMapper(LogBinder.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "timestamp", expression = "java(DateUtil.parseLogDateTime.apply(source.group(1)))")
    @Mapping(target = "logLevel", expression = "java(source.group(2))")
    @Mapping(target = "serviceName", expression = "java(source.group(3))")
    @Mapping(target = "message", expression = "java(source.group(4))")
    Log bindLog(Matcher source);

    @Mapping(target = "timestamp", expression = "java(DateUtil.convertAWSEventTOString.apply(source.getTimestamp()))")
    LogTO bind(Log source);

    default LogTO bind(PanacheEntityBase source) {
        var entity = (Log) source;
        return bind(entity);
    }

    default PaginatedTO<LogTO> bindPaginated(PanacheQuery<PanacheEntityBase> source) {
        var listSource = source.
                stream()
                .map(this::bind)
                .toList();
        return new PaginatedTO<>(listSource, source.page().index, source.page().size, source.count());
    }

    LogCriteria bind(AdvanceCriteriaTO source);
}
