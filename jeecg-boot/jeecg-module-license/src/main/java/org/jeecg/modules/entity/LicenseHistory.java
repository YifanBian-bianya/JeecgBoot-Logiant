package org.jeecg.modules.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "license_history")
public class LicenseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licenseType;

    private String projectName;

    private String macAddress;

    private String username;

    private Date issuedTime;

    private Date expiryTime;

    private String privateKeyName;

    private String operator;

    private Date operationTime;

    @Lob
    private String licenseCode; // 授权码
}
