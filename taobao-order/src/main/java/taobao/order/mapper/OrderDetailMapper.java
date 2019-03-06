package taobao.order.mapper;

import org.apache.ibatis.annotations.*;
import taobao.order.model.OrderDetail;

import java.util.List;

public interface OrderDetailMapper {
    @Delete({
        "delete from order_detail",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into order_detail (order_id, product_id, ",
        "user_id, quantity, ",
        "price, create_time, ",
        "update_time, product_specs_id)",
        "values (#{orderId,jdbcType=BIGINT}, #{productId,jdbcType=BIGINT}, ",
        "#{userId,jdbcType=BIGINT}, #{quantity,jdbcType=INTEGER}, ",
        "#{price,jdbcType=DECIMAL}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{productSpecsId,jdbcType=BIGINT})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(OrderDetail record);

    int insertSelective(OrderDetail record);

    @Select({
        "select",
        "id, order_id, product_id, user_id, quantity, price, create_time, update_time, ",
        "product_specs_id",
        "from order_detail",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.order.mapper.OrderDetailMapper.BaseResultMap")
    OrderDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderDetail record);

    @Update({
        "update order_detail",
        "set order_id = #{orderId,jdbcType=BIGINT},",
          "product_id = #{productId,jdbcType=BIGINT},",
          "user_id = #{userId,jdbcType=BIGINT},",
          "quantity = #{quantity,jdbcType=INTEGER},",
          "price = #{price,jdbcType=DECIMAL},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "product_specs_id = #{productSpecsId,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(OrderDetail record);

    int insertBatch(@Param("list") List<OrderDetail> details);
}