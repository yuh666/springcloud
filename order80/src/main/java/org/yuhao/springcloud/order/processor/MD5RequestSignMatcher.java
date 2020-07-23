package org.yuhao.springcloud.order.processor;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.yuhao.springcloud.order.aspect.SignCheck;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

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

        // 比对签名
        HashMap<String, String[]> params = new HashMap<>(request.getParameterMap());
        params.remove(signCheck.signField());
        String newSign = signForHttpServletRequest(params, env.getProperty(signCheck.secretKey()));
        if (!newSign.equals(signResult)) {
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


    public static String signForHttpServletRequest(Map<String, String[]> params, String appSecret) {
        StringBuilder valueSb = new StringBuilder();
        params.put("appSecret", new String[]{appSecret});
        // 将参数以参数名的字典升序排序
        Map<String, String[]> sortParams = new TreeMap<>(params);
        Set<Map.Entry<String, String[]>> entrys = sortParams.entrySet();
        // 遍历排序的字典,并拼接value1+value2......格式
        for (Map.Entry<String, String[]> entry : entrys) {
            if (entry.getValue().length > 0 && org.apache.commons.lang.StringUtils.isNotBlank(
                    entry.getValue()[0])) {
                valueSb.append(entry.getKey() + "=" + entry.getValue()[0] + "&");
            }
        }
        params.remove("appSecret");
        return md5(valueSb.substring(0, valueSb.lastIndexOf("&")));
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

    public static void main(String[] args) {
        HashMap<String, String[]> map = new HashMap<>();
        //usr=i3221625185&imei=&gift_recharging=66&rgt=&desc=天猫&sign=4234234234234234


        map.put("usr", new String[]{"i3221625185"});
        map.put("gift_recharging", new String[]{"66"});
        map.put("desc", new String[]{"天猫"});
        String abc = signForHttpServletRequest(map, "040f22ba9ef949babc79ad3dd8b07f58");
        System.out.println(abc);

        System.out.println(UUID.randomUUID().toString().replace("-",""));
    }

}
