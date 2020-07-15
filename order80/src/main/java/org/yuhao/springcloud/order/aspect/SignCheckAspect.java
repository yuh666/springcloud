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

/**
 * 签名校验切面
 * 提取注解指定的参数进行MD5，和请求中的签名结果进行比对
 * {@link org.yuhao.springcloud.order.aspect.SignCheck}
 *
 * @author zy-user
 */
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
            // 提取Method
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            // 提取注解
            SignCheck annotation = method.getAnnotation(SignCheck.class);
            // 如果没有配置校验字段或者没有秘钥，直接跳过检测
            if (annotation.checkFields().length == 0 || StringUtils.isBlank(
                    annotation.signField()) || StringUtils.isBlank(annotation.signKey())) {
                return pjp.proceed();
            }
            // 取原始参数
            String[] parameterNames = signature.getParameterNames();
            Object[] args = pjp.getArgs();
            Map<String, Object> paramMap = makePair(parameterNames, args);
            String signField = annotation.signField();

            // 检查传入的签名结果
            Object signResult = paramMap.get(signField);
            if (signResult == null) {
                return "sign failed";
            }
            paramMap.remove(signField);

            // 取出最终的的参数
            Map<String, Object> params = extractParams(paramMap, annotation.checkFields());
            if (annotation.skipWhenNull()) {
                // 过滤掉空的
                params.entrySet().removeIf(next -> next.getValue() == null);
            }
            //转成string
            HashMap<String, String> paramsStrMap = new HashMap<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                paramsStrMap.put(entry.getKey(), entry.getValue().toString());
            }
            // 签名
            String newSign = sign(paramsStrMap, annotation.signKey());
            if (newSign.equals(signResult)) {
                //对比
                return pjp.proceed();
            }
            return "sign failed";
        } catch (Exception e) {
            //TODO LOG
            // 异常直接放过去
            return pjp.proceed();
        }
    }

    /**
     * 根据传入的校验字段名字取出最终的值
     *
     * @param paramMap    原始实参
     * @param checkFields 校验字段
     * @return 最终校验字段的值
     * @throws NoSuchFieldException   反射异常
     * @throws IllegalAccessException 反射异常
     */
    private static Map<String, Object> extractParams(Map<String, Object> paramMap,
                                                     String[] checkFields) throws NoSuchFieldException, IllegalAccessException {
        HashMap<String, Object> params = new HashMap<>(checkFields.length);
        for (String checkField : checkFields) {
            int dotIndex = checkField.indexOf(".");
            if (dotIndex > -1) {
                // 多级
                extractParamField(checkField, paramMap.get(checkField.substring(0, dotIndex)), params);
            } else {
                // 单级
                extractParamField(checkField, paramMap.get(checkField), params);
            }
        }
        return params;
    }


    /**
     * 递归处理参数
     *
     * @param name   参数名
     * @param val    参数值
     * @param result 结果
     * @throws NoSuchFieldException   反射异常
     * @throws IllegalAccessException 反射异常
     */
    private static void extractParamField(String name, Object val,
                                          Map<String, Object> result) throws NoSuchFieldException, IllegalAccessException {
        int dotIndex = name.indexOf(".");
        if (dotIndex == -1) {
            // 最后一级
            result.put(name, val);
            return;
        }

        int nextDotIndex = name.indexOf(".", dotIndex + 1);
        if (nextDotIndex == -1) {
            nextDotIndex = name.length();
        }
        // 取出下一级
        String filedName = name.substring(dotIndex + 1, nextDotIndex);
        Field field = val.getClass().getDeclaredField(filedName);
        field.setAccessible(true);
        extractParamField(name.substring(dotIndex + 1), field.get(val), result);
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
