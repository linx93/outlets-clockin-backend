package com.outletcn.app.model.dto;

import lombok.Data;

@Data
public class WriteOffListRequest {
    private Long begin;
    private Long end;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
}
