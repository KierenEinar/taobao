package taobao.product.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import taobao.product.models.ProductParamsItem;
import taobao.product.models.ProductSpecs;

import java.util.List;

public interface ProductParamsItemMapper {
    @Delete({
        "delete from product_params_item",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into product_params_item (product_id, product_specs_id, ",
        "type, create_time, ",
        "update_time, param_data)",
        "values (#{productId,jdbcType=BIGINT}, #{productSpecsId,jdbcType=BIGINT}, ",
        "#{type,jdbcType=CHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{paramData,jdbcType=LONGVARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(ProductParamsItem record);

    int insertSelective(ProductParamsItem record);

    @Select({
        "select",
        "id, product_id, product_specs_id, type, create_time, update_time, param_data",
        "from product_params_item",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.product.mapper.ProductParamsItemMapper.ResultMapWithBLOBs")
    ProductParamsItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductParamsItem record);

    @Update({
        "update product_params_item",
        "set product_id = #{productId,jdbcType=BIGINT},",
          "product_specs_id = #{productSpecsId,jdbcType=BIGINT},",
          "type = #{type,jdbcType=CHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "param_data = #{paramData,jdbcType=LONGVARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKeyWithBLOBs(ProductParamsItem record);

    @Update({
        "update product_params_item",
        "set product_id = #{productId,jdbcType=BIGINT},",
          "product_specs_id = #{productSpecsId,jdbcType=BIGINT},",
          "type = #{type,jdbcType=CHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ProductParamsItem record);

    int insertBatch(List<ProductParamsItem> items);

    @Select("select id, product_id, product_specs_id, type, create_time, update_time, param_data from product_params_item where product_id = #{arg0};")
    List<ProductParamsItem> selectByProductId(Long productId);
}