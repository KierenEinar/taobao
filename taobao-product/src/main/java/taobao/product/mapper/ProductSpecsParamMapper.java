package taobao.product.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import taobao.product.models.ProductSpecsParam;

public interface ProductSpecsParamMapper {
    @Delete({
        "delete from product_specs_param",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String id);

    @Insert({
        "insert into product_specs_param (name, value, ",
        "product_specs_id, create_time)",
        "values (#{name,jdbcType=VARCHAR}, #{value,jdbcType=VARCHAR}, ",
        "#{productSpecsId,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=String.class)
    int insert(ProductSpecsParam record);

    int insertSelective(ProductSpecsParam record);

    @Select({
        "select",
        "id, name, value, product_specs_id, create_time",
        "from product_specs_param",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    @ResultMap("taobao.product.mapper.ProductSpecsParamMapper.BaseResultMap")
    ProductSpecsParam selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProductSpecsParam record);

    @Update({
        "update product_specs_param",
        "set name = #{name,jdbcType=VARCHAR},",
          "value = #{value,jdbcType=VARCHAR},",
          "product_specs_id = #{productSpecsId,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(ProductSpecsParam record);
}