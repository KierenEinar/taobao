package taobao.product.mapper;

import org.apache.ibatis.annotations.*;
import taobao.product.models.ProductSpecsAttributeKey;
import taobao.product.models.ProductSpecsAttributeValue;

import java.util.List;

public interface ProductSpecsAttributeValueMapper {
    @Delete({
        "delete from product_specs_attribute_value",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into product_specs_attribute_value (value, attr_id, ",
        "product_id, create_time)",
        "values (#{value,jdbcType=VARCHAR}, #{attrId,jdbcType=BIGINT}, ",
        "#{productId,jdbcType=BIGINT}, #{createTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(ProductSpecsAttributeValue record);

    int insertSelective(ProductSpecsAttributeValue record);

    @Select({
        "select",
        "id, value, attr_id, product_id, create_time",
        "from product_specs_attribute_value",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.product.mapper.ProductSpecsAttributeValueMapper.BaseResultMap")
    ProductSpecsAttributeValue selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductSpecsAttributeValue record);

    @Update({
        "update product_specs_attribute_value",
        "set value = #{value,jdbcType=VARCHAR},",
          "attr_id = #{attrId,jdbcType=BIGINT},",
          "product_id = #{productId,jdbcType=BIGINT},",
          "create_time = #{createTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ProductSpecsAttributeValue record);

    int insertBatch(@Param("list") List<ProductSpecsAttributeKey> keys);
}