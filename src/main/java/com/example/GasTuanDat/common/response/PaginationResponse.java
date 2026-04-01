package com.example.GasTuanDat.common.response;

import java.util.List;

public class PaginationResponse<T> {
    private List<T> items;
    private int page;
    private int size;
    private long total;
}
