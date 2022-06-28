package com.outletcn.app.model.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客服电话
 * </p>
 *
 * @author linx
 * @since 2022-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ConsumerHotline对象", description = "客服电话")
public class ConsumerHotline implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "电话号码")
    private String phoneNumber;

    private long createTime;

    private long updateTime;


}
