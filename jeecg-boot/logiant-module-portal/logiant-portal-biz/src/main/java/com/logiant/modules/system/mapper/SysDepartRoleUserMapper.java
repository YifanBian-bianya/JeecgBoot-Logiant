package com.logiant.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.logiant.modules.system.entity.SysDepartRoleUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 部门角色人员信息
 * @Author: jeecg-boot
 * @Date:   2020-02-13
 * @Version: V1.0
 */
public interface SysDepartRoleUserMapper extends BaseMapper<SysDepartRoleUser> {

    void deleteByRoleIds(@Param("ids")List<String> ids);
}
