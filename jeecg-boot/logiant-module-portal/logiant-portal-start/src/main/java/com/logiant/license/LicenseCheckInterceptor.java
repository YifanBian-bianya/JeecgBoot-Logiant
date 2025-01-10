package com.logiant.license;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.logiant.common.api.vo.Result;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * LicenseCheckInterceptor
 *
 * @author zifangsky
 * @date 2018/4/25
 * @since 1.0.0
 */
public class LicenseCheckInterceptor extends HandlerInterceptorAdapter{
    private static Logger logger = LogManager.getLogger(LicenseCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LicenseVerify licenseVerify = new LicenseVerify();

        //校验证书是否有效
        boolean verifyResult = licenseVerify.verify();

        if(verifyResult){
            return true;
        }else{
            // 证书无效，拦截请求并返回错误信息

            // 设置响应内容类型为 JSON
            response.setContentType("application/json;charset=UTF-8");

            // 创建返回的错误信息，使用 Result 类来封装
            Result<Map<String, String>> result = Result.error("您的证书无效，请核查服务器是否取得授权或重新申请证书！", null);

            // 返回 JSON 格式的响应
            response.getWriter().write(JSON.toJSONString(result));

            // 返回 false，阻止请求继续
            return false;
        }
    }

}
