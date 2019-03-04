package taobao.user.mapper;

import taobao.user.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Insert("insert into users (id, user_id, first_name, last_name, member_id, credit_id, create_time, update_time, gender, phone) values (#{id}, #{userId},#{firstName}, #{lastName}, #{memberId}, #{creditId}, #{createTime}, #{updateTime}, #{gender}, #{phone});")
    int insertOne (User user);

    @Select("select * from users where id = #{id};")
    User selectOne(@Param("id") Long id);

    @Select("select * from users where phone = #{phone};")
    User selectByPhone(@Param("phone") String mobile);
}
