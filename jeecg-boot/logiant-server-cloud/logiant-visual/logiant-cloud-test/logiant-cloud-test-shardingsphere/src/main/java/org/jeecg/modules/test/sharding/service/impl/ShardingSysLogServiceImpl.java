package com.logiant.modules.test.sharding.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.logiant.modules.test.sharding.entity.ShardingSysLog;
import com.logiant.modules.test.sharding.mapper.ShardingSysLogMapper;
import com.logiant.modules.test.sharding.service.IShardingSysLogService;
import org.springframework.stereotype.Service;

/**
 * 系统日志表 服务实现类
 * @author: zyf
 * @date: 2022/04/21
 */
@Service
@DS("sharding")
public class ShardingSysLogServiceImpl extends ServiceImpl<ShardingSysLogMapper, ShardingSysLog> implements IShardingSysLogService {

}
