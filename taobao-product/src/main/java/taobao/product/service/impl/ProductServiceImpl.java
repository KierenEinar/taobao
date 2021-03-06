package taobao.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import io.shardingsphere.core.keygen.KeyGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import taobao.core.RedisService;
import taobao.core.constant.Constant;
import taobao.core.vo.InventoryWebVo;
import taobao.hbase.service.HbaseService;
import taobao.localmq.core.LocalMQSendCallback;
import taobao.localmq.core.LocalMQTemplate;
import taobao.product.cache.ProductCacheContainer;
import taobao.product.constant.*;
import taobao.product.dto.IdNameObject;
import taobao.product.exception.ProductEventException;
import taobao.product.mapper.*;
import taobao.product.models.*;
import taobao.product.service.EventLogSevice;
import taobao.product.service.InventoryService;
import taobao.product.service.ProducerService;
import taobao.product.service.ProductService;
import taobao.product.vo.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    HbaseService hbaseService;

    @Autowired
    RedisService redisService;

    @Autowired
    ProducerService producerService;

    @Autowired
    ProductCacheContainer productCacheContainer;

    @Autowired
    LocalMQTemplate localMQTemplate;

    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    InventoryService inventoryService;

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
        List<ProductSpecsAttributeKey> keys = attrs.keySet().stream().map(i -> {
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
            for (String v : entrys.getValue()) {
                ProductSpecsAttributeValue value = new ProductSpecsAttributeValue();
                value.setProductId(productCreateAttrsStockWebVo.getProductId());
                value.setAttrId(attrId);
                value.setValue(v);
                values.add(value);
            }
        }

        productSpecsAttributeValueMapper.insertBatch(values);

        final List<ProductSkuVo> skus = productCreateAttrsStockWebVo.getSkus();
        List<ProductSpecs> productSpecsList = skus.stream().map(sku -> {
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
        producerService.sendCreateProductMQInTransaction(productId);
    }

    @Override
    @Transactional
    public void releaseProduct2DB (Long productId) {
        String status = productMapper.selectByPrimaryKey(productId).getStatus();
        int result = productMapper.updateStatusByPreStatusAndProductId(Arrays.asList(ProductStatusEnum.creating.name(), ProductStatusEnum.updating.name()), ProductStatusEnum.sale.name(), productId);
        if (result == 0) throw new ProductEventException("非法请求");
        if (ProductStatusEnum.creating.name().equals(status)) {
            updateEventLog(ProductCreateEventEnum.release, ProductCreateEventEnum.create_infos, productId);
        }
    }

    @Override
    public List<ProductSpecs> findSpecsByProductId(Long productId) {
        return productSpecsMapper.selectByProductId(productId);
    }

    private String sortAttrs(String attrs) {
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
        List<ProductParamsItem> items = productParamsCreateVo.stream().map(i -> {
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
            updateEventLog(ProductCreateEventEnum.create_params, ProductCreateEventEnum.create_attrs, productId);
        }
        if (type.equals(ProductParamsEnum.info.name())) {
            updateEventLog(ProductCreateEventEnum.create_infos, ProductCreateEventEnum.create_params, productId);
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
    public ProductDetailVo findProductDetailFromDB(Long productId) {

        ProductDetailVo productDetailVo = new ProductDetailVo();

        Product product = productMapper.selectByPrimaryKey(productId);

        if (Objects.isNull(product)) {
            return null;
        }

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

        for (ProductSpecs s : specs) {
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

    @Override
    public ProductDetailVo findProductDetail(Long productId) {
        ProductDetailVo productDetailVoCache = findProductDetailFromCache(productId);
        logger.info("find product from cahce -> {}", productDetailVoCache);
        if (Objects.nonNull(productDetailVoCache)) return productDetailVoCache;
        ProductDetailVo productDetailVo = findProductDetailFromRedis(productId);
        if (Objects.isNull(productDetailVo)) productDetailVo = sendProductCreate2RedisMessage(productId);



        logger.info("find product from redis -> {}", productDetailVo);
        if (Objects.isNull(productDetailVoCache) && Objects.nonNull(productDetailVo) && Objects.nonNull(productDetailVo.getProduct())) {
            localMQTemplate.sendAsync(Constant.Topic.product_create_local_cache_topic, productDetailVo, new LocalMQSendCallback() {
                @Override
                public void onSuccess(String msgId) {
                    logger.info("send local mq success ... ");
                }
                @Override
                public void onException(Exception e) {
                    logger.error("send local mq error ... ");
                }
            });
        }
        return productDetailVo;
    }

    @Override
    public void createProduct2Hbase(Long productId, ProductDetailVo productDetailVo) {
        String table = HbaseConstant.Product.table;
        String rowKey = productId + "";
        List<Put> puts = Lists.newArrayList();
        puts.addAll(ColumnFamilyInfoPuts(rowKey, productDetailVo));
        puts.addAll(ColumnFamilyAttrPuts(rowKey, productDetailVo));
        puts.addAll(ColumnFamilySpecsPuts(rowKey, productDetailVo));
        hbaseService.put(table, rowKey, puts);
    }

    /**
     * 120分钟缓存有效期
     */
    @Override
    public void createProduct2Redis(Long productId, ProductDetailVo productDetailVo) {
        String key = RedisPrefix.productDetailByKey(productId);
        redisService.set(key, JSONObject.toJSONString(productDetailVo), 120, TimeUnit.MINUTES);
    }

    @Override
    public ProductDetailVo findProductDetailFromHbase(Long productId) {

        Map<String, Map<String, String>> map = hbaseService.get(HbaseConstant.Product.table, productId + "");

        if (CollectionUtils.isEmpty(map)) return null;

        Map<String, String> specsMap = map.get(HbaseConstant.Product.specs);

        Map<String, Map<String, String>> mapList = Maps.newConcurrentMap();

        for (Map.Entry<String, String> entry : specsMap.entrySet()) {
            String k[] = entry.getKey().split("-");
            String group = k[k.length - 1];
            String key = entry.getKey().substring(0, entry.getKey().lastIndexOf("-"));
            if (!mapList.containsKey(group)) mapList.put(group, Maps.newConcurrentMap());
            Map<String, String> list = mapList.get(group);
            list.put(key, entry.getValue());
        }

        ProductDetailVo productDetailVo = new ProductDetailVo();
        Map<String, String> info = map.get(HbaseConstant.Product.Info);
        Map<String, String> attrs = map.get(HbaseConstant.Product.attrs);

        Product product = new Product();
        product.setProductId(productId);
        product.setTitle(info.get(HbaseConstant.Product.CFInfo.title));
        product.setHtml(info.get(HbaseConstant.Product.CFInfo.html));
        product.setName(info.get(HbaseConstant.Product.CFInfo.name));
        productDetailVo.setProduct(product);

        List<ProductDetailVo.AttrValuePair> attrsList = Lists.newArrayList();
        productDetailVo.setAttrs(attrsList);
        for (Map.Entry<String, String> entry : attrs.entrySet()) {
            String key = entry.getKey();
            ProductDetailVo.AttrValuePair pair = new ProductDetailVo.AttrValuePair();
            pair.setKey(key);
            pair.setValue(Arrays.asList(entry.getValue().split(",")));
            attrsList.add(pair);
        }

        List<ProductDetailVo.Specs> specsList = Lists.newArrayList();

        productDetailVo.setSpecs(specsList);

        for (Map.Entry<String, Map<String, String>> entry : mapList.entrySet()) {
            Map<String, String> v = entry.getValue();
            ProductDetailVo.Specs specs = new ProductDetailVo.Specs();
            specs.setPrice(new BigDecimal(v.get(HbaseConstant.Product.CFSpecs.price)));
            specs.setAttrs(v.get(HbaseConstant.Product.CFSpecs.attrs));
            specs.setId(Long.parseLong(entry.getKey()));
            Map<String, Object> infoMap = Maps.newConcurrentMap();
            if (v.containsKey("info")) infoMap.put("info", JSONObject.parse(v.get("info")));
            if (v.containsKey("params")) infoMap.put("params", JSONObject.parse(v.get("params")));
            specs.setKv(infoMap);
            specsList.add(specs);
        }

        return productDetailVo;
    }

    @Override
    public ProductDetailVo findProductDetailFromRedis(Long productId) {
        String value = redisService.get(RedisPrefix.productDetailByKey(productId));
        if (StringUtils.isBlank(value)) return null;
        ProductDetailVo productDetailVo = null;
        try {
            productDetailVo = JSONObject.parseObject(value, ProductDetailVo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productDetailVo;
    }

    @Override
    public void setStocks (ProductDetailVo productDetailVo) {
        if (Objects.isNull(productDetailVo) || Objects.isNull(productDetailVo.getProduct())) return;
        List<Integer> stocks = inventoryService.getStocks(productDetailVo.getProduct().getProductId(), productDetailVo.getSpecs());
        for (int i=0; i<productDetailVo.getSpecs().size(); i++) productDetailVo.getSpecs().get(i).setStock(stocks.get(i));
        logger.info("stocks -> {}", stocks);
    }

    @Override
    public Boolean isNotExistsProduct(Long productId) {
        return redisService.mightContainsByBloomFilter(RedisPrefix.productBloomFilterKey, productId+"");
    }

    @Override
    public void putNotExistsProductByBloomFilter(Long productId) {
        redisService.putByBloomFilter(RedisPrefix.productBloomFilterKey, productId+"");
    }

    @Override
    public List<ProductSpecs> findSpeces(List<InventoryWebVo> vos) {
        List<ProductSpecs> productSpecs = Lists.newArrayList();
        for (InventoryWebVo vo : vos) {
            productSpecs.add(productSpecsMapper.selectBySpeces(vo));
        }
        return productSpecs;
    }


    @Override
    public ProductDetailVo sendProductCreate2RedisMessage(Long productId) {
        int retryCount = 0;

        while (Boolean.TRUE) {
            if (retryCount == 50) break;
            if (isNotExistsProduct(productId)) return null;
            ProductDetailVo productDetailVo = findProductDetailFromRedis(productId);
            if (Objects.nonNull(productDetailVo)) return productDetailVo;
            Boolean result = redisService.setNX(RedisPrefix.productExistsKey(productId), new Date().getTime() + "", 10, TimeUnit.SECONDS);
            if (Boolean.TRUE.equals(result)) {
                producerService.sendCreateProdcut2CacheMessage(productId);
            } else {
                Integer random = new Random().nextInt(200);
                try {
                    Thread.sleep(200L + random);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                productDetailVo = findProductDetailFromCache(productId);
                if (Objects.nonNull(productDetailVo)) return productDetailVo;
                productDetailVo = findProductDetailFromRedis(productId);
                if (Objects.nonNull(productDetailVo)) {
                    return productDetailVo;
                }
            }
            ++retryCount;
            logger.info("retryCount -> {}", retryCount);
        }
        return null;
    }

    private ProductDetailVo findProductDetailFromCache(Long productId) {
        return productCacheContainer.getProductCache(productId);
    }

    @Override
    public Boolean isProductDetailExistsFromRedis(Long productId) {
        return redisService.exists(RedisPrefix.productDetailByKey(productId));
    }

    private List<Put> ColumnFamilyInfoPuts(String rowKey, ProductDetailVo productDetailVo) {

        String columnFamily = HbaseConstant.Product.Info;
        Product product = productDetailVo.getProduct();
        byte[] cf = Bytes.toBytes(columnFamily);
        Put put1 = new Put(Bytes.toBytes(rowKey));
        put1.addColumn(cf, Bytes.toBytes(HbaseConstant.Product.CFInfo.title), Bytes.toBytes(product.getTitle()));

        Put put2 = new Put(Bytes.toBytes(rowKey));
        put2.addColumn(cf, Bytes.toBytes(HbaseConstant.Product.CFInfo.name), Bytes.toBytes(product.getName()));

        Put put3 = new Put(Bytes.toBytes(rowKey));
        put3.addColumn(cf, Bytes.toBytes(HbaseConstant.Product.CFInfo.html), Bytes.toBytes(product.getHtml()));

        Put put4 = new Put(Bytes.toBytes(rowKey));
        put3.addColumn(cf, Bytes.toBytes(HbaseConstant.Product.CFInfo.status), Bytes.toBytes(product.getStatus()));

        return Lists.newArrayList(put1, put2, put3, put4);
    }

    private List<Put> ColumnFamilyAttrPuts(String rowKey, ProductDetailVo productDetailVo) {
        String columnFamily = HbaseConstant.Product.attrs;
        List<ProductDetailVo.AttrValuePair> attrs = productDetailVo.getAttrs();
        byte[] row = Bytes.toBytes(rowKey);
        return attrs.stream().map(attr -> {
            byte[] attrKey = Bytes.toBytes(attr.getKey());
            byte[] attrValue = Bytes.toBytes(attr.getValue().stream().collect(Collectors.joining(",")));
            Put put = new Put(row);
            put.addColumn(Bytes.toBytes(columnFamily), attrKey, attrValue);
            return put;
        }).collect(Collectors.toList());
    }

    private List<Put> ColumnFamilySpecsPuts(String rowKey, ProductDetailVo productDetailVo) {

        List<ProductDetailVo.Specs> specsList = productDetailVo.getSpecs();
        byte[] row = Bytes.toBytes(rowKey);
        List<Put> puts = Lists.newArrayList();
        for (ProductDetailVo.Specs specs : specsList) {

            String columnFamily = HbaseConstant.Product.specs;
            Put put1 = new Put(row);
            put1.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(HbaseConstant.Product.CFSpecs.attrs + "-" + specs.getId()), Bytes.toBytes(specs.getAttrs()));

            Put put2 = new Put(row);
            put2.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(HbaseConstant.Product.CFSpecs.price + "-" + specs.getId()), Bytes.toBytes(specs.getPrice() + ""));

            puts.add(put1);
            puts.add(put2);
            Map<String, Object> kv = specs.getKv();

            for (Map.Entry<String, Object> entry : kv.entrySet()) {
                Put put4 = new Put(row);
                put4.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(entry.getKey() + "-" + specs.getId()), Bytes.toBytes(JSONObject.toJSONString(entry.getValue())));
                puts.add(put4);
            }

        }

        return puts;

    }


}
