package taobao.product.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import taobao.product.service.ProductService;
import taobao.product.vo.ProductDetailVo;

import java.util.Objects;

@Aspect
@Component
public class ProductInterceptor {

    @Pointcut("execution (* taobao.product.service.ProductService.findProductDetail(..))")
    public void pointcut(){};

    @Autowired
    ProductService productService;

    Logger logger = LoggerFactory.getLogger(ProductInterceptor.class);

    @Around(value = "pointcut()")
    public Object around (ProceedingJoinPoint proceedingJoinPoint) {
       Object value = null;
        try {
            Object args[] = proceedingJoinPoint.getArgs();
            if (args.length<0) return null;

            Long productId = (Long)args[0];
            logger.info("productId -> {}", productId);
            if (productService.isNotExistsProduct(productId)) {
                logger.info("非法请求, 不存在该商品, {}", productId);
                return null;
            }
            value = proceedingJoinPoint.proceed();
            if (Objects.nonNull(value)) {
                ProductDetailVo productDetailVo = (ProductDetailVo)value;
                productService.setStocks(productDetailVo);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return value;
    }


    //@AfterReturning(value = "pointcut()", returning = "returnValue")
    public Object afterReturning (JoinPoint joinPoint, Object returnValue) {
        if (Objects.nonNull(returnValue)) {
            ProductDetailVo productDetailVo = (ProductDetailVo)returnValue;
            productService.setStocks(productDetailVo);
        }
        return returnValue;
    }


}
