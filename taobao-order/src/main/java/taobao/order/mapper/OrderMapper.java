package taobao.order.mapper;

import org.apache.ibatis.annotations.*;
import taobao.order.model.Order;

import java.util.Date;

public interface OrderMapper {
    @Delete({
        "delete from orders",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into orders (id, total_cost, user_id, ",
        "status, create_time, ",
        "update_time, pay_channel)",
        "values (#{id, jdbcType=BIGINT} ,#{totalCost,jdbcType=DECIMAL}, #{userId,jdbcType=BIGINT}, ",
        "#{status,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{payChannel,jdbcType=VARCHAR})"
    })
    int insert(Order record);

    int insertSelective(Order record);

    @Select({
        "select",
        "id, total_cost, user_id, status, create_time, update_time, pay_channel",
        "from orders",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.order.mapper.OrderMapper.BaseResultMap")
    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order record);

    @Update({
        "update orders",
        "set total_cost = #{totalCost,jdbcType=DECIMAL},",
          "user_id = #{userId,jdbcType=BIGINT},",
          "status = #{status,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "pay_channel = #{payChannel,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Order record);

    @Update("update orders set status = #{status}, update_time = #{updateTime} where id = #{id} and user_id = #{userId} and status = #{preStatus};")
    int updateStatusByPreStatusAndId(@Param("id") Long id, @Param("status") String status, @Param("preStatus") String preStatus, @Param("updateTime") Date updateTime, @Param("userId") Long userId);

    @Select("select * from order where id = #{id} and user_id = #{userId};")
    Order selectByIdAndUserId(@Param("id") Long id,@Param("userId") Long userId);
}