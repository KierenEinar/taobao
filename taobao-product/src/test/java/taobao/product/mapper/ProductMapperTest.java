package taobao.product.mapper;

import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import taobao.product.App;
import taobao.product.models.Product;
import taobao.product.models.ProductSpecsAttributeKey;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class ProductMapperTest {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductSpecsAttributeKeyMapper productSpecsAttributeKeyMapper;

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

}
