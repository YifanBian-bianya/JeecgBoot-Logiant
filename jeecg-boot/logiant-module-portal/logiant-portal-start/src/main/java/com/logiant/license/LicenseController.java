package com.logiant.license;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.logiant.modules.license.LicenseCreatorParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/license")
public class LicenseController {

    private static final Logger logger = LogManager.getLogger(LicenseController.class);

    /**
     * 安装证书
     *
     * @return 安装结果
     */
    @PostMapping(value = "/install", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> installLicense(@RequestBody String licenseCode) {
        Map<String, Object> response = new HashMap<>();

        try {
            LicenseVerify licenseVerify = new LicenseVerify();

            // 调用安装方法
            logger.info("开始安装证书...");
            var result = licenseVerify.installWithLicenseCode(licenseCode);

            if (result != null) {
                // 格式化证书有效期
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String validFrom = format.format(result.getNotBefore());
                String validTo = format.format(result.getNotAfter());

                // 安装成功的响应
                response.put("status", "success");
                response.put("message", "证书安装成功！");
                response.put("validFrom", validFrom);
                response.put("validTo", validTo);
            } else {
                // 安装失败的响应
                response.put("status", "error");
                response.put("message", "证书安装失败，请检查授权码或路径！");
            }
        } catch (Exception e) {
            logger.error("证书安装异常", e);
            response.put("status", "error");
            response.put("message", "证书安装异常: " + e.getMessage());
        }
        return response;
    }

    /**
     * 申请证书
     *
     http://localhost:8080/jeecg-boot/license/requestLicense 的测试用请求体
     
     {
	"subject": "license_demo",
    "issuedTime": "2022-04-10 00:00:01",
	"expiryTime": "2030-12-31 15:45:00",
	"consumerType": "User",
	"consumerAmount": 1,
	"description": "这是证书描述信息"
 }

     * 
     */
    @PostMapping(value = "/requestLicense", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> requestLicense(@RequestBody(required = true) LicenseCreatorParam param) throws Exception {
        Map<String, Object> response = new HashMap<>();
        LicenseClient licenseClient = new LicenseClient();
        response.put("message", licenseClient.sendRequest(param));
        return response;
    }
}
