package taobao.product.mapper;

import org.apache.ibatis.annotations.*;
import taobao.product.dto.ProductDetailDto;
import taobao.product.models.Product;

import java.util.List;

public interface ProductMapper {
    @Delete({
        "delete from product",
        "where product_id = #{productId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long productId);

    @Insert({
        "insert into product (product_id, name, ",
        "title, create_time, ",
        "update_time, status, ",
        "html)",
        "values (#{productId,jdbcType=BIGINT}, #{name,jdbcType=VARCHAR}, ",
        "#{title,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{status,jdbcType=CHAR}, ",
        "#{html,jdbcType=LONGVARCHAR})"
    })
    int insert(Product record);

    int insertSelective(Product record);

    @Select({
        "select",
        "product_id, name, title, create_time, update_time, status, html",
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
          "status = #{status,jdbcType=CHAR},",
          "html = #{html,jdbcType=LONGVARCHAR}",
        "where product_id = #{productId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKeyWithBLOBs(Product record);

    @Update({
        "update product",
        "set name = #{name,jdbcType=VARCHAR},",
          "title = #{title,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "status = #{status,jdbcType=CHAR}",
        "where product_id = #{productId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Product record);

    int updateStatusByPreStatusAndProductId(@Param("preStatus") List<String> preStatus, @Param("status") String status, @Param("productId") Long productId);

    @Select("select a.*, b.name as attr_key from product a join product_specs_attribute_key b on a.product_id = b.product_id where a.product_id = #{arg0};")
    List<ProductDetailDto> selectProductsAttrKeyByProductId(Long productId);

}