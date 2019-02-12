package taobao.product.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import taobao.product.models.ProductSpecs;

public interface ProductSpecsMapper {
    @Delete({
        "delete from product_specs",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into product_specs (product_id, attrs, ",
        "stock, price)",
        "values (#{productId,jdbcType=BIGINT}, #{attrs,jdbcType=VARCHAR}, ",
        "#{stock,jdbcType=INTEGER}, #{price,jdbcType=DECIMAL})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(ProductSpecs record);

    int insertSelective(ProductSpecs record);

    @Select({
        "select",
        "id, product_id, attrs, stock, price",
        "from product_specs",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.product.mapper.ProductSpecsMapper.BaseResultMap")
    ProductSpecs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductSpecs record);

    @Update({
        "update product_specs",
        "set product_id = #{productId,jdbcType=BIGINT},",
          "attrs = #{attrs,jdbcType=VARCHAR},",
          "stock = #{stock,jdbcType=INTEGER},",
          "price = #{price,jdbcType=DECIMAL}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ProductSpecs record);
}