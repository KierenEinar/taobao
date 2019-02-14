package taobao.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import io.shardingsphere.core.keygen.KeyGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import taobao.product.constant.EventEnum;
import taobao.product.constant.ProductCreateEventEnum;
import taobao.product.constant.ProductParamsEnum;
import taobao.product.constant.ProductStatusEnum;
import taobao.product.dto.IdNameObject;
import taobao.product.exception.ProductEventException;
import taobao.product.mapper.*;
import taobao.product.models.*;
import taobao.product.service.EventLogSevice;
import taobao.product.service.ProductService;
import taobao.product.vo.*;

import javax.annotation.Resource;
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

    @Resource(name = "DefaultKeyGenerator")
    KeyGenerator defaultKeyGenerator;

    @Override
    @Transactional
    public Product createProduct(Product product) {
        product.setCreateTime(new Date());
        product.setProductId(defaultKeyGenerator.generateKey().longValue());
        product.setStatus(ProductStatusEnum.creating.name());
        productMapper.insert(product);
        createEventLog(ProductCreateEventEnum.create_product, ProductCreateEventEnum.none, EventEnum.create_product_event, product.getProductId());
        return product;
    }

    @Override
    @Transactional
    public List<ProductSpecs> createProductAttrsAndStock(ProductCreateAttrsStockWebVo productCreateAttrsStockWebVo) {
        final Map<String, List<String>> attrs = productCreateAttrsStockWebVo.getAttrs();
        final Map<String, ProductSpecsAttributeKey> keyMap = new HashMap<>();
        KeyGenerator keyGenerator = new DefaultKeyGenerator();
        List<ProductSpecsAttributeKey> keys = attrs.keySet().stream().map(i->{
            ProductSpecsAttributeKey productSpecsAttributeKey = new ProductSpecsAttributeKey();
            productSpecsAttributeKey.setId(keyGenerator.generateKey().longValue());
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

        productSpecsAttributeValueMapper.insertBatch(values);

        final List<ProductSkuVo> skus = productCreateAttrsStockWebVo.getSkus();
        List<ProductSpecs> productSpecsList = skus.stream().map(sku->{
            ProductSpecs productSpecs = new ProductSpecs();
            productSpecs.setId(defaultKeyGenerator.generateKey().longValue());
            productSpecs.setProductId(productCreateAttrsStockWebVo.getProductId());
            productSpecs.setAttrs(sortAttrs(sku.getSku()));
            productSpecs.setPrice(sku.getPrice());
            productSpecs.setStock(sku.getStock());
            return productSpecs;
        }).collect(Collectors.toList());

        productSpecsMapper.insertBatch(productSpecsList);

        updateEventLog(ProductCreateEventEnum.create_attrs, ProductCreateEventEnum.create_product, productCreateAttrsStockWebVo.getProductId());

        return productSpecsList;

    }

    @Override
    @Transactional
    public void releaseProduct(Long productId) {
        if (ProductStatusEnum.creating.name().equals(productMapper.selectByPrimaryKey(productId).getStatus())) {
            updateEventLog(ProductCreateEventEnum.release, ProductCreateEventEnum.create_infos, productId);
        }

        int result = productMapper.updateStatusByPreStatusAndProductId(Arrays.asList(ProductStatusEnum.creating.name(), ProductStatusEnum.updating.name()), ProductStatusEnum.sale.name(), productId);
        if (result == 0) throw new ProductEventException("非法请求");
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
    @Transactional
    public void createProdouctParamsItems(List<ProductParamsCreateVo> productParamsCreateVo) {
        List<ProductParamsItem> items = productParamsCreateVo.stream().map(i->{
            ProductParamsItem paramsItem = new ProductParamsItem();
            paramsItem.setProductId(i.getProductId());
            paramsItem.setProductSpecsId(i.getProductSpecsId());
            paramsItem.setType(i.getType());
            paramsItem.setId(defaultKeyGenerator.generateKey().longValue());
            paramsItem.setParamData(JSON.toJSONString(i.getParams()));
            return paramsItem;
        }).collect(Collectors.toList());

        productParamsItemMapper.insertBatch(items);
        String type = productParamsCreateVo.get(0).getType();
        Long productId = productParamsCreateVo.get(0).getProductId();
        if (type.equals(ProductParamsEnum.params.name())) {
            updateEventLog( ProductCreateEventEnum.create_params, ProductCreateEventEnum.create_attrs, productId);
        }
        if (type.equals(ProductParamsEnum.info.name())) {
            updateEventLog( ProductCreateEventEnum.create_infos, ProductCreateEventEnum.create_params, productId);
        }
    }

    @Override
    public void createEventLog(ProductCreateEventEnum status, ProductCreateEventEnum preStatus, EventEnum eventEnum, Long productId) {
        EventLog eventLog = new EventLog();
        eventLog.setEventName(eventEnum.name());
        eventLog.setStatus(status.name());
        eventLog.setPreStatus(preStatus.name());
        eventLog.setProductId(productId);
        eventLogSevice.create(eventLog);
    }

    @Override
    public void updateEventLog(ProductCreateEventEnum status, ProductCreateEventEnum preStatus, Long productId) {
        int result = eventLogMapper.updateStatusByProductIdAndPreStatus(productId, preStatus, status);
        if (result == 0) throw new ProductEventException("非法请求");
    }

    @Override
    public ProductDetailVo findProductDetail(Long productId) {

        ProductDetailVo productDetailVo = new ProductDetailVo();

        Product product = productMapper.selectByPrimaryKey(productId);

        productDetailVo.setProduct(product);

        List<IdNameObject> keys = productSpecsAttributeKeyMapper.selectAttrsByProductId(productId);

        List<IdNameObject> values = productSpecsAttributeValueMapper.selectAttrsByProductId(productId);

        Map<Long, List<String>> valueMap = values.stream().collect(Collectors.groupingBy(IdNameObject::getId, Collectors.mapping(IdNameObject::getName, Collectors.toList())));

        List<ProductDetailVo.AttrValuePair> pairs = Lists.newArrayList();

        for (IdNameObject key : keys) {
            ProductDetailVo.AttrValuePair pair = new ProductDetailVo.AttrValuePair();
            pair.setKey(key.getName());
            List<String> attrValues = valueMap.get(key.getId());
            pair.setValue(attrValues);
            pairs.add(pair);
        }

        productDetailVo.setAttrs(pairs);

        List<ProductSpecs> specs = productSpecsMapper.selectByProductId(productId);

        List<ProductParamsItem> productParamsItems = productParamsItemMapper.selectByProductId(productId);

        Map<Long, List<ProductParamsItem>> itemsMap = productParamsItems.stream().collect(Collectors.groupingBy(ProductParamsItem::getProductSpecsId));

        List<ProductDetailVo.Specs> list = Lists.newArrayList();

        for ( ProductSpecs s : specs) {
            ProductDetailVo.Specs specs1 = new ProductDetailVo.Specs();
            specs1.setKv(new HashMap<>());
            BeanUtils.copyProperties(s, specs1);
            List<ProductParamsItem> items = itemsMap.get(s.getId());
            if (!CollectionUtils.isEmpty(items)) {
                Map<String, Object> kv = specs1.getKv();
                for (ProductParamsItem item : items) {
                    kv.put(item.getType(), JSONObject.parse(item.getParamData()));
                }
            }
            list.add(specs1);
        }

        productDetailVo.setSpecs(list);

        return productDetailVo;
    }


}
