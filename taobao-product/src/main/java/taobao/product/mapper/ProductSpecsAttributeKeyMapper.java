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
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String id);

    @Insert({
        "insert into product_specs_attribute_key (name, product_id, ",
        "create_time)",
        "values (#{name,jdbcType=VARCHAR}, #{productId,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=String.class)
    int insert(ProductSpecsAttributeKey record);

    int insertSelective(ProductSpecsAttributeKey record);

    @Select({
        "select",
        "id, name, product_id, create_time",
        "from product_specs_attribute_key",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    @ResultMap("taobao.product.mapper.ProductSpecsAttributeKeyMapper.BaseResultMap")
    ProductSpecsAttributeKey selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProductSpecsAttributeKey record);

    @Update({
        "update product_specs_attribute_key",
        "set name = #{name,jdbcType=VARCHAR},",
          "product_id = #{productId,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(ProductSpecsAttributeKey record);
}