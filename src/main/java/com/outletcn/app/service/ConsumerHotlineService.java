package com.outletcn.app.service;

import com.outletcn.app.model.dto.consumerhotline.ConsumerHotlineAddRequest;
import com.outletcn.app.model.mysql.ConsumerHotline;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 客服电话 服务类
 * </p>
 *
 * @author linx
 * @since 2022-06-28
 */
public interface ConsumerHotlineService extends IService<ConsumerHotline> {

    boolean add(ConsumerHotlineAddRequest request);
}
