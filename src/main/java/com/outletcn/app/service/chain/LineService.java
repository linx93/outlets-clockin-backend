package com.outletcn.app.service.chain;

import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.model.dto.chain.CreateLineAttributeRequest;
import com.outletcn.app.model.dto.chain.CreateLineRequest;
import com.outletcn.app.model.dto.chain.PutOnRequest;
import com.outletcn.app.model.dto.chain.StickRequest;
import com.outletcn.app.model.mongo.Line;

import java.util.List;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 3:00 PM
 */
public interface LineService {

    /**
     * 创建线路
     * @param createLineRequest
     */
    boolean createLine(CreateLineRequest createLineRequest);

    /**
     * 修改线路
     * @param createLineRequest
     * @param id
     * @return
     */
    boolean modifyLine(CreateLineRequest createLineRequest, Long id);

    /**
     * 删除线路
     * @param id
     */
    boolean deleteLine(Long id);

    /**
     * 创建线路属性
     * @param createLineAttributeRequest
     */
    boolean createLineAttribute(CreateLineAttributeRequest createLineAttributeRequest);

    /**
     * 上/下架线路
     * @param putOnRequest
     */
    boolean putOnLine(PutOnRequest putOnRequest);

    /**
     * 置顶线路
     * @param stickRequest
     */
    boolean stickLine(StickRequest stickRequest);

    /**
     * 基于属性查询线路
     * @param attr
     */
    List<Line> findLineByAttr(String attr);

    /**
     * 基于目的地查询线路
     * @param destinationName
     */
    List<Line> findLineByDestinationName(String destinationName);

    /**
     * 通过线路id查询线路下的目的地和目的地群
     * @param id 线路id
     * @return
     */
    LineElementsVO lineElementsById(Long id);

    /**
     * 通过线路id查询线路下所有目的地的经纬度
     * @param id 线路id
     * @return apiResult
     */
    List<DestinationMapVO> lineElementsMapById(Long id);

    /**
     * 路线列表
     * @param lineListRequest
     * @return 路线列表
     */
    List<LineVO> lineList(LineListRequest lineListRequest);

    /**
     * 路线的选项卡接口
     * @return LineTabVO
     */
    List<LineTabVO> lineTab();
}
