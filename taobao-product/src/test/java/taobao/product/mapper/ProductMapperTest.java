package taobao.product.mapper;

import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import taobao.product.App;
import taobao.product.dto.ProductDetailDto;
import taobao.product.models.Product;
import taobao.product.models.ProductSpecsAttributeKey;
import taobao.product.service.ProductService;
import taobao.product.vo.ProductDetailVo;

import java.util.Date;
import java.util.List;

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

}
