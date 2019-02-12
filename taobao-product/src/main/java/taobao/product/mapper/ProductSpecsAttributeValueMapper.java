package taobao.product.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import taobao.product.models.ProductSpecsAttributeValue;

public interface ProductSpecsAttributeValueMapper {
    @Delete({
        "delete from product_specs_attribute_value",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String id);

    @Insert({
        "insert into product_specs_attribute_value (value, attr_id, ",
        "create_time)",
        "values (#{value,jdbcType=VARCHAR}, #{attrId,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=String.class)
    int insert(ProductSpecsAttributeValue record);

    int insertSelective(ProductSpecsAttributeValue record);

    @Select({
        "select",
        "id, value, attr_id, create_time",
        "from product_specs_attribute_value",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    @ResultMap("taobao.product.mapper.ProductSpecsAttributeValueMapper.BaseResultMap")
    ProductSpecsAttributeValue selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProductSpecsAttributeValue record);

    @Update({
        "update product_specs_attribute_value",
        "set value = #{value,jdbcType=VARCHAR},",
          "attr_id = #{attrId,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(ProductSpecsAttributeValue record);
}