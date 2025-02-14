package com.logiant.modules.demo.test.service;

import com.logiant.common.system.base.service.JeecgService;
import com.logiant.modules.demo.test.entity.JeecgDemo;

import java.util.List;

/**
 * @Description: 动态数据源测试
 * @Author: zyf
 * @Date:2020-04-21
 */
public interface IJeecgDynamicDataService extends JeecgService<JeecgDemo> {

	/**
	 * 测试从header获取数据源
	 * @return
	 */
	public List<JeecgDemo> selectSpelByHeader();

	/**
	 * 使用spel从参数获取
	 * @param dsName
	 * @return
	 */
	public  List<JeecgDemo> selectSpelByKey(String dsName);

}
