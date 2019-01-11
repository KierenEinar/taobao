package taobao.user.mapper;

import taobao.user.entity.models.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Insert("insert into users (first_name, last_name, member_id, credit_id, create_time, update_time, gender) values (#{firstName}, #{lastName}, #{memberId}, #{creditId}, #{createTime}, #{updateTime}, #{gender});")
    int insertOne (User user);

    @Select("select * from users where id = #{id};")
    User selectOne(@Param("id") Long id);
}
