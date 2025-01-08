package org.jeecg.modules.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.entity.LicenseHistory;
import org.jeecg.modules.license.*;
import org.jeecg.modules.service.ILicenseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 用于生成证书文件，不能放在给客户部署的代码里
 * @author zifangsky
 * @date 2018/4/26
 * @since 1.0.0
 */
@RestController
@RequestMapping("/license")
public class LicenseCreatorController {

    /**
     * 证书默认生成路径
     */
    @Value("${license.licenseGeneratePath}")
    private String licenseGeneratePath;

    @Value("${license.storePass}")
    private String storePassword;

    @Value("${license.defaultSubject}")
    private String licenseSubject;

    @Value("${license.privateAlias}")
    private String licensePrivateAlias;

    @Value("${license.privateKeysStorePath}")
    private String licensePrivateKeysStorePath;

    @Value("${license.keyPass}")
    private String licenseKeyPass;

    private final ILicenseHistoryService licenseHistoryService;

    @Autowired
    public LicenseCreatorController(ILicenseHistoryService licenseHistoryService) {
        this.licenseHistoryService = licenseHistoryService;
    }

    private static final String AES_KEY = "your_secure_key"; // 固定的AES密钥

    /**
     * 获取服务器硬件信息
     * @author zifangsky
     * @date 2018/4/26 13:13
     * @since 1.0.0
     * @param osName 操作系统类型，如果为空则自动判断
     * @return com.ccx.models.license.LicenseCheckModel
     */
    @RequestMapping(value = "/getServerInfos",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public LicenseCheckModel getServerInfos(@RequestParam(value = "osName",required = false) String osName) {
        //操作系统类型
        if(StringUtils.isBlank(osName)){
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();

        AbstractServerInfos abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        }else{//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }

        return abstractServerInfos.getServerInfos();
    }

    /**
     * 生成证书
     * @param param 生成证书需要的参数，示例：
     *
     * {
     * 	"subject": "license_demo",
     * 	"privateAlias": "privateKey",
     * 	"keyPass": "private_password1234",
     * 	"storePass": "public_password1234",
     * 	"licensePath": "C:/Users/26232/Desktop/LicenseDemo/license.lic",
     * 	"privateKeysStorePath": "C:/Users/26232/Desktop/LicenseDemo/privateKeys.keystore",
     * 	"issuedTime": "2022-04-10 00:00:01",
     * 	"expiryTime": "2030-12-31 23:59:59",
     * 	"consumerType": "User",
     * 	"consumerAmount": 1,
     * 	"description": "这是证书描述信息",
     * 	"licenseCheckModel": {
     * 		"ipAddress": [],
     * 		"macAddress": [],
     * 		"cpuSerial": "",
     * 		"mainBoardSerial": ""
     * 	    }
     * }
     *
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @RequestMapping(value = "/generateLicense",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Map<String,Object> generateLicense(@RequestBody(required = true) LicenseCreatorParam param) {
        Map<String,Object> resultMap = new HashMap<>(3);

        if(StringUtils.isBlank(param.getLicensePath())){
            param.setLicensePath(licenseGeneratePath);
        }

        if(StringUtils.isBlank(param.getStorePass())){
            param.setStorePass(storePassword);
        }

        if(StringUtils.isBlank(param.getSubject())){
            param.setSubject(licenseSubject);
        }

        if(StringUtils.isBlank(param.getPrivateAlias())){
            param.setPrivateAlias(licensePrivateAlias);
        }

        if(StringUtils.isBlank(param.getPrivateKeysStorePath())){
            param.setPrivateKeysStorePath(licensePrivateKeysStorePath);
        }

        if(StringUtils.isBlank(param.getKeyPass())){
            param.setKeyPass(licenseKeyPass);
        }

        if(param.getIssuedTime() == null){
            // 获取当前时间
            LocalDateTime currentTime = LocalDateTime.now();
            // 定义时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 格式化时间
            String formattedTime = currentTime.format(formatter);
            // 自动设置 issuedTime
            param.setIssuedTime(java.sql.Timestamp.valueOf(formattedTime));
        }

        String osName = System.getProperty("os.name").toLowerCase();
        AbstractServerInfos abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        }else{//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }

        if (abstractServerInfos.getServerInfos() == null || abstractServerInfos.getServerInfos().getIpAddress() == null || abstractServerInfos.getServerInfos().getIpAddress().isEmpty()) {
            resultMap.put("result", "error");
            resultMap.put("msg", "无法获取服务器信息！");
            return resultMap;
        }

        // 将服务器信息放入请求的 LicenseCreatorParam 中
        param.setLicenseCheckModel(abstractServerInfos.getServerInfos());

        LicenseCreator licenseCreator = new LicenseCreator(param);
        boolean result = licenseCreator.generateLicense();

        if (result) {
            try {
                // 将生成的许可证文件读取为字节数组
                byte[] licenseBytes = Files.readAllBytes(Paths.get(param.getLicensePath()));

                // 对许可证文件进行 Base64 编码，生成授权码
                String licenseCode = Base64.getEncoder().encodeToString(licenseBytes);

                resultMap.put("result", "ok");
                resultMap.put("msg", "证书文件生成成功！");
                resultMap.put("param", param);
                resultMap.put("licenseCode", licenseCode); // 返回生成的授权码

            } catch (Exception e) {
                resultMap.put("result", "error");
                resultMap.put("msg", "证书生成成功，但读取文件失败：" + e.getMessage());
            }
        } else {
            resultMap.put("result", "error");
            resultMap.put("msg", "证书文件生成失败！");
        }

        return resultMap;
    }

    @RequestMapping("/encryptGenerateRequest")
    public Map<String,Object> EncryptGenerateRequest(@RequestBody String encryptedRequest) {
        Map<String,Object> resultMap = new HashMap<>(3);
        try {
            // 解密请求体
            LicenseCreatorParam decryptedData = decryptAES(encryptedRequest);

            // 获取请求时间戳
            Date requestTimestamp = decryptedData.getLicenseCheckModel().getTimestamp();

            // 校验时间戳是否超过3分钟
            if (requestTimestamp == null) {
                resultMap.put("result", "error");
                resultMap.put("msg", "请求时间戳为空！");
                return resultMap;
            }

            // 当前时间
            Date currentTime = new Date();

            // 计算时间差（毫秒）
            long timeDifference = currentTime.getTime() - requestTimestamp.getTime();

            // 超过3分钟（3分钟 = 3 * 60 * 1000 毫秒）
            if (timeDifference > 3 * 60 * 1000 || timeDifference < 0) {
                resultMap.put("result", "error");
                resultMap.put("msg", "请求已过期或时间不合法！");
                return resultMap;
            }


            //添加服务端才有的的私钥字段
            decryptedData.setSubject(licenseSubject);
            decryptedData.setKeyPass(licenseKeyPass);
            decryptedData.setPrivateAlias(licensePrivateAlias);
            decryptedData.setLicensePath(licenseGeneratePath);
            decryptedData.setPrivateKeysStorePath(licensePrivateKeysStorePath);
            decryptedData.setStorePass(storePassword);

            System.out.println("完整的请求体：" + decryptedData);

            LicenseHistory history = new LicenseHistory();
            history.setLicenseType("某类型");
            history.setProjectName(decryptedData.getSubject());
            history.setMacAddress(decryptedData.getLicenseCheckModel().getMacAddress().toString());
            history.setIssuedTime(decryptedData.getIssuedTime());
            history.setExpiryTime(decryptedData.getExpiryTime());
            history.setPrivateKeyName(decryptedData.getPrivateAlias());
            history.setOperator(decryptedData.getConsumerType());
            history.setOperationTime(decryptedData.getLicenseCheckModel().getTimestamp());

            LicenseCreator licenseCreator = new LicenseCreator(decryptedData);
            boolean result = licenseCreator.generateLicense();

            if (result) {
                try {
                    // 将生成的许可证文件读取为字节数组
                    byte[] licenseBytes = Files.readAllBytes(Paths.get(decryptedData.getLicensePath()));

                    // 对许可证文件进行 Base64 编码，生成授权码
                    String licenseCode = Base64.getEncoder().encodeToString(licenseBytes);
                    history.setLicenseCode(licenseCode);

                    resultMap.put("result", "ok");
                    resultMap.put("msg", "证书文件生成成功！");
                    resultMap.put("param", decryptedData.toString());
                    resultMap.put("licenseCode", licenseCode); // 返回生成的授权码

                } catch (Exception e) {
                    resultMap.put("result", "error");
                    resultMap.put("msg", "证书生成成功，但读取文件失败：" + e.getMessage());
                }
            } else {
                resultMap.put("result", "error");
                resultMap.put("msg", "证书文件生成失败！");
            }

            licenseHistoryService.save(history);
            return resultMap;

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", e.getMessage());
            return resultMap;
        }
    }

    // AES 解密方法
    private static LicenseCreatorParam decryptAES(String encryptedData) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(LicenseCreatorController.AES_KEY.getBytes());
        keyGen.init(128, secureRandom);
        SecretKey secretKey = keyGen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        String json = new String(decryptedData, "UTF-8");

        // 使用 Jackson 将 JSON 映射为 LicenseCreatorParam 对象
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, LicenseCreatorParam.class);
    }

}
