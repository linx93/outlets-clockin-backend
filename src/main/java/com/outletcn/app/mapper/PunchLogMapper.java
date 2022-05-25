package com.outletcn.app.mapper;

import com.alibaba.fastjson.JSONObject;
import com.outletcn.app.model.dto.statistics.CommonTopListUnit;
import com.outletcn.app.model.dto.statistics.HeatMap;
import com.outletcn.app.model.mysql.PunchLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 打卡日志
 * Mapper 接口
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Component
public interface PunchLogMapper extends BaseMapper<PunchLog> {
    //打卡点排行
    @Select("SELECT " +
            "COUNT( 1 ) AS num," +
            "destination_name AS name " +
            "FROM " +
            "punch_log " +
            "GROUP BY " +
            "destination_name " +
            "ORDER BY " +
            "num DESC " +
            "LIMIT 10")
    List<CommonTopListUnit> topDestinationStatistics();

    //礼品包排行
    @Select("SELECT " +
            "count( 1 ) AS num," +
            "gift_name AS name " +
            "FROM " +
            "gift_voucher " +
            "GROUP BY " +
            "gift_id " +
            "ORDER BY " +
            "num DESC " +
            "LIMIT 10")
    List<CommonTopListUnit> topGiftStatistics();

    //打卡次数统计（时间）
    @Select("SELECT " +
            "COUNT( 1 ) AS count," +
            "FROM_UNIXTIME( `create_time`, '%Y-%m-%d' ) AS days " +
            "FROM " +
            "punch_log " +
            "WHERE " +
            "create_time BETWEEN #{begin} AND #{end} " +
            "GROUP BY " +
            "days " +
            "ORDER BY " +
            "days ASC")
    List<JSONObject> punchTimesStatistics(@Param("begin") Long begin, @Param("end") Long end);

    //活跃用户人数统计（时间）
    @Select("SELECT " +
            "COUNT( DISTINCT ( user_id )) AS count," +
            "FROM_UNIXTIME( `create_time`, '%Y-%m-%d' ) AS days " +
            "FROM " +
            "punch_log " +
            "WHERE " +
            "create_time BETWEEN #{begin} AND #{end} " +
            "GROUP BY " +
            "days " +
            "ORDER BY " +
            "days ASC")
    List<JSONObject> activePeopleStatistics(@Param("begin") Long begin, @Param("end") Long end);

    //新增用户统计
    @Select("SELECT " +
            "COUNT(1) as num " +
            "FROM clock_in_user " +
            "WHERE create_time BETWEEN #{begin} AND #{end}")
    Integer countNewUser(@Param("begin") Date begin, @Param("end") Date end);

    //总用户数量统计
    @Select("SELECT " +
            "COUNT(1) as num " +
            "FROM clock_in_user")
    Integer countUser();


    //电子券核销统计
    @Select("SELECT " +
            "COUNT(1) as num " +
            "FROM gift_voucher " +
            "where state = 1 and exchange_time BETWEEN #{begin} AND #{end}")
    Integer countUsedGiftVoucher(@Param("begin") Long begin, @Param("end") Long end);

    //电子券兑换统计（积分兑换礼品券次数）
    @Select("SELECT COUNT(1) as num " +
            "FROM gift_voucher " +
            "WHERE create_time BETWEEN #{begin} AND #{end}")
    Integer countGiftVoucher(@Param("begin") Long begin, @Param("end") Long end);

    //热力图
    @Select("SELECT " +
            "destination_id as destinationId, " +
            "destination_name as destinationName, " +
            "punch_longitude as lng, " +
            "punch_latitude as lat, " +
            "COUNT(DISTINCT ( user_id )) AS count  " +
            "FROM " +
            "punch_log  " +
            "WHERE " +
            "create_time BETWEEN #{begin} AND #{end} " +
            "GROUP BY " +
            "destination_id")
    List<HeatMap> heatMap(@Param("begin") Long begin, @Param("end") Long end);
}
