package taobao.product.repository.redis;

import org.springframework.data.repository.CrudRepository;
import taobao.product.models.redis.Product;

public interface ProductRedisRepository extends CrudRepository<Product,Long> {

}
