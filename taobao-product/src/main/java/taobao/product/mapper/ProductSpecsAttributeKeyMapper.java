package taobao.product.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import taobao.product.models.ProductSpecsAttributeKey;

public interface ProductSpecsAttributeKeyMapper {
    @Delete({
        "delete from product_specs_attribute_key",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into product_specs_attribute_key (name, product_id, ",
        "create_time)",
        "values (#{name,jdbcType=VARCHAR}, #{productId,jdbcType=BIGINT}, ",
        "#{createTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(ProductSpecsAttributeKey record);

    int insertSelective(ProductSpecsAttributeKey record);

    @Select({
        "select",
        "id, name, product_id, create_time",
        "from product_specs_attribute_key",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.product.mapper.ProductSpecsAttributeKeyMapper.BaseResultMap")
    ProductSpecsAttributeKey selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductSpecsAttributeKey record);

    @Update({
        "update product_specs_attribute_key",
        "set name = #{name,jdbcType=VARCHAR},",
          "product_id = #{productId,jdbcType=BIGINT},",
          "create_time = #{createTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ProductSpecsAttributeKey record);
}