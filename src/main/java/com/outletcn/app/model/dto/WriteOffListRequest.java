package com.outletcn.app.model.dto;

import lombok.Data;

@Data
public class WriteOffListRequest {
    private Long begin;
    private Long end;
    private String condition;
    private Integer pageNum;
    private Integer pageSize;
}
