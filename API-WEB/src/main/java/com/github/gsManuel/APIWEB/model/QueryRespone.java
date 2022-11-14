package com.github.gsManuel.APIWEB.model;

public class QueryRespone {
    private String query;
    private String clusterName;

    public QueryRespone(String query, String clusterName) {
        this.query = query;
        this.clusterName = clusterName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
