package taobao.product.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import taobao.product.models.ProductParamsTemplate;

public interface ProductParamsTemplateMapper {
    @Delete({
        "delete from product_params_template",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into product_params_template (product_id, type, ",
        "create_time, update_time, ",
        "template_json)",
        "values (#{productId,jdbcType=BIGINT}, #{type,jdbcType=CHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP}, ",
        "#{templateJson,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(ProductParamsTemplate record);

    int insertSelective(ProductParamsTemplate record);

    @Select({
        "select",
        "id, product_id, type, create_time, update_time, template_json",
        "from product_params_template",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.product.mapper.ProductParamsTemplateMapper.ResultMapWithBLOBs")
    ProductParamsTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductParamsTemplate record);

    @Update({
        "update product_params_template",
        "set product_id = #{productId,jdbcType=BIGINT},",
          "type = #{type,jdbcType=CHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "template_json = #{templateJson,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKeyWithBLOBs(ProductParamsTemplate record);

    @Update({
        "update product_params_template",
        "set product_id = #{productId,jdbcType=BIGINT},",
          "type = #{type,jdbcType=CHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ProductParamsTemplate record);
}