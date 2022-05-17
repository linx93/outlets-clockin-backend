package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "gift_brand")
public class GiftBrand  implements Serializable {

    private static final long serialVersionUID = -7242349478617984379L;

    private Long id;

    /**
     * 礼品品牌
     */
    @Indexed(unique = true)
    private String name;

    private Long createTime;

    private Long updateTime;
}
