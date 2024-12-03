package com.microboxlabs.service.contract.to.criteria;

public class CriteriaTO {
    private String search;
    private int page;
    private int size;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public CriteriaTO withPage(int page) {
        setPage(page);
        return this;
    }
    public CriteriaTO withSize(int size) {
        setSize(size);
        return this;
    }
}
