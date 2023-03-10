package com.outletcn.app.service.chain;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.chain.*;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.DestinationAttribute;
import com.outletcn.app.model.mongo.DestinationType;

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
    String createDestination(CreateDestinationRequest createDestinationRequest);

    /**
     * 创建目的地类型
     * @param createDestinationTypeRequest
     */
    boolean createDestinationType(CreateDestinationTypeRequest createDestinationTypeRequest);

    /**
     * 创建目的地属性
     * @param createDestinationAttributeRequest
     */
    boolean createDestinationAttribute(CreateDestinationAttributeRequest createDestinationAttributeRequest);

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
    boolean modifyDestination(CreateDestinationRequest createDestinationRequest, Long id);

    /**
     * 基于ID查询目的地
     * @param id
     * @return
     */
    QueryOneResponse<Destination> findDestinationById(Long id);

    /**
     * 上/下架目的地
     * @param putOnRequest
     * @return
     */
    boolean putOnDestination(PutOnRequest putOnRequest);

    /**
     * 查询目的地与群、线路绑定关系
     * @param id
     * @return
     */
    PutOnDestinationResponse getRelates(Long id);

    /**
     * 基于名称模糊查询
     * @param name
     */
    List<Destination> findDestinationByName(String name);

    /**
     * 基于名称模糊查询（分页）
     * @param name
     * @param current
     * @param size
     * @return
     */
    PageInfo<Destination> findDestinationByNameForPage(String name, int current, int size);

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
     * 基于上下架状态查询
     * @param putOn
     * @param current
     * @param size
     * @return
     */
    PageInfo<Destination> findDestinationByPutOnForPage(int putOn, int current, int size);

    /**
     * 查询所有
     * @param current
     * @param size
     * @return
     */
    PageInfo<Destination> findAllForPage(int current, int size);


    /**
     * 基于名称、上下架查询
     * @param name
     * @param putOn
     * @param current
     * @param size
     * @return
     */
    PageInfo<QueryDestinationResponse> findDestinationByNameOrPutOnForPage(String name, int putOn, int current, int size);

    /**
     * 查询目的地属性列表
     * @return
     */
    List<DestinationAttribute> findDestinationAttributes();

    /**
     * 查询目的地类型列表
     * @return
     */
    List<DestinationType> findDestinationTypes();
}
