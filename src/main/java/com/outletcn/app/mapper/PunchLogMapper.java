package com.outletcn.app.mapper;

import com.alibaba.fastjson.JSONObject;
import com.outletcn.app.model.mysql.PunchLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Date;

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
    //打卡次数统计（时间）
    @Select("SELECT " +
            "COUNT( 1 ) AS countData," +
            "FROM_UNIXTIME( `create_time`, '%Y-%m-%d' ) AS days " +
            "FROM " +
            "punch_log " +
            "WHERE " +
            "create_time BETWEEN #{begin} AND #{end} " +
            "GROUP BY " +
            "days " +
            "ORDER BY " +
            "days ASC")
    JSONObject punchTimesStatistics(@Param("begin") Long begin, @Param("end") Long end);

    //活跃用户人数统计（时间）
    @Select("SELECT " +
            "COUNT( DISTINCT ( user_id )) AS num," +
            "FROM_UNIXTIME( `create_time`, '%Y-%m-%d' ) AS days " +
            "FROM " +
            "punch_log " +
            "WHERE " +
            "create_time BETWEEN #{begin} AND #{end} " +
            "GROUP BY " +
            "days " +
            "ORDER BY " +
            "days ASC")
    JSONObject activePeopleStatistics(@Param("begin") Long begin, @Param("end") Long end);

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
    JSONObject topDestinationStatistics();

    //礼品包排行
    @Select("SELECT " +
            "count( 1 ) AS num," +
            "gift_name AS NAME " +
            "FROM " +
            "gift_voucher " +
            "GROUP BY " +
            "gift_id " +
            "ORDER BY " +
            "num DESC " +
            "LIMIT 10")
    JSONObject topGiftStatistics();

    //新增用户统计
    @Select("SELECT " +
            "COUNT(1) as num " +
            "FROM clock_in_user " +
            "WHERE create_time BETWEEN #{begin} AND #{end}")
    JSONObject countNewUser(@Param("begin") Date begin, @Param("end") Date end);

    //总用户数量统计
    @Select("SELECT" +
            "COUNT(1) as num " +
            "FROM clock_in_user " +
            "WHERE create_time BETWEEN #{begin} AND #{end}")
    JSONObject countUser(@Param("begin") Date begin, @Param("end") Date end);


    //电子券核销统计
    @Select("SELECT " +
            "COUNT(1) as num " +
            "FROM gift_voucher " +
            "where state = 1 and exchange_time BETWEEN #{begin} AND #{end}")
    JSONObject countUsedGiftVoucher(@Param("begin") Long begin, @Param("end") Long end);

    //电子券兑换统计（积分兑换礼品券次数）
    @Select("SELECT COUNT(1) as num " +
            "FROM gift_voucher " +
            "WHERE create_time BETWEEN #{begin} AND #{end}")
    JSONObject countGiftVoucher(@Param("begin") Long begin, @Param("end") Long end);


}
