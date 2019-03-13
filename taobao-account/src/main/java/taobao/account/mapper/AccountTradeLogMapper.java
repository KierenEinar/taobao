package taobao.account.mapper;

import org.apache.ibatis.annotations.*;
import taobao.account.model.AccountTradeLog;
import taobao.core.vo.OrderPayVo;

public interface AccountTradeLogMapper {
    @Delete({
        "delete from account_trade_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into account_trade_log (from_account_id, to_account_id, ",
        "create_time, balance, ",
        "remark, channel, order_id, user_id)",
        "values (#{fromAccountId,jdbcType=BIGINT}, #{toAccountId,jdbcType=BIGINT}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{balance,jdbcType=DECIMAL}, ",
        "#{remark,jdbcType=VARCHAR}, #{channel,jdbcType=VARCHAR}, #{orderId, jdbcType=VARCHAR}, #{userId, jdbcType=BIGINT})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(AccountTradeLog record);

    int insertSelective(AccountTradeLog record);

    @Select({
        "select",
        "id, from_account_id, to_account_id, create_time, balance, remark, channel, order_id, user_id",
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
          "order_id = #{orderId,jdbcType=VARCHAR}",
          "user_id = #{userId,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(AccountTradeLog record);

    @Select("select * from account_trade_log where user_id = #{orderPayVo.userId} and id = #{orderPayVo.tradeNo}")
    AccountTradeLog selectByIdAndUserId(@Param("orderPayVo") OrderPayVo orderPayVo);
}