package com.outletcn.app.model.dto.map;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 城市区域搜索
 *
 * @author linx
 * @since 2022-05-31 11:48
 */
@Data
public class CityAreaSearchRequest {
    @NotBlank(message = "boundary必传")
    private String boundary;

    private Integer page_size;

    private Integer page_index;

    @NotBlank(message = "keyword必传")
    private String keyword;

    private String filter;
}

