package taobao.product.mapper;

import org.apache.ibatis.annotations.*;
import taobao.product.models.Product;

public interface ProductMapper {
    @Delete({
        "delete from product",
        "where product_id = #{productId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long productId);

    @Insert({
        "insert into product ( name, ",
        "title, create_time, ",
        "update_time, html)",
        "values ( #{name,jdbcType=VARCHAR}, ",
        "#{title,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{html,jdbcType=LONGVARCHAR})"
    })
    @SelectKey(statement = "select last_insert_id() as product_id", keyProperty = "productId", keyColumn = "product_id", before = false, resultType = Long.class)
    int insert(Product record);

    int insertSelective(Product record);

    @Select({
        "select",
        "product_id, name, title, create_time, update_time, html",
        "from product",
        "where product_id = #{productId,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.product.mapper.ProductMapper.ResultMapWithBLOBs")
    Product selectByPrimaryKey(Long productId);

    int updateByPrimaryKeySelective(Product record);

    @Update({
        "update product",
        "set name = #{name,jdbcType=VARCHAR},",
          "title = #{title,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "html = #{html,jdbcType=LONGVARCHAR}",
        "where product_id = #{productId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKeyWithBLOBs(Product record);

    @Update({
        "update product",
        "set name = #{name,jdbcType=VARCHAR},",
          "title = #{title,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where product_id = #{productId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Product record);
}