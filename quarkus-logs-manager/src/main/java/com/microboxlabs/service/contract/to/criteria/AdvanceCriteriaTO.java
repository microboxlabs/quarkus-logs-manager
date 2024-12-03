package com.microboxlabs.service.contract.to.criteria;

import jakarta.json.bind.annotation.JsonbProperty;

import java.util.Map;

public class AdvanceCriteriaTO extends CriteriaTO {
    @JsonbProperty
    private Map<String, Object> fields;

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }
}
