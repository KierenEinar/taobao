package taobao.product.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import taobao.product.models.ProductSpecsInfo;

public interface ProductSpecsInfoMapper {
    @Delete({
        "delete from product_specs_info",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into product_specs_info (name, value, ",
        "product_id, product_specs_id, ",
        "create_time)",
        "values (#{name,jdbcType=VARCHAR}, #{value,jdbcType=VARCHAR}, ",
        "#{productId,jdbcType=BIGINT}, #{productSpecsId,jdbcType=BIGINT}, ",
        "#{createTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(ProductSpecsInfo record);

    int insertSelective(ProductSpecsInfo record);

    @Select({
        "select",
        "id, name, value, product_id, product_specs_id, create_time",
        "from product_specs_info",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.product.mapper.ProductSpecsInfoMapper.BaseResultMap")
    ProductSpecsInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductSpecsInfo record);

    @Update({
        "update product_specs_info",
        "set name = #{name,jdbcType=VARCHAR},",
          "value = #{value,jdbcType=VARCHAR},",
          "product_id = #{productId,jdbcType=BIGINT},",
          "product_specs_id = #{productSpecsId,jdbcType=BIGINT},",
          "create_time = #{createTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ProductSpecsInfo record);
}