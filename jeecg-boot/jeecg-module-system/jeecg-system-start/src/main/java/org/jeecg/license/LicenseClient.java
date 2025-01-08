package org.jeecg.license;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jeecg.modules.license.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LicenseClient {

    private static final String AES_KEY = "your_secure_key"; // 固定的AES密钥
    private static Logger logger = LogManager.getLogger(LicenseCheckListener.class);

    public Map<String, Object> sendRequest(LicenseCreatorParam param) throws Exception {
        // 构造请求体，使用TreeMap实现升序
        Map<String, Object> requestBody = new TreeMap<>();
        Map<String, Object> resultMap = new HashMap<>(3);

        //1. 用户输入的信息
        requestBody.put("subject", param.getSubject());
        requestBody.put("consumerType", param.getConsumerType());
        requestBody.put("consumerAmount", param.getConsumerAmount());
        requestBody.put("description", param.getDescription());
        requestBody.put("issuedTime", param.getIssuedTime());
        requestBody.put("expiryTime", param.getExpiryTime());

        //2. 获取IP，Mac
        String osName = System.getProperty("os.name").toLowerCase();
        AbstractServerInfos abstractServerInfos = null;
        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        } else {//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }

        if (abstractServerInfos.getServerInfos() == null || abstractServerInfos.getServerInfos().getIpAddress() == null || abstractServerInfos.getServerInfos().getIpAddress().isEmpty()) {
            resultMap.put("result", "error");
            resultMap.put("msg", "无法获取服务器信息！");
            return resultMap;
        }

        // 将服务器信息放入请求的 LicenseCreatorParam 中
        requestBody.put("licenseCheckModel", abstractServerInfos.getServerInfos());

        //3. 补充时间
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化时间
        String formattedTime = currentTime.format(formatter);

        if (requestBody.get("issuedTime") == null) {
            // 设置 issuedTime
            requestBody.put("issuedTime", java.sql.Timestamp.valueOf(formattedTime));
        }

        LicenseCheckModel licenseCheckModel = (LicenseCheckModel) requestBody.get("licenseCheckModel");
        licenseCheckModel.setTimestamp(java.sql.Timestamp.valueOf(formattedTime)); // 设置时间戳
        requestBody.put("licenseCheckModel", licenseCheckModel); // 第二次更新 LicenseCheckModel

        logger.info("param", param);

        // 将请求体转为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        // 对请求体进行 AES 加密
        String encryptedRequest = encryptAES(jsonRequestBody);

        // 调用服务端 API，发送到服务端
        logger.info("发送的加密请求体：{}", encryptedRequest);
        return sendPostRequest(encryptedRequest);
    }

    // AES 加密方法
    private static String encryptAES(String data) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(LicenseClient.AES_KEY.getBytes());
        keyGen.init(128, secureRandom);
        SecretKey secretKey = keyGen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // 模拟发送 POST 请求
    private static Map<String, Object> sendPostRequest(String encryptedData) {
        HttpClient client = HttpClient.newHttpClient();

        // 创建请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:7000/license/encryptGenerateRequest"))
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(encryptedData))
                .build();

        try {
            // Java 11的简单版 发送请求并获取响应
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 打印响应内容
            String responseBody = response.body();

            // 处理响应
            return Map.of("result", responseBody);

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("result", "Error: " + e.getMessage());
        }
    }
}
