package com.microboxlabs.service.datasource.criteria;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

import java.util.Map;

public abstract class AdvanceCriteria<T> extends Criteria<T> {
    private Map<String, Object> fields;

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    public abstract PanacheQuery<T> query();
}
