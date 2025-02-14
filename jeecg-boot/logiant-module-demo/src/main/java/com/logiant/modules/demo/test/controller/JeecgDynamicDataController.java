package com.logiant.modules.demo.test.controller;

import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.logiant.common.api.vo.Result;
import com.logiant.common.aspect.annotation.AutoLog;
import com.logiant.common.system.base.controller.JeecgController;
import com.logiant.modules.demo.test.entity.JeecgDemo;
import com.logiant.modules.demo.test.service.IJeecgDemoService;
import com.logiant.modules.demo.test.service.IJeecgDynamicDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 动态数据源测试
 * @Author: zyf
 * @Date:2020-04-21
 */
@Slf4j
@Api(tags = "动态数据源测试")
@RestController
@RequestMapping("/test/dynamic")
public class JeecgDynamicDataController extends JeecgController<JeecgDemo, IJeecgDemoService> {

    @Autowired
    private IJeecgDynamicDataService jeecgDynamicDataService;


    /**
     * 动态切换数据源

     * @return
     */
    @PostMapping(value = "/test1")
    @AutoLog(value = "动态切换数据源")
    @ApiOperation(value = "动态切换数据源", notes = "动态切换数据源")
    public Result<List<JeecgDemo>> selectSpelByKey(@RequestParam(required = false) String dsName) {
        List<JeecgDemo> list = jeecgDynamicDataService.selectSpelByKey(dsName);
        return Result.OK(list);
    }


}
