//package com.logiant.modules.test.feign.client;
//
//import com.logiant.common.api.vo.Result;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
///**
// * 动态feign接口定义
// */
//public interface JeecgTestClientDyn {
//
//    @GetMapping(value = "/test/getMessage")
//    Result<String> getMessage(@RequestParam(value = "name",required = false) String name);
//}
