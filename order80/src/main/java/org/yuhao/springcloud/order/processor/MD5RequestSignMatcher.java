package org.yuhao.springcloud.order.processor;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.yuhao.springcloud.order.aspect.SignCheck;
import org.yuhao.springcloud.order.aspect.SignCheckAspect;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * MD5签名校验
 *
 * @author yuhao
 * @date 2020/7/15 6:14 下午
 */
public class MD5RequestSignMatcher implements RequestSignMatcher {

    private static final Logger LOG = LoggerFactory.getLogger(MD5RequestSignMatcher.class);

    private Environment env;

    public MD5RequestSignMatcher(Environment env) {
        this.env = env;
    }

    @Override
    public boolean match(HttpServletRequest request, SignCheck signCheck) {
        if (!checkEnv(signCheck)) {
            // 环境不满足不拦截
            return true;
        }

        // 取调用方签名结果
        String signResult = request.getParameter(signCheck.signField());
        if (signResult == null) {
            LOG.error("sign check, request signResult is null!");
            return false;
        }

        // 取校验字段
        String[] checkFields = signCheck.checkFields();
        HashMap<String, String> params = new HashMap<>(checkFields.length);
        for (String checkField : checkFields) {
            String parameter = request.getParameter(checkField);
            if (StringUtils.isNotBlank(parameter)) {
                params.put(checkField, parameter);
            }
        }

        // 比对签名
        String newSign = sign(params, env.getProperty(signCheck.secretKey()));
        if(!newSign.equals(signResult)){
            LOG.error("sign check not match,request:{}, real:{}", signResult, newSign);
            return false;
        }

        return true;
    }

    /**
     * 校验加密参数
     *
     * @param annotation 加密参数注解
     * @return 是否可用
     */
    private boolean checkEnv(SignCheck annotation) {
        if (StringUtils.isBlank(annotation.secretKey())) {
            LOG.warn("sign check,secretKey not exist,skipped!");
            return false;
        }
        if (annotation.checkFields().length == 0) {
            LOG.warn("sign check,checkFields is Empty,skipped!");
            return false;
        }
        if (StringUtils.isBlank(annotation.signField())) {
            LOG.warn("sign check,signField not exist,skipped!");
            return false;
        }
        String secretProperty = env.getProperty(annotation.secretKey());
        if (StringUtils.isBlank(secretProperty)) {
            LOG.warn("sign check,secretProperty not exist,skipped!");
            return false;
        }
        return true;
    }


    /**
     * 将请求参数进行签名
     * @param params 签名参数
     * @param appSecret 秘钥
     * @return 签名结果
     */
    public static String sign(Map<String, String> params, String appSecret) {
        // 将参数以参数名的字典升序排序
        Map<String, String> sortParams = new TreeMap<>(params);
        sortParams.put("appSecret", appSecret);
        // k1=v1&k2=v2&..
        String finalRes = sortParams.entrySet().stream()
                .filter(entry -> StringUtils.isNotBlank(entry.getValue()))
                .reduce("", (res, entry) -> String.format("%s%s=%s&", res, entry.getKey(),
                        entry.getValue()), String::concat);
        System.out.println(finalRes);
        // 去掉最后一个&
        return md5(finalRes.substring(0, finalRes.length() - 1));
    }

    /**
     * 将字符串MD5生成摘要 生成32位md5码
     *
     * @param inStr 输入串
     * @return 摘要信息
     */
    public static String md5(String inStr) {
        try {
            return DigestUtils.md5Hex(inStr.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5摘要过程中出现错误");
        }
    }

}
