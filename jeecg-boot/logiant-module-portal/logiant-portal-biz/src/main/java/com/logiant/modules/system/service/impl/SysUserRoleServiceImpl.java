package com.logiant.modules.system.service.impl;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import com.logiant.modules.system.entity.SysRole;
import com.logiant.modules.system.entity.SysUser;
import com.logiant.modules.system.entity.SysUserRole;
import com.logiant.modules.system.mapper.SysUserRoleMapper;
import com.logiant.modules.system.service.ISysRoleService;
import com.logiant.modules.system.service.ISysUserRoleService;
import com.logiant.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

}
