package com.outletcn.app.service.chain;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.chain.*;
import com.outletcn.app.model.mongo.DestinationGroup;
import com.outletcn.app.model.mongo.DestinationGroupAttribute;

import java.util.List;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 10:08 AM
 */
public interface DestinationGroupService {

    /**
     * 创建目的地群
     * @param createDestinationGroupRequest
     */
    boolean createDestinationGroup(CreateDestinationGroupRequest createDestinationGroupRequest);

    /**
     * 删除目的地群
     * @param id
     * @return
     */
    boolean deleteDestinationGroup(Long id);

    /**
     * 修改目的地群
     * @param createDestinationGroupRequest
     * @param id
     */
    boolean modifyDestinationGroup(CreateDestinationGroupRequest createDestinationGroupRequest, Long id);

    /**
     * 基于ID查询目的地群
     * @param id
     * @return
     */
    QueryDestinationGroupOneResponse findDestinationGroupById(Long id);

    /**
     * 创建目的地群属性
     * @param createDestinationGroupAttributeRequest
     */
    boolean createDestinationGroupAttribute(CreateDestinationGroupAttributeRequest createDestinationGroupAttributeRequest);

    /**
     * 上/下架目的地群
     * @param putOnRequest
     */
    boolean putOnDestinationGroup(PutOnRequest putOnRequest);

    /**
     * 查询目的地群与线路绑定关系
     * @param id
     * @return
     */
    List<PutOnDestinationResponse.LineItem> getRelates(Long id);

    /**
     * 基于名称模糊查询
     * @param name
     */
    List<DestinationGroup> findDestinationGroupByName(String name);

    /**
     * 基于名称模糊查询（分页）
     * @param name
     * @param current
     * @param size
     * @return
     */
    PageInfo<DestinationGroup> findDestinationGroupByNameForPage(String name, int current, int size);

    /**
     * 基于属性查询
     * @param attr
     */
    List<DestinationGroup> findDestinationGroupByAttr(String attr);

    /**
     * 基于上下架状态查询
     * @param putOn
     * @param current
     * @param size
     * @return
     */
    PageInfo<DestinationGroup> findDestinationGroupByPutOnForPage(int putOn, int current, int size);

    /**
     * 查询所有
     * @param current
     * @param size
     * @return
     */
    PageInfo<DestinationGroup> findAllForPage(int current, int size);

    /**
     * 基于名称、上下架查询
     * @param name
     * @param putOn
     * @param current
     * @param size
     * @return
     */
    PageInfo<QueryDestinationGroupResponse> findDestinationGroupByNameOrPutOnForPage(String name, int putOn, int current, int size);

    /**
     * 根据组ID获取打开分值
     * @param groupId
     * @return
     */
    int getDestinationForPoint(Long groupId);

    /**
     * 查询目的地群属性列表
     * @return
     */
    List<DestinationGroupAttribute> findDestinationGroupAttributes();
}
