package com.outletcn.app.service.chain;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.model.dto.chain.*;
import com.outletcn.app.model.mongo.Line;
import com.outletcn.app.model.mongo.LineAttribute;

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
     *
     * @param createLineRequest
     */
    String createLine(CreateLineRequest createLineRequest);

    /**
     * 修改线路
     *
     * @param createLineRequest
     * @param id
     * @return
     */
    boolean modifyLine(CreateLineRequest createLineRequest, Long id);

    /**
     * 删除线路
     *
     * @param id
     */
    boolean deleteLine(Long id);

    /**
     * 基于ID查询线路信息
     *
     * @param id
     * @return
     */
    QueryLineOneResponse findLineById(Long id);

    /**
     * 创建线路属性
     *
     * @param createLineAttributeRequest
     */
    boolean createLineAttribute(CreateLineAttributeRequest createLineAttributeRequest);

    /**
     * 上/下架线路
     *
     * @param putOnRequest
     */
    boolean putOnLine(PutOnRequest putOnRequest);

    /**
     * 置顶线路
     *
     * @param stickRequest
     */
    boolean stickLine(StickRequest stickRequest);

    /**
     * 基于属性查询线路
     *
     * @param attr
     */
    List<Line> findLineByAttr(String attr);

    /**
     * 基于目的地查询线路
     *
     * @param destinationName
     */
    List<Line> findLineByDestinationName(String destinationName);

    /**
     * 基于名称、上下架查询
     *
     * @param name
     * @param putOn
     * @param current
     * @param size
     * @return
     */
    PageInfo<QueryLineResponse> findLineByNameOrPutOnForPage(String name, int putOn, int current, int size);

    /**
     * 通过线路id查询线路下的目的地和目的地群
     *
     * @param id 线路id
     * @return
     */
    LineElementsVO lineElementsById(Long id);

    /**
     * 通过线路id查询线路下所有目的地的经纬度
     *
     * @param id 线路id
     * @return apiResult
     */
    List<DestinationMapVO> lineElementsMapById(Long id);

    /**
     * 路线列表
     *
     * @param lineListRequest
     * @return 路线列表
     */
    List<LineVO> lineList(LineListRequest lineListRequest);

    /**
     * 路线的选项卡接口
     *
     * @return LineTabVO
     */
    List<LineTabVO> lineTab();

    /**
     * 查询线路属性列表
     *
     * @return
     */
    List<LineAttribute> findLineAttributes();

    /**
     * 附近
     *
     * @param nearbyRequest req
     * @return res
     */
    List<DestinationVO> nearby(NearbyRequest nearbyRequest);

    /**
     * 目的地详情
     *
     * @param id 目的地id
     * @return 目的地详情数据
     */
    DestinationVO destinationDetails(Long id);


    /**
     * 目的地群详情
     *
     * @param id 目的地群id
     * @return 目的地群详情vo
     */
    DestinationGroupVO destinationGroupDetails(Long id);

    /**
     * 搜索目的地，返回包含目的地的路线list和目的地list
     *
     * @param searchDestinationRequest keywords 目的地名
     * @return
     */
    SearchDestinationResponse searchDestination(SearchDestinationRequest searchDestinationRequest);

    /**
     * 线路详情
     *
     * @param id 线路id
     * @return 线路详情
     */
    LineItemsVO lineDetailsById(Long id);
}
