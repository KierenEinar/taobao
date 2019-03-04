package taobao.user.mapper;

import org.apache.ibatis.annotations.*;
import taobao.user.model.UserReceiveConfig;

import java.util.List;

public interface UserReceiveConfigMapper {
    @Delete({
        "delete from user_receive_config",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into user_receive_config (user_id, receiver_name, ",
        "receiver_phone, receiver_detail, ",
        "create_time, update_time)",
        "values (#{userId,jdbcType=BIGINT}, #{receiverName,jdbcType=VARCHAR}, ",
        "#{receiverPhone,jdbcType=VARCHAR}, #{receiverDetail,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(UserReceiveConfig record);

    int insertSelective(UserReceiveConfig record);

    @Select({
        "select",
        "id, user_id, receiver_name, receiver_phone, receiver_detail, create_time, update_time",
        "from user_receive_config",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.user.mapper.UserReceiveConfigMapper.BaseResultMap")
    UserReceiveConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserReceiveConfig record);

    @Update({
        "update user_receive_config",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "receiver_name = #{receiverName,jdbcType=VARCHAR},",
          "receiver_phone = #{receiverPhone,jdbcType=VARCHAR},",
          "receiver_detail = #{receiverDetail,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UserReceiveConfig record);


    @Select("select * from user_receive_config where user_id = #{userId};")
    List<UserReceiveConfig> findByUserId(@Param("userId") Long userId);


}