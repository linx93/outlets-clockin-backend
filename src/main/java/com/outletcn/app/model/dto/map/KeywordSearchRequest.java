package com.outletcn.app.model.dto.map;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 关键词输入提示请求
 *
 * @author linx
 * @since 2022-05-31 16:19
 */
@Data
public class KeywordSearchRequest {
    @NotBlank(message = "region必传")
    private String region;
    @NotBlank(message = "keyword必传")
    private String keyword;
}
