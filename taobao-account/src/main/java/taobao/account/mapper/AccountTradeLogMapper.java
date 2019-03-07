package taobao.account.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import taobao.account.model.AccountTradeLog;

public interface AccountTradeLogMapper {
    @Delete({
        "delete from account_trade_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into account_trade_log (from_account_id, to_account_id, ",
        "create_time, balance, ",
        "remark, channel)",
        "values (#{fromAccountId,jdbcType=BIGINT}, #{toAccountId,jdbcType=BIGINT}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{balance,jdbcType=DECIMAL}, ",
        "#{remark,jdbcType=VARCHAR}, #{channel,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(AccountTradeLog record);

    int insertSelective(AccountTradeLog record);

    @Select({
        "select",
        "id, from_account_id, to_account_id, create_time, balance, remark, channel",
        "from account_trade_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("taobao.account.mapper.AccountTradeLogMapper.BaseResultMap")
    AccountTradeLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountTradeLog record);

    @Update({
        "update account_trade_log",
        "set from_account_id = #{fromAccountId,jdbcType=BIGINT},",
          "to_account_id = #{toAccountId,jdbcType=BIGINT},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "balance = #{balance,jdbcType=DECIMAL},",
          "remark = #{remark,jdbcType=VARCHAR},",
          "channel = #{channel,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(AccountTradeLog record);
}