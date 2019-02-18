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
    ProductDetailVo findProductDetail(Long productId);
    void createProduct2Hbase(Long productId);
    ProductDetailVo findProductDetailFromHbase(Long productId);

}
