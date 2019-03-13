package taobao.account.mapper;

import org.apache.ibatis.annotations.*;
import taobao.account.model.Account;

import java.math.BigDecimal;

public interface AccountMapper {
    @Delete({
        "delete from account",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into account (user_id, balance, ",
        "lock_balance, status, ",
        "create_time, update_time)",
        "values (#{userId,jdbcType=BIGINT}, #{balance,jdbcType=DECIMAL}, ",
        "#{lockBalance,jdbcType=DECIMAL}, #{status,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(Account record);

    int insertSelective(Account record);

    @Select({
        "select",
        "id, user_id, balance, lock_balance, status, create_time, update_time",
        "from account",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.account.mapper.AccountMapper.BaseResultMap")
    Account selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Account record);

    @Update({
        "update account",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "balance = #{balance,jdbcType=DECIMAL},",
          "lock_balance = #{lockBalance,jdbcType=DECIMAL},",
          "status = #{status,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Account record);

    int updateDecrBalanceAndIncrLockBalance(@Param("userId") Long userId, @Param("amount") BigDecimal amount);
}