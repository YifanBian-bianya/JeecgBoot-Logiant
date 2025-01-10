package com.logiant.modules.system.model;

import lombok.Data;
import com.logiant.modules.system.entity.SysDepart;
import com.logiant.modules.system.entity.SysUser;

/**
 * 包含 SysUser 和 SysDepart 的 Model
 *
 * @author sunjianlei
 */
@Data
public class SysUserSysDepartModel {

    private String id;
    private String realname;
    private String workNo;
    private String post;
    private String telephone;
    private String email;
    private String phone;
    private String departId;
    private String departName;
    private String avatar;
    private String sex;
    private String birthday;

}
