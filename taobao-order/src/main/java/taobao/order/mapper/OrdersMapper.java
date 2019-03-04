package taobao.order.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import taobao.order.model.Orders;

public interface OrdersMapper {
    @Delete({
        "delete from orders",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into orders (total_cost, user_id, ",
        "status, create_time, ",
        "update_time, pay_channel)",
        "values (#{totalCost,jdbcType=DECIMAL}, #{userId,jdbcType=BIGINT}, ",
        "#{status,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{payChannel,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(Orders record);

    int insertSelective(Orders record);

    @Select({
        "select",
        "id, total_cost, user_id, status, create_time, update_time, pay_channel",
        "from orders",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.order.mapper.OrdersMapper.BaseResultMap")
    Orders selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Orders record);

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
    int updateByPrimaryKey(Orders record);
}