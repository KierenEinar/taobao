package taobao.product.service;

import taobao.product.constant.EventEnum;
import taobao.product.constant.ProductCreateEventEnum;
import taobao.product.models.Product;
import taobao.product.models.ProductSpecs;
import taobao.product.vo.ProductCreateAttrsStockWebVo;
import taobao.product.vo.ProductDetailVo;
import taobao.product.vo.ProductParamsCreateVo;
import taobao.product.vo.ProductTemplateCreateVo;


import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    List<ProductSpecs> createProductAttrsAndStock(ProductCreateAttrsStockWebVo productCreateAttrsStockWebVo);
    void releaseProduct(Long productId);
    void createProductParamsTemplate(ProductTemplateCreateVo templateCreateVo);
    void createProdouctParamsItems(List<ProductParamsCreateVo> productParamsCreateVo);
    void createEventLog(ProductCreateEventEnum status, ProductCreateEventEnum preStatus, EventEnum eventEnum,Long productId);
    void updateEventLog(ProductCreateEventEnum status, ProductCreateEventEnum preStatus, Long productId);
    void createProduct2Hbase(Long productId, ProductDetailVo productDetailVo);
    void createProduct2Redis(Long productId, ProductDetailVo productDetailVo);
    ProductDetailVo findProductDetailFromHbase(Long productId);
    ProductDetailVo findProductDetailFromRedis(Long productId);
    ProductDetailVo findProductDetailFromDB(Long productId);
    ProductDetailVo findProductDetail (Long productId);
    ProductDetailVo sendProductCreate2RedisMessage(Long productId);
    Boolean isProductDetailExistsFromRedis(Long productId);
    void releaseProduct2DB(Long productId);
    List<ProductSpecs> findSpecsByProductId(Long productId);
    void setStocks (ProductDetailVo productDetailVo);
}
