package taobao.product.mapper;

import com.alibaba.fastjson.JSONObject;
import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import taobao.core.RedisService;
import taobao.product.App;
import taobao.product.constant.RedisPrefix;
import taobao.product.dto.ProductDetailDto;
import taobao.product.models.Product;
import taobao.product.models.ProductSpecsAttributeKey;
import taobao.product.repository.redis.ProductRedisRepository;
import taobao.product.service.ProductService;
import taobao.product.vo.ProductDetailVo;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class ProductMapperTest {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductService productService;

    @Autowired
    ProductSpecsAttributeKeyMapper productSpecsAttributeKeyMapper;

    @Autowired
    ProductSpecsAttributeValueMapper productSpecsAttributeValueMapper;

    Logger logger = LoggerFactory.getLogger(ProductMapperTest.class);

    @Autowired
    RedisService redisService;

    @Autowired
    ProductRedisRepository productRedisRepository;

    @Test
    @Transactional
    public void testInsert () {
        Product product = new Product();
        Long productId = new DefaultKeyGenerator().generateKey().longValue();
        product.setProductId(productId);
        product.setName("macbook pro");
        product.setTitle("京东精选 Apple MacBook Pro 13.3英寸笔记本电脑 深空灰色 2018新款（四核八代i5 8G 256G固态硬盘 MR9Q2CH/A）");
        product.setCreateTime(new Date());
        productMapper.insertSelective(product);
        ProductSpecsAttributeKey productSpecsAttributeKey = new ProductSpecsAttributeKey();
        productSpecsAttributeKey.setName("颜色");
        productSpecsAttributeKey.setProductId(product.getProductId());
        productSpecsAttributeKey.setCreateTime(new Date());
        productSpecsAttributeKeyMapper.insert(productSpecsAttributeKey);
        int a = 0/0;
    }

    @Test
    public void testJoin () {
        List<ProductDetailDto> detailDtos = productMapper.selectProductsAttrKeyByProductId(302770275328458753L);
        logger.info("{}", detailDtos);
    }

    @Test
    public void testFindDetail () {
        productService.findProductDetail(302770275328458753L);
    }

    @Test
    public void testCreateProduct2Hbase () {
        //productService.createProduct2Hbase(302770275328458753L);
        String value = redisService.get(RedisPrefix.productDetailByKey(302770275328458754L));
        ProductDetailVo productDetailVo = JSONObject.parseObject(value, ProductDetailVo.class);
        logger.info("productDetailVo -> {}", productDetailVo);
    }

    @Test
    public void testIncrProductStock() {
        redisService.hincr(RedisPrefix.productSpecsStockKey(302770275328458753L),"302770936333991938");
    }


    @Test
    public void testCreateProductDetail2Redis () {
        productService.createProduct2Redis(302770275328458753L, productService.findProductDetailFromHbase(302770275328458753L));
    }

//    @Test
//    public void testCreateStock2Redis () {
//        productService.releaseProductStock2Redis(302770275328458753L);
//    }


    @Test
    public void testFindProductFromHbase() {
        boolean result = redisService.setNX("hello","china");
        logger.info("result -> {}", result);
        productService.findProductDetailFromHbase(302770275328458753L);
    }

    @Test
    public void testRedisRepository() {
//        product.setId(1L);
//        product.setName("tamiya");
        //productRedisRepository.save(product);
        taobao.product.models.redis.Product product = productRedisRepository.findById(1L).get();
        logger.info("product -> {}", product);
    }
}
