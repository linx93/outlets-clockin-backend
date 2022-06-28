package com.outletcn.app.service.impl;

import com.outletcn.app.model.dto.consumerhotline.ConsumerHotlineAddRequest;
import com.outletcn.app.model.mysql.ConsumerHotline;
import com.outletcn.app.mapper.ConsumerHotlineMapper;
import com.outletcn.app.service.ConsumerHotlineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * <p>
 * 客服电话 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-06-28
 */
@Service
public class ConsumerHotlineServiceImpl extends ServiceImpl<ConsumerHotlineMapper, ConsumerHotline> implements ConsumerHotlineService {

    @Override
    public boolean add(ConsumerHotlineAddRequest request) {
        ConsumerHotline consumerHotline = new ConsumerHotline();
        consumerHotline.setCreateTime(Instant.now().getEpochSecond());
        consumerHotline.setUpdateTime(Instant.now().getEpochSecond());
        consumerHotline.setPhoneNumber(request.getPhoneNumber());
        return save(consumerHotline);
    }
}
