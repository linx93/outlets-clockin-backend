package com.outletcn.app.service.chain;

import com.outletcn.app.model.dto.chain.CreateDestinationAttributeRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationTypeRequest;
import com.outletcn.app.model.dto.chain.PutOnRequest;
import com.outletcn.app.model.mongo.Destination;

import java.util.List;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/12 3:18 PM
 */
public interface DestinationService {

    /**
     * 创建目的地
     * @param createDestinationRequest
     */
    void createDestination(CreateDestinationRequest createDestinationRequest);

    /**
     * 创建目的地类型
     * @param createDestinationTypeRequest
     */
    void createDestinationType(CreateDestinationTypeRequest createDestinationTypeRequest);

    /**
     * 创建目的地属性
     * @param createDestinationAttributeRequest
     */
    void createDestinationAttribute(CreateDestinationAttributeRequest createDestinationAttributeRequest);

    /**
     * 删除目的地
     * @param id
     * @return
     */
    boolean deleteDestination(Long id);

    /**
     * 修改目的地
     * @param createDestinationRequest
     * @param id
     */
    void modifyDestination(CreateDestinationRequest createDestinationRequest, Long id);

    /**
     * 上/下架目的地
     * @param putOnRequest
     */
    void putOnDestination(PutOnRequest putOnRequest);

    /**
     * 基于名称模糊查询
     * @param name
     */
    List<Destination> findDestinationByName(String name);

    /**
     * 基于属性查询
     * @param attr
     */
    List<Destination> findDestinationByAttr(String attr);

    /**
     * 基于类型查询
     * @param type
     */
    List<Destination> findDestinationByType(String type);

    /**
     * 查询所有
     * @return
     */
//    List<Destination> findAll();

}
