package com.microboxlabs.service.datasource.criteria;

import com.microboxlabs.service.datasource.domain.Log;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.tuples.Tuple2;

import java.util.Map;
import java.util.Objects;

public class LogCriteria extends AdvanceCriteria<PanacheEntityBase> {
    private String predicates = "from Log l where 1=1";
    private final Sort timestamp = Sort.by("timestamp").descending();

    @Override
    public PanacheQuery<PanacheEntityBase> query() {
        Tuple2<String, Parameters> objects = prepareQuery(super.getFields());
        var projection = ((Objects.isNull(super.getFields()) || super.getFields().isEmpty()) && (Objects.nonNull(super.getSearch()) || !super.getSearch().isEmpty()))
                ? this.searchByDefaultText()
                : this.findFromFields();
        return projection.page(super.getPage(), super.getSize());
    }

    private PanacheQuery<PanacheEntityBase> findFromFields() {
        Tuple2<String, Parameters> objects = prepareQuery(super.getFields());
        return Log.find(objects.getItem1(), timestamp, objects.getItem2());
    }

    private PanacheQuery<PanacheEntityBase> searchByDefaultText() {
        predicates += " or l.logLevel = :logLevel or l.serviceName = :serviceName";
        return Log.find(predicates, timestamp, Map.of("logLevel", super.getSearch(), "serviceName", super.getSearch()));
    }

    private Tuple2<String, Parameters> prepareQuery(Map<String, Object> fields) {
        var parameters = new Parameters();
        final var params = fields
                .entrySet()
                .stream()
                .map(entry -> parameters.and(entry.getKey(), entry.getValue()))
                .reduce((p1, p2) -> p2)
                .orElse(Parameters.with("serviceName", ""));
        final var predicates = createPredicates(fields);
        return Tuple2.of(predicates, params);
    }


    private String createPredicates(Map<String, Object> fields) {
        if (fields.containsKey("logLevel"))
            predicates += " and l.logLevel = :logLevel";
        if (fields.containsKey("serviceName"))
            predicates += " and l.serviceName = :serviceName";
        if (fields.containsKey("startDate"))
            predicates += " and l.timestamp >= :startDate";
        if (fields.containsKey("endDate"))
            predicates += " and l.timestamp <= :endDate";
        return predicates;
    }
}
