package com.outletcn.app.mapper;

import com.alibaba.fastjson.JSONObject;
import com.outletcn.app.model.mysql.PunchLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 打卡日志
 Mapper 接口
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
public interface PunchLogMapper extends BaseMapper<PunchLog> {
    @Select("SELECT" +
            "COUNT( 1 ) AS countData," +
            "FROM_UNIXTIME( `create_time`, '%Y-%m-%d' ) AS days " +
            "FROM" +
            "punch_log" +
            "WHERE" +
            "create_time BETWEEN #{begin} AND #{end} " +
            "GROUP BY" +
            "days" +
            "ORDER BY" +
            "days ASC")
    JSONObject punchTimesStatistics(@Param("begin") Long begin,@Param("end") Long end);
    
    @Select("SELECT" +
            "COUNT( DISTINCT ( user_id )) AS num," +
            "FROM_UNIXTIME( `create_time`, '%Y-%m-%d' ) AS days " +
            "FROM" +
            "punch_log " +
            "WHERE" +
            "create_time BETWEEN #{begin} AND #{end} " +
            "GROUP BY" +
            "days " +
            "ORDER BY" +
            "days ASC")
    JSONObject activePeopleStatistics(@Param("begin") Long begin,@Param("end") Long end);

    @Select("SELECT" +
            "COUNT( 1 ) AS num," +
            "destination_name AS NAME" +
            "FROM" +
            "punch_log" +
            "GROUP BY" +
            "destination_name" +
            "ORDER BY" +
            "num DESC " +
            "LIMIT 10")
    JSONObject topDestinationStatistics();

    @Select("SELECT" +
            "count( 1 ) AS num," +
            "gift_name AS NAME " +
            "FROM" +
            "gift_voucher" +
            "GROUP BY" +
            "gift_id" +
            "ORDER BY" +
            "num DESC" +
            "LIMIT 10")
    JSONObject topGiftStatistics();

}
