package com.logiant.common.system.api.factory;

import org.springframework.cloud.openfeign.FallbackFactory;
import com.logiant.common.system.api.ISysBaseAPI;
import com.logiant.common.system.api.fallback.SysBaseAPIFallback;
import org.springframework.stereotype.Component;

/**
 * @Description: SysBaseAPIFallbackFactory
 * @author: jeecg-boot
 */
@Component
public class SysBaseAPIFallbackFactory implements FallbackFactory<ISysBaseAPI> {

    @Override
    public ISysBaseAPI create(Throwable throwable) {
        SysBaseAPIFallback fallback = new SysBaseAPIFallback();
        fallback.setCause(throwable);
        return fallback;
    }
}