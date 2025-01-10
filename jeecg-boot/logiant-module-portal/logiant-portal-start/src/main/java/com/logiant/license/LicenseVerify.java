package com.logiant.license;

import de.schlichtherle.license.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.logiant.modules.license.CustomKeyStoreParam;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.prefs.Preferences;

/**
 * License校验类
 *
 * @author zifangsky
 * @date 2018/4/20
 * @since 1.0.0
 */
public class LicenseVerify {
    private static Logger logger = LogManager.getLogger(LicenseVerify.class);

    /**
     * 安装License证书
     * @author zifangsky
     * @date 2018/4/20 16:26
     * @since 1.0.0
     */
    public synchronized LicenseContent install(LicenseVerifyParam param){
        LicenseContent result = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //1. 安装证书
        try{
            LicenseManager licenseManager = LicenseManagerHolder.getInstance(initLicenseParam(param));
            licenseManager.uninstall();

            result = licenseManager.install(new File(param.getLicensePath()));
            logger.info(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}",format.format(result.getNotBefore()),format.format(result.getNotAfter())));
        }catch (Exception e){
            logger.error("证书安装失败！",e);
        }

        return result;
    }

    /**
     * 使用授权码安装证书
     *
     * @param licenseCode 授权码 (Base64 编码的 License)
     * @return LicenseContent 证书内容
     */
    public synchronized LicenseContent installWithLicenseCode(String licenseCode) {
        LicenseContent result = null;
        LicenseVerifyParam param = new LicenseVerifyParam("","","","","",licenseCode);
        try {
            // 将 Base64 授权码解码为文件路径
            String tempFilePath = decodeLicenseCodeToFile(licenseCode);

            if (tempFilePath != null) {
                logger.info("授权码成功转换为临时证书文件: " + tempFilePath);

                // 使用生成的临时文件安装证书
                param.setLicensePath(tempFilePath);
                result = install(param);

                // 删除临时文件
                new File(tempFilePath).delete();
                logger.info("安装完成后删除临时证书文件: {}", tempFilePath);
            } else {
                logger.error("授权码转换为文件失败！");
            }
        } catch (Exception e) {
            logger.error("使用授权码安装证书失败！", e);
        }

        return result;
    }

    /**
     * 将 Base64 授权码解码为证书文件
     *
     * @param licenseCode Base64 授权码
     * @return 解码后生成的临时文件路径
     */
    private String decodeLicenseCodeToFile(String licenseCode) {
        try {
            // 解码 Base64 授权码
            byte[] decodedBytes = Base64.getDecoder().decode(licenseCode);

            // 创建临时文件
            File tempFile = File.createTempFile("license", ".lic");

            // 写入解码后的字节内容到临时文件
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(decodedBytes);
                fos.flush();
            }

            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            logger.error("解码授权码为文件失败！", e);
            return null;
        }
    }

    /**
     * 校验License证书
     * @author zifangsky
     * @date 2018/4/20 16:26
     * @since 1.0.0
     * @return boolean
     */
    public boolean verify(){
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //2. 校验证书
        try {
            LicenseContent licenseContent = licenseManager.verify();
//            System.out.println(licenseContent.getSubject());

            logger.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}",format.format(licenseContent.getNotBefore()),format.format(licenseContent.getNotAfter())));
            return true;
        }catch (Exception e){
            logger.error("证书校验失败！",e);
            return false;
        }
    }

    /**
     * 初始化证书生成参数
     * @author zifangsky
     * @date 2018/4/20 10:56
     * @since 1.0.0
     * @param param License校验类需要的参数
     * @return de.schlichtherle.license.LicenseParam
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param){
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);

        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class
                ,param.getPublicKeysStorePath()
                ,param.getPublicAlias()
                ,param.getStorePass()
                ,null);

        return new DefaultLicenseParam(param.getSubject()
                ,preferences
                ,publicStoreParam
                ,cipherParam);
    }

}
