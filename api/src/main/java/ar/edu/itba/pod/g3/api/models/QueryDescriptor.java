package ar.edu.itba.pod.g3.api.models;

import ar.edu.itba.pod.g3.api.enums.QueryType;

import java.io.Serializable;

public class QueryDescriptor implements Serializable {
    final private String id;
    final private QueryType type;

    public QueryDescriptor(String id, QueryType type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public QueryType getType() {
        return type;
    }
}