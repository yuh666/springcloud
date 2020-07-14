package org.yuhao.springcloud.order.aspect;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Component
public class SignCheckAspect {

    private static final Logger LOG = LoggerFactory.getLogger(SignCheckAspect.class);


    @Pointcut("@annotation(org.yuhao.springcloud.order.aspect.SignCheck)")
    private void pointCutMethod() {
    }

    @Around("pointCutMethod()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            SignCheck annotation = method.getAnnotation(SignCheck.class);
            if (annotation.checkFields().length == 0 || StringUtils.isBlank(
                    annotation.signField()) || StringUtils.isBlank(annotation.signKey())) {
                return pjp.proceed();
            }

            String[] parameterNames = signature.getParameterNames();
            Object[] args = pjp.getArgs();
            Map<String, Object> paramMap = makePair(parameterNames, args);
            String signField = annotation.signField();

            Object signResult = paramMap.get(signField);
            if (signResult == null) {
                return "sign failed";
            }
            paramMap.remove(signField);

            Map<String, Object> params = extractParams(paramMap, annotation.checkFields());
            if (annotation.skipWhenNull()) {
                params.entrySet().removeIf(next -> next.getValue() == null);
            }
            HashMap<String, String> paramsStrMap = new HashMap<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                paramsStrMap.put(entry.getKey(), entry.getValue().toString());
            }
            String newSign = sign(paramsStrMap, annotation.signKey());
            if (newSign.equals(signResult)) {
                return pjp.proceed();
            }
            return "sign failed";
        } catch (Exception e) {
            //TODO LOG
            return pjp.proceed();
        }
    }


    private static Map<String, Object> extractParams(Map<String, Object> paramMap,
            String[] checkFields) throws NoSuchFieldException, IllegalAccessException {
        HashMap<String, Object> params = new HashMap<>(checkFields.length);
        for (String checkField : checkFields) {
            int dotIndex = checkField.indexOf(".");
            if (dotIndex > -1) {
                extractParamField(checkField.substring(dotIndex + 1),
                        paramMap.get(checkField.substring(0, dotIndex)), params);
            } else {
                params.put(checkField, paramMap.get(checkField));
            }
        }
        return params;
    }


    private static void extractParamField(String name, Object val,
            Map<String, Object> result) throws NoSuchFieldException, IllegalAccessException {
        if (StringUtils.isBlank(name)) {
            result.put(name, val);
            return;
        }
        int dotIndex = name.indexOf(".");
        String nextName = "";
        if (dotIndex == -1) {
            nextName = "";
        } else {
            nextName = name.substring(dotIndex + 1);
        }

        Field field = val.getClass().getField(name);
        field.setAccessible(true);
        extractParamField(nextName, field.get(val), result);
    }

    private static Map<String, Object> makePair(String[] parameterNames, Object[] args) {
        HashMap<String, Object> pair = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            pair.put(parameterNames[i], args[i]);
        }
        return pair;
    }

    /**
     * 方法描述:签名字符串
     *
     * @param params    需要签名的参数
     * @param appSecret 签名密钥
     * @return
     */
    public static String sign(Map<String, String> params, String appSecret) {
        StringBuilder valueSb = new StringBuilder();
        params.put("appSecret", appSecret);
        // 将参数以参数名的字典升序排序
        Map<String, String> sortParams = new TreeMap<String, String>(params);
        Set<Map.Entry<String, String>> entrys = sortParams.entrySet();
        // 遍历排序的字典,并拼接value1+value2......格式
        for (Map.Entry<String, String> entry : entrys) {
            if (org.apache.commons.lang.StringUtils.isNotBlank(entry.getValue())) {
                valueSb.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        params.remove("appSecret");
        return md5(valueSb.substring(0, valueSb.lastIndexOf("&")));
    }

    /**
     * 方法描述:将字符串MD5加码 生成32位md5码
     *
     * @param inStr
     * @return
     */
    public static String md5(String inStr) {
        try {
            return DigestUtils.md5Hex(inStr.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误");
        }
    }

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        String sign = sign(map, "abc");
        System.out.println(sign);
    }

}
