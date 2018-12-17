package com.wolf.handler;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Description: 返回给前端之前对返回值进行过滤再返回
 * 目前仅演示过程至于具体流程是否破坏已有的返回流程，还得再使用时深入研究。
 * <br/> Created on 12/13/2018 2:43 PM
 *
 * @author 李超
 * @since 1.0.0
 */
@Component
public class JsonReturnHandler implements HandlerMethodReturnValueHandler {

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {

        // 设置这个就是最终的处理类了，处理完不再去找下一个类进行处理
        //Whether the request has been handled fully within the handler
        mavContainer.setRequestHandled(true);

        // 获得注解并执行filter方法 最后返回
        HttpServletResponse response = ((ServletWebRequest) webRequest).getResponse();
        Annotation[] annos = returnType.getMethodAnnotations();
        CustomerJsonSerializer jsonSerializer = new CustomerJsonSerializer();
        Arrays.asList(annos).forEach(a -> { // 解析注解，设置过滤条件
            if (a instanceof JSON) {
                JSON json = (JSON) a;
                jsonSerializer.filter(json);
            } else if (a instanceof JSONS) { // 使用多重注解时，实际返回的是 @Repeatable(JSONS.class) 内指定的 @JSONS 注解
                JSONS jsons = (JSONS) a;
                Arrays.asList(jsons.value()).forEach(json -> {
                    jsonSerializer.filter(json);
                });
            }
        });

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String json = jsonSerializer.toJson(returnValue);
        response.getWriter().write(json);
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        JSON methodAnnotation = methodParameter.getMethodAnnotation(JSON.class);
        return methodAnnotation != null;
    }

}