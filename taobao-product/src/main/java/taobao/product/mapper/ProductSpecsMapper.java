package taobao.product.mapper;

import org.apache.ibatis.annotations.*;
import taobao.core.vo.InventoryWebVo;
import taobao.product.models.ProductSpecs;

import java.util.List;

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

    int insertBatch(@Param("list") List<ProductSpecs> productSpecsList);

    @Select("select * from product_specs where product_id = #{arg0};")
    List<ProductSpecs> selectByProductId(Long productId);

    @Update("update product_specs set stock = stock - #{num}, lock_num = lock_num + #{num} where product_id = #{productId} and id = #{specsId} and (stock - #{num}) > 0;")
    int updateInventory (@Param("productId") Long productId, @Param("specsId") Long specsId, @Param("num") Integer nums);

    ProductSpecs selectBySpeces(InventoryWebVo vos);

    @Update("update product_specs set lock_num = lock_num + #{num} where product_id = #{productId} and id = #{specsId} and (lock_num + #{num}) > 0;")
    int updateLockInventory(@Param("num") Integer num, @Param("productId") Long productId, @Param("specsId") Long specsId);
}