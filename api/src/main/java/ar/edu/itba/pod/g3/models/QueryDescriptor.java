package ar.edu.itba.pod.g3.models;

import ar.edu.itba.pod.g3.enums.QueryType;

public class QueryDescriptor {
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