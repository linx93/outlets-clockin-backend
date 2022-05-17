package com.outletcn.app.service.chain;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.applet.LineElementsVO;
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
     * 通过线路id查询线路下的目的地和目的地群
     * @param id 线路id
     * @return
     */
    ApiResult<LineElementsVO> lineElementsById(Long id);
}
