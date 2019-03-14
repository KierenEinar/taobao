package taobao.product.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import taobao.product.models.ProductSpecsLocknumLog;

public interface ProductSpecsLocknumLogMapper {
    @Delete({
        "delete from product_specs_locknum_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into product_specs_locknum_log (create_time, specs_id, ",
        "incr_lock_num, product_id)",
        "values (#{createTime,jdbcType=DATE}, #{specsId,jdbcType=BIGINT}, ",
        "#{incrLockNum,jdbcType=INTEGER}, #{productId,jdbcType=BIGINT})"
    })
    int insert(ProductSpecsLocknumLog record);

    int insertSelective(ProductSpecsLocknumLog record);

    @Select({
        "select",
        "id, create_time, specs_id, incr_lock_num, product_id",
        "from product_specs_locknum_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.product.mapper.ProductSpecsLocknumLogMapper.BaseResultMap")
    ProductSpecsLocknumLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductSpecsLocknumLog record);

    @Update({
        "update product_specs_locknum_log",
        "set create_time = #{createTime,jdbcType=DATE},",
          "specs_id = #{specsId,jdbcType=BIGINT},",
          "incr_lock_num = #{incrLockNum,jdbcType=INTEGER},",
          "product_id = #{productId,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ProductSpecsLocknumLog record);
}