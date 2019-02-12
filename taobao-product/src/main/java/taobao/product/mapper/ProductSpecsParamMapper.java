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
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into product_specs_param (name, value, ",
        "product_id, product_specs_id, ",
        "create_time)",
        "values (#{name,jdbcType=VARCHAR}, #{value,jdbcType=VARCHAR}, ",
        "#{productId,jdbcType=BIGINT}, #{productSpecsId,jdbcType=BIGINT}, ",
        "#{createTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(ProductSpecsParam record);

    int insertSelective(ProductSpecsParam record);

    @Select({
        "select",
        "id, name, value, product_id, product_specs_id, create_time",
        "from product_specs_param",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.product.mapper.ProductSpecsParamMapper.BaseResultMap")
    ProductSpecsParam selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductSpecsParam record);

    @Update({
        "update product_specs_param",
        "set name = #{name,jdbcType=VARCHAR},",
          "value = #{value,jdbcType=VARCHAR},",
          "product_id = #{productId,jdbcType=BIGINT},",
          "product_specs_id = #{productSpecsId,jdbcType=BIGINT},",
          "create_time = #{createTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ProductSpecsParam record);
}