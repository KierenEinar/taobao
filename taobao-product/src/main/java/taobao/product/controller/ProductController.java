package taobao.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taobao.core.model.APIResponse;
import taobao.product.models.Product;
import taobao.product.service.InventoryService;
import taobao.product.service.ProductService;
import taobao.product.vo.ProductCreateAttrsStockWebVo;
import taobao.product.vo.ProductParamsCreateVo;
import taobao.product.vo.ProductTemplateCreateVo;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    InventoryService inventoryService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createProduct (@RequestBody Product product) {
        return ResponseEntity.ok(new APIResponse<>(productService.createProduct(product)));
    }

    @RequestMapping(value = "/attrs", method = RequestMethod.POST)
    public ResponseEntity<?> createProductAttrsAndStock (@RequestBody ProductCreateAttrsStockWebVo productCreateAttrsStockWebVo) {
        return ResponseEntity.ok(new APIResponse(productService.createProductAttrsAndStock(productCreateAttrsStockWebVo)));
    }

    @RequestMapping(value = "/templates", method = RequestMethod.POST)
    public ResponseEntity<?> createProductParamsTemplate (@RequestBody ProductTemplateCreateVo templateCreateVo) {
        productService.createProductParamsTemplate(templateCreateVo);
        return ResponseEntity.ok(new APIResponse<String>("success"));
    }

    @RequestMapping(value = "/params-items", method = RequestMethod.POST)
    public ResponseEntity<?> createProdouctParamsItems (@RequestBody List<ProductParamsCreateVo> productParamsCreateVo) {
        productService.createProdouctParamsItems(productParamsCreateVo);
        return ResponseEntity.ok(new APIResponse<String>("success"));
    }

    @RequestMapping(value = "/release/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> release (@PathVariable Long id) {
        productService.releaseProduct(id);
        return ResponseEntity.ok(new APIResponse<String>("success"));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findDetail (@PathVariable Long id) {
        return ResponseEntity.ok(new APIResponse<>(productService.findProductDetail(id)));
    }


    @RequestMapping(value = "/hbase/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findDetailFromHbase (@PathVariable Long id) {
        return ResponseEntity.ok(new APIResponse<>(productService.findProductDetailFromHbase(id)));
    }

    @RequestMapping(value = "/release/result/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> isProductPersistRedis (@PathVariable Long id) {
        return ResponseEntity.ok(new APIResponse<>(inventoryService.isProductStockPersistRedis(id)));
    }

}
