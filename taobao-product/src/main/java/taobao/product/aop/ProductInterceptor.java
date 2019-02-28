package taobao.product.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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



    @AfterReturning(value = "pointcut()", returning = "returnValue")
    public Object afterReturning (JoinPoint joinPoint, Object returnValue) {
        if (Objects.nonNull(returnValue)) {
            ProductDetailVo productDetailVo = (ProductDetailVo)returnValue;
            productService.setStocks(productDetailVo);
        }
        return returnValue;
    }


}
