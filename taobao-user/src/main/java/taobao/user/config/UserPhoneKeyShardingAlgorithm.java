package taobao.user.config;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class UserPhoneKeyShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    Logger logger = LoggerFactory.getLogger(UserPhoneKeyShardingAlgorithm.class);

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {

        for (String db : availableTargetNames) {
            String value = shardingValue.getValue();
            if (StringUtils.isBlank(value)) throw new UnsupportedOperationException("user表phone列值用于分片, 不能为空");
            int index = value.hashCode() % availableTargetNames.size();
            if (index<0) index = 0 - index;
            if (db.endsWith(index+"")) return db;
        }
        return null;
    }
}
