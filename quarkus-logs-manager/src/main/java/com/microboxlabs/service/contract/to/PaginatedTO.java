package com.microboxlabs.service.contract.to;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class PaginatedTO<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 6717246915348191850L;
    private List<T> data;
    private int page;
    private int size;
    private long total;

    public PaginatedTO(List<T> data, int page, int size, long total) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
