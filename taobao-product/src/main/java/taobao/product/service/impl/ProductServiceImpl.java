package taobao.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taobao.product.constant.EventEnum;
import taobao.product.constant.ProductCreateEventEnum;
import taobao.product.exception.ProductEventException;
import taobao.product.mapper.*;
import taobao.product.models.*;
import taobao.product.service.EventLogSevice;
import taobao.product.service.ProductService;
import taobao.product.vo.ProductCreateAttrsStockWebVo;
import taobao.product.vo.ProductParamsCreateVo;
import taobao.product.vo.ProductSkuVo;
import taobao.product.vo.ProductTemplateCreateVo;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    EventLogSevice eventLogSevice;

    @Autowired
    EventLogMapper eventLogMapper;

    @Autowired
    ProductSpecsAttributeKeyMapper productSpecsAttributeKeyMapper;

    @Autowired
    ProductSpecsAttributeValueMapper productSpecsAttributeValueMapper;

    @Autowired
    ProductParamsTemplateMapper productParamsTemplateMapper;

    @Autowired
    ProductParamsItemMapper productParamsItemMapper;

    @Autowired
    ProductSpecsMapper productSpecsMapper;

    @Override
    @Transactional
    public void createProduct(Product product) {
        productMapper.insert(product);
        createEventLog(ProductCreateEventEnum.create_product, ProductCreateEventEnum.none, EventEnum.create_product_event);
    }

    @Override
    @Transactional
    public List<ProductSpecs> createProductAttrsAndStock(ProductCreateAttrsStockWebVo productCreateAttrsStockWebVo) {
        final Map<String, List<String>> attrs = productCreateAttrsStockWebVo.getAttrs();
        final Map<String, ProductSpecsAttributeKey> keyMap = new HashMap<>();

        List<ProductSpecsAttributeKey> keys = attrs.keySet().stream().map(i->{
            ProductSpecsAttributeKey productSpecsAttributeKey = new ProductSpecsAttributeKey();
            productSpecsAttributeKey.setId(new DefaultKeyGenerator().generateKey().longValue());
            productSpecsAttributeKey.setName(i);
            productSpecsAttributeKey.setProductId(productCreateAttrsStockWebVo.getProductId());
            keyMap.put(i, productSpecsAttributeKey);
            return productSpecsAttributeKey;
        }).collect(Collectors.toList());
        productSpecsAttributeKeyMapper.insertBatch(keys);

        final List<ProductSpecsAttributeValue> values = new ArrayList<>();

        for (Map.Entry<String, List<String>> entrys : attrs.entrySet()) {
            Long attrId = keyMap.get(entrys.getKey()).getId();
            for (String v: entrys.getValue()) {
                ProductSpecsAttributeValue value = new ProductSpecsAttributeValue();
                value.setProductId(productCreateAttrsStockWebVo.getProductId());
                value.setAttrId(attrId);
                value.setValue(v);
                values.add(value);
            }
        }

        productSpecsAttributeValueMapper.insertBatch(keys);

        final List<ProductSkuVo> skus = productCreateAttrsStockWebVo.getSkus();
        List<ProductSpecs> productSpecsList = skus.stream().map(sku->{
            ProductSpecs productSpecs = new ProductSpecs();
            productSpecs.setId(new DefaultKeyGenerator().generateKey().longValue());
            productSpecs.setProductId(productCreateAttrsStockWebVo.getProductId());
            productSpecs.setAttrs(sortAttrs(sku.getSku()));
            productSpecs.setPrice(sku.getPrice());
            productSpecs.setStock(sku.getStock());
            return productSpecs;
        }).collect(Collectors.toList());

        productSpecsMapper.insertBatch(productSpecsList);

        updateEventLog(ProductCreateEventEnum.create_product, ProductCreateEventEnum.none, productCreateAttrsStockWebVo.getProductId());

        return productSpecsList;

    }

    private String sortAttrs (String attrs) {
        return Arrays.asList(attrs.split("-")).stream().sorted().collect(Collectors.joining("-"));
    }

    @Override
    public void createProductParamsTemplate(ProductTemplateCreateVo templateCreateVo) {
        String tempalteJson = JSON.toJSONString(templateCreateVo.getTemplate());
        ProductParamsTemplate template = new ProductParamsTemplate();
        template.setProductId(templateCreateVo.getProductId());
        template.setType(templateCreateVo.getType());
        template.setTemplateJson(tempalteJson);
        productParamsTemplateMapper.insertSelective(template);
    }


    @Override
    public void createProdouctParamsItems(List<ProductParamsCreateVo> productParamsCreateVo) {
        List<ProductParamsItem> items = productParamsCreateVo.stream().map(i->{
            ProductParamsItem paramsItem = new ProductParamsItem();
            paramsItem.setProductId(i.getProductId());
            paramsItem.setProductSpecsId(i.getProductSpecsId());
            paramsItem.setType(i.getType());
            paramsItem.setId(new DefaultKeyGenerator().generateKey().longValue());
            paramsItem.setParamData(JSON.toJSONString(i.getParams()));
            return paramsItem;
        }).collect(Collectors.toList());

        productParamsItemMapper.insertBatch(items);

    }

    @Override
    public void createEventLog(ProductCreateEventEnum status, ProductCreateEventEnum preStatus, EventEnum eventEnum) {
        EventLog eventLog = new EventLog();
        eventLog.setEventName(eventEnum.name());
        eventLog.setStatus(status.name());
        eventLog.setPreStatus(preStatus.name());
        eventLogSevice.create(eventLog);
    }

    @Override
    public void updateEventLog(ProductCreateEventEnum status, ProductCreateEventEnum preStatus, Long productId) {
        int result = eventLogMapper.updateStatusByProductIdAndPreStatus(productId, preStatus, status);
        if (result == 0) throw new ProductEventException("非法请求");
    }


}
