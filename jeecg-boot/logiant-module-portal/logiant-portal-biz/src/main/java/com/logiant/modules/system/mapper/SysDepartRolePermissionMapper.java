package com.logiant.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.logiant.modules.system.entity.SysDepartRolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 部门角色权限
 * @Author: jeecg-boot
 * @Date:   2020-02-12
 * @Version: V1.0
 */
public interface SysDepartRolePermissionMapper extends BaseMapper<SysDepartRolePermission> {

    void deleteByRoleIds(@Param("ids")List<String> ids);
}
