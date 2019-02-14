package taobao.product.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import taobao.product.constant.ProductCreateEventEnum;
import taobao.product.models.EventLog;

public interface EventLogMapper {
    @Delete({
        "delete from event_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into event_log (product_id, event_name, ",
        "status, pre_status, ",
        "version)",
        "values (#{productId,jdbcType=BIGINT}, #{eventName,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR}, #{preStatus,jdbcType=VARCHAR}, ",
        "#{version,jdbcType=INTEGER})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(EventLog record);

    int insertSelective(EventLog record);

    @Select({
        "select",
        "id, product_id, event_name, status, pre_status, version",
        "from event_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.product.mapper.EventLogMapper.BaseResultMap")
    EventLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EventLog record);

    @Update({
        "update event_log",
        "set product_id = #{productId,jdbcType=BIGINT},",
          "event_name = #{eventName,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR},",
          "pre_status = #{preStatus,jdbcType=VARCHAR},",
          "version = #{version,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(EventLog record);

    @Update("insert into event_log (product_id, event_name, status, pre_status) values (#{productId}, #{eventName}, #{status}, #{preStatus}) " +
            "on duplicate key update version = version + 1, status = #{status}, pre_status = #{preStatus}")
    int upsert(EventLog eventLog);

    @Update("update event_log set status = #{arg2}, version = version + 1 where product_id = #{arg0} and status = #{arg1};")
    int updateStatusByProductIdAndPreStatus(Long productId, ProductCreateEventEnum preStatus, ProductCreateEventEnum status);
}